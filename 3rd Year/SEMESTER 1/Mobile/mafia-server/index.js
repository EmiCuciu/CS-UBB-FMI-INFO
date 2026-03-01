import http from 'http';
import Koa from 'koa';
import { WebSocketServer } from 'ws';
import Router from 'koa-router';
import bodyParser from 'koa-bodyparser';
import jwt from 'koa-jwt';
import cors from '@koa/cors';
import { jwtConfig, timingLogger, exceptionHandler } from './utils.js';
import { initWss } from './wss.js';
import { mafiotRouter } from './mafiot.js';
import { authRouter } from './auth.js';

const app = new Koa();
const server = http.createServer(app.callback());
const wss = new WebSocketServer({ server });
initWss(wss);

app.use(cors());
app.use(timingLogger);
app.use(exceptionHandler);
app.use(bodyParser({
    jsonLimit: '50mb',  // Increase limit for photo uploads
    formLimit: '50mb'
}));

const prefix = '/api';

// Public routes
const publicApiRouter = new Router({ prefix });
publicApiRouter.use('/auth', authRouter.routes());
app
    .use(publicApiRouter.routes())
    .use(publicApiRouter.allowedMethods());

// JWT middleware for protected routes
app.use(jwt(jwtConfig));

// Protected routes
const protectedApiRouter = new Router({ prefix });
protectedApiRouter.use('/mafiot', mafiotRouter.routes());
app
    .use(protectedApiRouter.routes())
    .use(protectedApiRouter.allowedMethods());

const PORT = process.env.PORT || 3000;
server.listen(PORT, () => {
    console.log(`Server running on http://localhost:${PORT}`);
    console.log(`WebSocket active on ws://localhost:${PORT}`);
});
