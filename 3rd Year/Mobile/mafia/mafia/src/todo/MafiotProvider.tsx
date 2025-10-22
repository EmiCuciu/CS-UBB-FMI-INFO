import React, { useCallback, useEffect, useReducer } from 'react';
import { getLogger } from '../core';
import { MafiotProps } from './MafiotProps';
import { createMafiot, getMafiots, newWebSocket, updateMafiot, MessageData } from './MafiotApi';

const log = getLogger('MafiotProvider');

type SaveMafiotFn = (mafiot: MafiotProps) => Promise<MafiotProps>;

export interface MafiotState {
    mafiots?: MafiotProps[],
    fetching: boolean,
    fetchingError?: Error | null,
    saving: boolean,
    savingError?: Error | null,
    saveMafiot?: SaveMafiotFn,
}

interface ActionProps {
    type: string,
    payload?: {
        mafiots?: MafiotProps[];
        mafiot?: MafiotProps;
        error?: Error;
    },
}

const initialState: MafiotState = {
    fetching: false,
    saving: false,
};

const FETCH_ITEMS_STARTED = 'FETCH_ITEMS_STARTED';
const FETCH_ITEMS_SUCCEEDED = 'FETCH_ITEMS_SUCCEEDED';
const FETCH_ITEMS_FAILED = 'FETCH_ITEMS_FAILED';
const SAVE_ITEM_STARTED = 'SAVE_ITEM_STARTED';
const SAVE_ITEM_SUCCEEDED = 'SAVE_ITEM_SUCCEEDED';
const SAVE_ITEM_FAILED = 'SAVE_ITEM_FAILED';
const DELETE_ITEM_SUCCEEDED = 'DELETE_ITEM_SUCCEEDED';
const REPLACE_ITEMS = 'REPLACE_ITEMS';

const reducer: (state: MafiotState, action: ActionProps) => MafiotState =
    (state, { type, payload }) => {
        switch (type) {
            case FETCH_ITEMS_STARTED:
                return { ...state, fetching: true, fetchingError: null };
            case FETCH_ITEMS_SUCCEEDED:
                return { ...state, mafiots: payload?.mafiots, fetching: false };
            case FETCH_ITEMS_FAILED:
                return { ...state, fetchingError: payload?.error, fetching: false };
            case SAVE_ITEM_STARTED:
                return { ...state, savingError: null, saving: true };
            case SAVE_ITEM_SUCCEEDED: {
                const mafiots = [...(state.mafiots || [])];
                const mafiot = payload?.mafiot;
                if (mafiot) {
                    const index = mafiots.findIndex(it => it.id === mafiot.id);
                    if (index === -1) {
                        mafiots.splice(0, 0, mafiot);
                    } else {
                        mafiots[index] = mafiot;
                    }
                }
                return { ...state, mafiots, saving: false };
            }
            case DELETE_ITEM_SUCCEEDED: {
                const toDelete = payload?.mafiot;
                const mafiots = (state.mafiots || []).filter(it => it.id !== toDelete?.id);
                return { ...state, mafiots };
            }
            case REPLACE_ITEMS:
                return { ...state, mafiots: payload?.mafiots };
            case SAVE_ITEM_FAILED:
                return { ...state, savingError: payload?.error, saving: false };
            default:
                return state;
        }
    };

export const MafiotContext = React.createContext<MafiotState>(initialState);

interface MafiotProviderProps {
    children: React.ReactNode,
}

export const MafiotProvider: React.FC<MafiotProviderProps> = ({ children }) => {
    const [state, dispatch] = useReducer(reducer, initialState);
    const { mafiots, fetching, fetchingError, saving, savingError } = state;

    useEffect(getMafiotsEffect, []);
    useEffect(wsEffect, []);

    async function saveMafiotCallback(mafiot: MafiotProps): Promise<MafiotProps> {
        try {
            log('saveMafiot started');
            dispatch({ type: SAVE_ITEM_STARTED });
            const savedMafiot = mafiot.id ? await updateMafiot(mafiot) : await createMafiot(mafiot);
            log('saveMafiot succeeded');
            dispatch({ type: SAVE_ITEM_SUCCEEDED, payload: { mafiot: savedMafiot } });
            return savedMafiot;
        } catch (error) {
            log('saveMafiot failed');
            dispatch({ type: SAVE_ITEM_FAILED, payload: { error: error as Error } });
            throw error;
        }
    }

    const saveMafiot = useCallback<SaveMafiotFn>(saveMafiotCallback, []);

    const value = { mafiots, fetching, fetchingError, saving, savingError, saveMafiot };
    log('returns');
    return (
        <MafiotContext.Provider value={value}>
            {children}
        </MafiotContext.Provider>
    );

    function getMafiotsEffect() {
        let canceled = false;
        fetchItems();
        return () => { canceled = true; };

        async function fetchItems() {
            try {
                log('fetchItems started');
                dispatch({ type: FETCH_ITEMS_STARTED });
                const items = await getMafiots();
                log('fetchItems succeeded');
                if (!canceled) {
                    dispatch({ type: FETCH_ITEMS_SUCCEEDED, payload: { mafiots: items } });
                }
            } catch (error) {
                log('fetchItems failed');
                if (!canceled) {
                    dispatch({ type: FETCH_ITEMS_FAILED, payload: { error: error as Error } });
                }
            }
        }
    }

    function wsEffect() {
        let canceled = false;
        log('wsEffect - connecting');
        const closeWebSocket = newWebSocket((message: MessageData) => {
            if (canceled) return;
            const { event, payload } = message;
            const { mafiot, mafiots } = payload || {};
            log(`ws message, event = ${event}`);
            switch (event) {
                case 'list':
                    if (mafiots) {
                        dispatch({ type: REPLACE_ITEMS, payload: { mafiots } });
                    }
                    break;
                default:
                    break;
            }
        });

        return () => {
            log('wsEffect - disconnecting');
            canceled = true;
            closeWebSocket();
        };
    }


}