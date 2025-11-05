import { WebSocket } from 'ws';
import jwt from 'jsonwebtoken';
import { jwtConfig } from './utils.js';

let wss;

export const initWss = (value) => {
    wss = value;
    wss.on('connection', (ws) => {
        console.log('New WebSocket connection - awaiting authorization');

        ws.on('message', (message) => {
            try {
                const { type, payload } = JSON.parse(message);

                if (type !== 'authorization') {
                    console.log('First message must be authorization');
                    ws.close();
                    return;
                }

                const { token } = payload;
                try {
                    ws.user = jwt.verify(token, jwtConfig.secret);
                    console.log(`WebSocket authorized for user: ${ws.user.username}`);
                    ws.send(JSON.stringify({ event: 'authorized', payload: {} }));
                } catch (err) {
                    console.log('Invalid token, closing connection');
                    ws.close();
                }
            } catch (err) {
                console.error('WebSocket message error:', err);
                ws.close();
            }
        });

        ws.on('close', () => {
            console.log('WebSocket connection closed');
        });

        ws.on('error', (err) => {
            console.error('WebSocket error:', err);
        });
    });
};

export const broadcast = (userId, data) => {
    if (!wss) {
        return;
    }

    wss.clients.forEach((client) => {
        if (client.readyState === WebSocket.OPEN && client.user && userId === client.user._id) {
            console.log(`Broadcast sent to ${client.user.username}`);
            client.send(JSON.stringify(data));
        }
    });
};
