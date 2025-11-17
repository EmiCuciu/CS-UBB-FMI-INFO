import axios, { AxiosResponse } from 'axios';
import { getLogger } from '../core';
import { MafiotProps } from './MafiotProps';

const log = getLogger('MafiotApi');

const baseUrl = '127.0.0.1:3000';
const mafiotUrl = `http://${baseUrl}/api/mafiot`;

function withLogs<T>(promise: Promise<AxiosResponse<T>>, fnName: string): Promise<T> {
    log(`${fnName} - started`);
    return promise
        .then((res: AxiosResponse<T>) => {
            log(`${fnName} - succeeded`);
            return Promise.resolve(res.data);
        })
        .catch((err) => {
            log(`${fnName} - failed`, err);
            return Promise.reject(err);
        });
}

const getConfig = () => {
    const token = localStorage.getItem('token');
    return {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
        },
    };
};

interface PaginatedResponse {
    mafiots: MafiotProps[];
    page: number;
    limit: number;
    total: number;
    totalPages: number;
}

export const getMafiots = (page: number = 1, limit: number = 10): Promise<PaginatedResponse> =>
    withLogs<PaginatedResponse>(
        axios.get(`${mafiotUrl}?page=${page}&limit=${limit}`, getConfig()),
        'getMafiots'
    );

export const createMafiot = (mafiot: MafiotProps): Promise<MafiotProps> =>
    withLogs<MafiotProps>(axios.post(mafiotUrl, mafiot, getConfig()), 'createMafiot');

export const updateMafiot = (mafiot: MafiotProps): Promise<MafiotProps> => {
    // Convert id → _id for NeDB compatibility
    const { id, ...rest } = mafiot;
    const payload = { ...rest, _id: id };
    
    return withLogs<MafiotProps>(
        axios.put(`${mafiotUrl}/${id}`, payload, getConfig()),
        'updateMafiot'
    );
};

export const deleteMafiot = (id: string): Promise<void> =>
    withLogs<void>(
        axios.delete(`${mafiotUrl}/${id}`, getConfig()),
        'deleteMafiot'
    );

export const getPhoto = async (mafiotId: string): Promise<string | null> => {
    try {
        log('getPhoto - started', mafiotId);
        const response = await axios.get(`${mafiotUrl}/${mafiotId}/photo`, getConfig());
        log('getPhoto - succeeded');
        return response.data.photo;
    } catch (err) {
        log('getPhoto - failed', err);
        return null;
    }
};

export interface MessageData {
    event: string;
    payload: {
        mafiot?: MafiotProps;
        mafiots?: MafiotProps[];
    };
}

export const newWebSocket = (token: string, onMessage: (data: MessageData) => void) => {
    let ws: WebSocket | null = null;
    let reconnectTimer: ReturnType<typeof setTimeout> | null = null;
    let isClosed = false;
    let reconnectAttempts = 0;
    const maxReconnectDelay = 10000;
    let isAuthorized = false;

    const connect = () => {
        if (isClosed) {
            log('web socket - not connecting (closed)');
            return;
        }

        log(`web socket - connecting (attempt ${reconnectAttempts + 1})`);
        ws = new WebSocket(`ws://${baseUrl}`);

        ws.onopen = () => {
            log('web socket onopen - sending authorization');
            reconnectAttempts = 0;
            isAuthorized = false;
            
            // Send authorization message
            if (ws) {
                ws.send(JSON.stringify({
                    type: 'authorization',
                    payload: { token }
                }));
            }
            
            if (reconnectTimer) {
                clearTimeout(reconnectTimer);
                reconnectTimer = null;
            }
        };

        ws.onclose = () => {
            log('web socket onclose');
            ws = null;
            isAuthorized = false;

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
                
                // Handle authorization response
                if (raw.event === 'authorized') {
                    log('web socket - authorized');
                    isAuthorized = true;
                    return;
                }
                
                if (!isAuthorized) {
                    log('web socket - message received before authorization');
                    return;
                }
                
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
