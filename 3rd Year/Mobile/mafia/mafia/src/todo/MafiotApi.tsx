import axios from 'axios';
import { getLogger } from '../core';
import { MafiotProps } from './MafiotProps';

const log = getLogger('MafiotApi');

const baseUrl = '127.0.0.1:3000';
const mafiotUrl = `http://${baseUrl}/mafiot`;

function withLogs<T>(promise: Promise<any>, fnName: string): Promise<T> {
    log(`${fnName} - started`);
    return promise
        .then((res: any) => {
            log(`${fnName} - succeeded`);
            return Promise.resolve(res.data as T);
        })
        .catch((err) => {
            log(`${fnName} - failed`);
            return Promise.reject(err);
        });
}

const config = {
    headers: { 'Content-Type': 'application/json' },
};

export const getMafiots = (): Promise<MafiotProps[]> =>
    withLogs<MafiotProps[]>(axios.get(mafiotUrl, config), 'getMafiots');

export const createMafiot = (mafiot: MafiotProps): Promise<MafiotProps> =>
    withLogs<MafiotProps>(axios.post(mafiotUrl, mafiot, config), 'createMafiot');

export const updateMafiot = (mafiot: MafiotProps): Promise<MafiotProps> =>
    withLogs<MafiotProps>(
        axios.put(`${mafiotUrl}/${mafiot.id}`, mafiot, config),
        'updateMafiot'
    );

export interface MessageData {
    event: string;
    payload: {
        mafiot?: MafiotProps;
        mafiots?: MafiotProps[];
    };
}

export const newWebSocket = (onMessage: (data: MessageData) => void) => {
    let ws: WebSocket | null = null;
    let reconnectTimer: ReturnType<typeof setTimeout> | null = null;
    let isClosed = false;
    let reconnectAttempts = 0;
    const maxReconnectDelay = 10000;

    const connect = () => {
        if (isClosed) {
            log('web socket - not connecting (closed)');
            return;
        }

        log(`web socket - connecting (attempt ${reconnectAttempts + 1})`);
        ws = new WebSocket(`ws://${baseUrl}`);

        ws.onopen = () => {
            log('web socket onopen');
            reconnectAttempts = 0; // Reset counter on successful connection
            if (reconnectTimer) {
                clearTimeout(reconnectTimer);
                reconnectTimer = null;
            }
        };

        ws.onclose = () => {
            log('web socket onclose');
            ws = null;

            if (!isClosed) {
                // Calculate exponential backoff: 1s, 2s, 4s, 8s, max 10s
                const delay = Math.min(1000 * Math.pow(2, reconnectAttempts), maxReconnectDelay);
                reconnectAttempts++;

                log(`web socket - reconnecting in ${delay}ms...`);
                reconnectTimer = setTimeout(connect, delay);
            }
        };

        ws.onerror = (error) => {
            log('web socket onerror', error);
        };

        ws.onmessage = (messageEvent) => {
            try {
                const raw = JSON.parse(messageEvent.data);
                const normalized: MessageData = raw.event
                    ? raw
                    : {
                        event: raw.type,
                        payload: raw.payload ?? {
                            mafiot: raw.data,
                            mafiots: raw.items ?? raw.list ?? undefined,
                        },
                    };
                log(`web socket onmessage: ${normalized.event}`);
                onMessage(normalized);
            } catch (e) {
                log('web socket parse error', e);
            }
        };
    };

    connect();

    return () => {
        log('web socket - cleanup called');
        isClosed = true;
        if (reconnectTimer) {
            clearTimeout(reconnectTimer);
            reconnectTimer = null;
        }
        if (ws) {
            ws.close();
            ws = null;
        }
    };
};