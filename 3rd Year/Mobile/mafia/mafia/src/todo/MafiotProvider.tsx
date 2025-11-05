import React, { useCallback, useContext, useEffect, useReducer, useState } from 'react';
import { getLogger } from '../core';
import { MafiotProps } from './MafiotProps';
import { createMafiot, getMafiots, newWebSocket, updateMafiot, deleteMafiot, MessageData } from './MafiotApi';
import { AuthContext } from '../auth/AuthProvider';
import { useNetwork } from '../core/useNetwork';
import { storageService, PendingOperation } from '../core/storage';

const log = getLogger('MafiotProvider');

type SaveMafiotFn = (mafiot: MafiotProps) => Promise<MafiotProps>;
type DeleteMafiotFn = (id: string) => Promise<void>;

export interface MafiotState {
    mafiots?: MafiotProps[];
    fetching: boolean;
    fetchingError?: Error | null;
    saving: boolean;
    savingError?: Error | null;
    saveMafiot?: SaveMafiotFn;
    deleteMafiot?: DeleteMafiotFn;
    hasMore: boolean;
    page: number;
    loadMore?: () => Promise<void>;
    pendingOperations: PendingOperation[];
    searchText?: string;
    setSearchText?: (text: string) => void;
    balanceFilter?: string;
    setBalanceFilter?: (filter: string) => void;
}

interface ActionProps {
    type: string;
    payload?: {
        mafiots?: MafiotProps[];
        mafiot?: MafiotProps;
        error?: Error;
        page?: number;
        hasMore?: boolean;
        pendingOperations?: PendingOperation[];
    };
}

const initialState: MafiotState = {
    fetching: false,
    saving: false,
    hasMore: true,
    page: 1,
    pendingOperations: [],
};

const FETCH_ITEMS_STARTED = 'FETCH_ITEMS_STARTED';
const FETCH_ITEMS_SUCCEEDED = 'FETCH_ITEMS_SUCCEEDED';
const FETCH_ITEMS_FAILED = 'FETCH_ITEMS_FAILED';
const SAVE_ITEM_STARTED = 'SAVE_ITEM_STARTED';
const SAVE_ITEM_SUCCEEDED = 'SAVE_ITEM_SUCCEEDED';
const SAVE_ITEM_FAILED = 'SAVE_ITEM_FAILED';
const DELETE_ITEM_SUCCEEDED = 'DELETE_ITEM_SUCCEEDED';
const REPLACE_ITEMS = 'REPLACE_ITEMS';
const APPEND_ITEMS = 'APPEND_ITEMS';
const UPDATE_PENDING_OPS = 'UPDATE_PENDING_OPS';

const reducer: (state: MafiotState, action: ActionProps) => MafiotState =
    (state, { type, payload }) => {
        switch (type) {
            case FETCH_ITEMS_STARTED:
                return { ...state, fetching: true, fetchingError: null };
            case FETCH_ITEMS_SUCCEEDED:
                return {
                    ...state,
                    mafiots: payload?.mafiots,
                    fetching: false,
                    page: payload?.page || 1,
                    hasMore: payload?.hasMore || false,
                };
            case APPEND_ITEMS: {
                const existingMafiots = state.mafiots || [];
                const newMafiots = payload?.mafiots || [];
                return {
                    ...state,
                    mafiots: [...existingMafiots, ...newMafiots],
                    fetching: false,
                    page: payload?.page || state.page,
                    hasMore: payload?.hasMore || false,
                };
            }
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
                    storageService.saveMafiots([mafiot]);
                }
                return { ...state, mafiots, saving: false };
            }

            case DELETE_ITEM_SUCCEEDED: {
                const toDelete = payload?.mafiot;
                const mafiots = (state.mafiots || []).filter(it => it.id !== toDelete?.id);
                // Șterge item din localStorage
                const stored = storageService.getMafiots().filter(m => m.id !== toDelete?.id);
                localStorage.setItem('mafiots', JSON.stringify(stored));
                return { ...state, mafiots };
            }
            case REPLACE_ITEMS:
                return { ...state, mafiots: payload?.mafiots };
            case SAVE_ITEM_FAILED:
                return { ...state, savingError: payload?.error, saving: false };
            case UPDATE_PENDING_OPS:
                return { ...state, pendingOperations: payload?.pendingOperations || [] };
            default:
                return state;
        }
    };

export const MafiotContext = React.createContext<MafiotState>(initialState);

interface MafiotProviderProps {
    children: React.ReactNode;
}

export const MafiotProvider: React.FC<MafiotProviderProps> = ({ children }) => {
    const [state, dispatch] = useReducer(reducer, initialState);
    const { mafiots, fetching, fetchingError, saving, savingError, page, hasMore, pendingOperations } = state;
    const { token } = useContext(AuthContext);
    const networkStatus = useNetwork();
    const [searchText, setSearchText] = useState('');
    const [balanceFilter, setBalanceFilter] = useState('');
    const [isSyncing, setIsSyncing] = useState(false);

    // Load from local storage on mount
    useEffect(() => {
        const storedMafiots = storageService.getMafiots();
        const storedPendingOps = storageService.getPendingOperations();

        if (storedMafiots.length > 0) {
            log('Loaded mafiots from local storage');
            dispatch({ type: REPLACE_ITEMS, payload: { mafiots: storedMafiots } });
        }

        if (storedPendingOps.length > 0) {
            log('Loaded pending operations from local storage');
            dispatch({ type: UPDATE_PENDING_OPS, payload: { pendingOperations: storedPendingOps } });
        }
    }, []);

    // Fetch initial mafiots
    useEffect(() => {
        if (token) {
            fetchMafiots(1);
        }
    }, [token]);

    // Sync pending operations when online
    useEffect(() => {
        if (networkStatus.connected && pendingOperations.length > 0 && !isSyncing) {
            log('Network connected - syncing pending operations');
            syncPendingOperations();
        }
    }, [networkStatus.connected, pendingOperations.length, isSyncing]);

    // WebSocket effect
    useEffect(() => {
        if (!token) return;

        let canceled = false;
        log('wsEffect - connecting');
        const closeWebSocket = newWebSocket(token, (message: MessageData) => {
            if (canceled) return;
            const { event, payload } = message;
            const { mafiot } = payload || {};
            log(`ws message, event = ${event}`);

            switch (event) {
                case 'created':
                case 'updated':
                    if (mafiot) {
                        dispatch({ type: SAVE_ITEM_SUCCEEDED, payload: { mafiot } });
                    }
                    break;
                case 'deleted':
                    if (mafiot) {
                        dispatch({ type: DELETE_ITEM_SUCCEEDED, payload: { mafiot } });
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
    }, [token]);

    // Save to local storage whenever mafiots change
    useEffect(() => {
        if (mafiots) {
            storageService.saveMafiots(mafiots);
        }
    }, [mafiots]);

    async function fetchMafiots(pageNum: number, append: boolean = false) {
        if (!networkStatus.connected) {
            log('Offline - using cached data');
            return;
        }

        try {
            log(`fetchMafiots started - page ${pageNum}`);
            dispatch({ type: FETCH_ITEMS_STARTED });
            const response = await getMafiots(pageNum, 5); // 5 items pe pagină
            log('fetchMafiots succeeded');

            const actionType = append ? APPEND_ITEMS : FETCH_ITEMS_SUCCEEDED;
            dispatch({
                type: actionType,
                payload: {
                    mafiots: response.mafiots,
                    page: response.page,
                    hasMore: response.page < response.totalPages,
                }
            });
        } catch (error) {
            log('fetchMafiots failed', error);
            dispatch({ type: FETCH_ITEMS_FAILED, payload: { error: error as Error } });
        }
    }

    // const loadMore = useCallback(async () => {
    //     if (fetching || !hasMore) return;
    //     await fetchMafiots(page + 1, true);
    // }, [page, fetching, hasMore]);

    const loadMore = useCallback(async () => {
        if (fetching || !hasMore) return;
        try {
            const nextPage = page + 1;
            const response = await getMafiots(nextPage, 5); // 5 items pe pagină
            dispatch({
                type: 'APPEND_ITEMS',
                payload: {
                    mafiots: response.mafiots,
                    page: nextPage,
                    hasMore: nextPage < response.totalPages
                }
            });
        } catch (error) {
            log('loadMore failed', error);
        }
    }, [page, hasMore, fetching]);

    async function syncPendingOperations() {
        if (isSyncing) {
            log('Sync already in progress, skipping');
            return;
        }

        setIsSyncing(true);
        const ops = storageService.getPendingOperations();
        log(`Syncing ${ops.length} pending operations`);

        for (const op of ops) {
            try {
                switch (op.type) {
                    case 'create':
                        await createMafiot(op.mafiot);
                        break;
                    case 'update':
                        await updateMafiot(op.mafiot);
                        break;
                    case 'delete':
                        await deleteMafiot(op.mafiot.id!);
                        break;
                }
                storageService.removePendingOperation(op.id);
                log(`Synced operation ${op.id}`);
            } catch (error) {
                log(`Failed to sync operation ${op.id}`, error);
                // Keep operation in queue for later retry
            }
        }

        // Update state with remaining pending ops
        const remaining = storageService.getPendingOperations();
        dispatch({ type: UPDATE_PENDING_OPS, payload: { pendingOperations: remaining } });
        setIsSyncing(false);
    }

    async function saveMafiotCallback(mafiot: MafiotProps): Promise<MafiotProps> {
        try {
            log('saveMafiot started');
            dispatch({ type: SAVE_ITEM_STARTED });

            if (networkStatus.connected) {
                // Try to save to server
                try {
                    const savedMafiot = mafiot.id ? await updateMafiot(mafiot) : await createMafiot(mafiot);
                    log('saveMafiot succeeded');
                    dispatch({ type: SAVE_ITEM_SUCCEEDED, payload: { mafiot: savedMafiot } });
                    return savedMafiot;
                } catch (error) {
                    log('saveMafiot failed - adding to pending operations', error);
                    // Add to pending operations and update local state optimistically
                    dispatch({ type: SAVE_ITEM_SUCCEEDED, payload: { mafiot } });
                    storageService.addPendingOperation({
                        type: mafiot.id ? 'update' : 'create',
                        mafiot,
                    });
                    const ops = storageService.getPendingOperations();
                    dispatch({ type: UPDATE_PENDING_OPS, payload: { pendingOperations: ops } });
                    return mafiot;
                }
            } else {
                // Offline - add to pending operations and update local state
                log('Offline - adding to pending operations');
                dispatch({ type: SAVE_ITEM_SUCCEEDED, payload: { mafiot } });
                storageService.addPendingOperation({
                    type: mafiot.id ? 'update' : 'create',
                    mafiot,
                });
                const ops = storageService.getPendingOperations();
                dispatch({ type: UPDATE_PENDING_OPS, payload: { pendingOperations: ops } });
                return mafiot;
            }
        } catch (error) {
            log('saveMafiot failed');
            dispatch({ type: SAVE_ITEM_FAILED, payload: { error: error as Error } });
            throw error;
        }
    }

    const saveMafiot = useCallback<SaveMafiotFn>(saveMafiotCallback, [networkStatus.connected]);

    async function deleteMafiotCallback(id: string): Promise<void> {
        try {
            log('deleteMafiot started', id);

            // Find mafiot to delete
            const mafiotToDelete = mafiots?.find(m => m.id === id);
            if (!mafiotToDelete) {
                log('Mafiot not found', id);
                return;
            }

            // Remove from local state immediately (optimistic)
            dispatch({ type: DELETE_ITEM_SUCCEEDED, payload: { mafiot: mafiotToDelete } });

            if (networkStatus.connected) {
                // Try to delete from server
                try {
                    await deleteMafiot(id);
                    log('deleteMafiot succeeded');
                } catch (error) {
                    log('deleteMafiot failed - adding to pending operations', error);
                    // Add to pending operations
                    storageService.addPendingOperation({
                        type: 'delete',
                        mafiot: mafiotToDelete,
                    });
                    const ops = storageService.getPendingOperations();
                    dispatch({ type: UPDATE_PENDING_OPS, payload: { pendingOperations: ops } });
                    throw error;
                }
            } else {
                // Offline - add to pending operations
                log('Offline - adding delete to pending operations');
                storageService.addPendingOperation({
                    type: 'delete',
                    mafiot: mafiotToDelete,
                });
                const ops = storageService.getPendingOperations();
                dispatch({ type: UPDATE_PENDING_OPS, payload: { pendingOperations: ops } });
            }
        } catch (error) {
            log('deleteMafiot failed', error);
            throw error;
        }
    }

    const deleteMafiotFn = useCallback<DeleteMafiotFn>(deleteMafiotCallback, [networkStatus.connected, mafiots]);

    // Filter mafiots based on search text and balance filter
    const filteredMafiots = mafiots?.filter(m => {
        // Search filter
        if (searchText) {
            const search = searchText.toLowerCase();
            const matchesSearch =
                m.nume.toLowerCase().includes(search) ||
                m.prenume.toLowerCase().includes(search) ||
                m.balanta.toLowerCase().includes(search);
            if (!matchesSearch) return false;
        }

        // Balance filter
        if (balanceFilter) {
            const balance = parseFloat(m.balanta) || 0;
            switch (balanceFilter) {
                case 'positive':
                    return balance > 0;
                case 'negative':
                    return balance < 0;
                case 'zero':
                    return balance === 0;
                default:
                    return true;
            }
        }

        return true;
    });

    const value = {
        mafiots: filteredMafiots,
        fetching,
        fetchingError,
        saving,
        savingError,
        saveMafiot,
        deleteMafiot: deleteMafiotFn,
        hasMore,
        page,
        loadMore,
        pendingOperations,
        searchText,
        setSearchText,
        balanceFilter,
        setBalanceFilter,
    };

    log('returns');
    return (
        <MafiotContext.Provider value={value}>
            {children}
        </MafiotContext.Provider>
    );
};
