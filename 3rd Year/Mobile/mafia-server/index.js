const Koa = require('koa');
const app = new Koa();
const http = require('http');
const server = http.createServer(app.callback());
const WebSocket = require('ws');
const Router = require('koa-router');
const cors = require('koa-cors');
const bodyParser = require('koa-bodyparser');
const fs = require('fs').promises;
const path = require('path');

const router = new Router();
const mafiotsFile = path.join(__dirname, 'db', 'mafiots.json');

// --- Helpers: read / write ---
async function readMafiots() {
    try {
        const raw = await fs.readFile(mafiotsFile, 'utf8');
        const json = JSON.parse(raw);
        return Array.isArray(json.mafiot) ? json.mafiot : [];
    } catch (err) {
        console.error('readMafiots error:', err);
        return [];
    }
}

async function writeMafiots(list) {
    const payload = { mafiot: list };
    await fs.writeFile(mafiotsFile, JSON.stringify(payload, null, 2), 'utf8');
}

function generateId() {
    return Date.now().toString();
}

// --- Middleware ---
app.use(cors());
app.use(bodyParser());

// --- REST Routes ---

// GET all
router.get('/mafiot', async (ctx) => {
    const list = await readMafiots();
    ctx.status = 200;
    ctx.body = list;
});

// GET one
router.get('/mafiot/:id', async (ctx) => {
    const id = ctx.params.id;
    const list = await readMafiots();
    const item = list.find((x) => String(x.id) === String(id));
    if (!item) {
        ctx.status = 404;
        ctx.body = { message: `Mafiot with id ${id} not found` };
        return;
    }
    ctx.status = 200;
    ctx.body = item;
});

// POST
router.post('/mafiot', async (ctx) => {
    const body = ctx.request.body || {};
    const { nume, prenume, balanta } = body;

    if (!nume || !prenume) {
        ctx.status = 400;
        ctx.body = { message: 'nume and prenume are required' };
        return;
    }

    const list = await readMafiots();
    const newItem = {
        id: body.id || generateId(),
        nume,
        prenume,
        balanta: balanta != null ? String(balanta) : '0.00',
    };
    list.push(newItem);

    try {
        await writeMafiots(list);
        ctx.status = 201;
        ctx.body = newItem;
        broadcastEvent({ event: 'list', payload: { mafiots: list } });
    } catch (err) {
        console.error('POST error:', err);
        ctx.status = 500;
        ctx.body = { message: 'Failed to save mafiot' };
    }
});

// PUT
router.put('/mafiot/:id', async (ctx) => {
    const id = ctx.params.id;
    const body = ctx.request.body || {};
    const list = await readMafiots();
    const idx = list.findIndex((x) => String(x.id) === String(id));

    if (idx === -1) {
        ctx.status = 404;
        ctx.body = { message: `Mafiot with id ${id} not found` };
        return;
    }

    const updated = {
        ...list[idx],
        nume: body.nume ?? list[idx].nume,
        prenume: body.prenume ?? list[idx].prenume,
        balanta: body.balanta != null ? String(body.balanta) : list[idx].balanta,
    };

    list[idx] = updated;

    try {
        await writeMafiots(list);
        ctx.status = 200;
        ctx.body = updated;
        broadcastEvent({ event: 'list', payload: { mafiots: list } });
    } catch (err) {
        console.error('PUT error:', err);
        ctx.status = 500;
        ctx.body = { message: 'Failed to update mafiot' };
    }
});

// DELETE
router.delete('/mafiot/:id', async (ctx) => {
    const id = ctx.params.id;
    const list = await readMafiots();
    const idx = list.findIndex((x) => String(x.id) === String(id));

    if (idx === -1) {
        ctx.status = 404;
        ctx.body = { message: `Mafiot with id ${id} not found` };
        return;
    }

    const removed = list.splice(idx, 1)[0];

    try {
        await writeMafiots(list);
        ctx.status = 200;
        ctx.body = removed;
        broadcastEvent({ event: 'list', payload: { mafiots: list } });
    } catch (err) {
        console.error('DELETE error:', err);
        ctx.status = 500;
        ctx.body = { message: 'Failed to delete mafiot' };
    }
});

app.use(router.routes());
app.use(router.allowedMethods());

// --- WebSocket setup ---
const wss = new WebSocket.Server({ server });

function broadcastEvent(data, excludeWs = null) {
    const message = JSON.stringify(data);
    wss.clients.forEach((client) => {
        if (client.readyState === WebSocket.OPEN && client !== excludeWs) {
            client.send(message);
        }
    });
}

wss.on('connection', async (ws) => {
    console.log('New WebSocket connection established');
    ws.send(JSON.stringify({ event: 'connected', payload: {} }));

    // send lista curenta la conectare
    const list = await readMafiots();
    ws.send(JSON.stringify({ event: 'list', payload: { mafiots: list } }));

    ws.on('close', () => console.log('WebSocket connection closed'));
    ws.on('error', (err) => console.error('WebSocket error:', err));
});

const PORT = process.env.PORT || 3000;
server.listen(PORT, () => {
    console.log(` Server running on http://localhost:${PORT}`);
    console.log(` WebSocket active on ws://localhost:${PORT}`);
});
