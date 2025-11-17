import Router from 'koa-router';
import dataStore from 'nedb-promise';
import { broadcast } from './wss.js';
import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

export class MafiotStore {
    constructor({ filename, autoload }) {
        this.store = dataStore({ filename, autoload });
    }

    async find(props) {
        return this.store.find(props);
    }

    async findOne(props) {
        return this.store.findOne(props);
    }

    async insert(mafiot) {
        return this.store.insert(mafiot);
    }

    async update(props, mafiot) {
        return this.store.update(props, mafiot);
    }

    async remove(props) {
        return this.store.remove(props);
    }
}

const mafiotStore = new MafiotStore({ filename: './db/mafiots.json', autoload: true });

// Ensure photos directory exists
const PHOTOS_DIR = path.join(__dirname, 'poze-db');
if (!fs.existsSync(PHOTOS_DIR)) {
    fs.mkdirSync(PHOTOS_DIR, { recursive: true });
}

export const mafiotRouter = new Router();

// Helper function to normalize mafiot (map _id to id)
const normalizeMafiot = (mafiot) => {
    if (!mafiot) return null;
    const { _id, ...rest } = mafiot;
    return { ...rest, id: _id };
};

// GET all mafiots with pagination
mafiotRouter.get('/', async (ctx) => {
    const userId = ctx.state.user._id;
    const page = parseInt(ctx.query.page) || 1;
    const limit = parseInt(ctx.query.limit) || 10;
    const skip = (page - 1) * limit;

    try {
        // Get all mafiots for user
        const allMafiots = await mafiotStore.find({ userId });
        const total = allMafiots.length;

        // Apply pagination and normalize
        const mafiots = allMafiots.slice(skip, skip + limit).map(normalizeMafiot);

        ctx.response.body = {
            mafiots,
            page,
            limit,
            total,
            totalPages: Math.ceil(total / limit)
        };
        ctx.response.status = 200;
    } catch (err) {
        ctx.response.body = { error: err.message };
        ctx.response.status = 500;
    }
});

// GET one mafiot
mafiotRouter.get('/:id', async (ctx) => {
    const userId = ctx.state.user._id;
    const mafiotId = ctx.params.id;

    try {
        const mafiot = await mafiotStore.findOne({ _id: mafiotId, userId });
        if (mafiot) {
            ctx.response.body = normalizeMafiot(mafiot);
            ctx.response.status = 200;
        } else {
            ctx.response.body = { error: 'Mafiot not found' };
            ctx.response.status = 404;
        }
    } catch (err) {
        ctx.response.body = { error: err.message };
        ctx.response.status = 500;
    }
});

// POST create mafiot
mafiotRouter.post('/', async (ctx) => {
    const userId = ctx.state.user._id;
    const mafiot = ctx.request.body;

    try {
        // Separate photo from mafiot object
        const { photo, ...mafiotData } = mafiot;

        mafiotData.userId = userId;
        const newMafiot = await mafiotStore.insert(mafiotData);
        const normalized = normalizeMafiot(newMafiot);

        // Save photo separately if provided
        if (photo) {
            const photoFilename = `${normalized.id}.jpg`;
            const photoPath = path.join(PHOTOS_DIR, photoFilename);

            // Remove data URL prefix if present
            const base64Data = photo.replace(/^data:image\/\w+;base64,/, '');
            fs.writeFileSync(photoPath, base64Data, 'base64');

            // Update mafiot with photo filename
            await mafiotStore.update({ _id: newMafiot._id }, { $set: { photoPath: photoFilename } });
            normalized.photoPath = photoFilename;
        }

        ctx.response.body = normalized;
        ctx.response.status = 201;

        // Broadcast to user's connections
        broadcast(userId, { event: 'created', payload: { mafiot: normalized } });
    } catch (err) {
        ctx.response.body = { error: err.message };
        ctx.response.status = 400;
    }
});

// PUT update mafiot
mafiotRouter.put('/:id', async (ctx) => {
    const userId = ctx.state.user._id;
    const mafiotId = ctx.params.id;
    const mafiot = ctx.request.body;

    try {
        const existingMafiot = await mafiotStore.findOne({ _id: mafiotId, userId });
        if (!existingMafiot) {
            ctx.response.body = { error: 'Mafiot not found' };
            ctx.response.status = 404;
            return;
        }

        // Separate photo from mafiot object
        const { photo, ...mafiotData } = mafiot;

        await mafiotStore.update({ _id: mafiotId }, { $set: mafiotData });

        // Save photo separately if provided
        if (photo) {
            const photoFilename = `${mafiotId}.jpg`;
            const photoPath = path.join(PHOTOS_DIR, photoFilename);

            // Remove data URL prefix if present
            const base64Data = photo.replace(/^data:image\/\w+;base64,/, '');
            fs.writeFileSync(photoPath, base64Data, 'base64');

            // Update mafiot with photo filename
            await mafiotStore.update({ _id: mafiotId }, { $set: { photoPath: photoFilename } });
        }

        const updatedMafiot = await mafiotStore.findOne({ _id: mafiotId });
        const normalized = normalizeMafiot(updatedMafiot);
        ctx.response.body = normalized;
        ctx.response.status = 200;

        // Broadcast to user's connections
        broadcast(userId, { event: 'updated', payload: { mafiot: normalized } });
    } catch (err) {
        ctx.response.body = { error: err.message };
        ctx.response.status = 400;
    }
});

// DELETE mafiot
mafiotRouter.delete('/:id', async (ctx) => {
    const userId = ctx.state.user._id;
    const mafiotId = ctx.params.id;

    const mafiot = await mafiotStore.findOne({ _id: mafiotId });

    if (!mafiot) {
        ctx.status = 404;
        ctx.body = { error: 'Mafiot not found' };
        return;
    }

    if (mafiot.userId !== userId) {
        ctx.status = 403;
        ctx.body = { error: 'Forbidden' };
        return;
    }

    // Delete photo if exists
    if (mafiot.photoPath) {
        const photoPath = path.join(PHOTOS_DIR, mafiot.photoPath);
        if (fs.existsSync(photoPath)) {
            fs.unlinkSync(photoPath);
        }
    }

    await mafiotStore.remove({ _id: mafiotId });

    // Broadcast delete to user's connections
    broadcast(userId, { event: 'deleted', payload: { mafiot: normalizeMafiot(mafiot) } });

    ctx.status = 204;
});

// GET photo
mafiotRouter.get('/:id/photo', async (ctx) => {
    const userId = ctx.state.user._id;
    const mafiotId = ctx.params.id;

    try {
        const mafiot = await mafiotStore.findOne({ _id: mafiotId, userId });
        if (!mafiot) {
            ctx.response.body = { error: 'Mafiot not found' };
            ctx.response.status = 404;
            return;
        }

        if (!mafiot.photoPath) {
            ctx.response.body = { error: 'No photo available' };
            ctx.response.status = 404;
            return;
        }

        const photoPath = path.join(PHOTOS_DIR, mafiot.photoPath);
        if (!fs.existsSync(photoPath)) {
            ctx.response.body = { error: 'Photo file not found' };
            ctx.response.status = 404;
            return;
        }

        const photoData = fs.readFileSync(photoPath, 'base64');
        ctx.response.body = { photo: photoData };
        ctx.response.status = 200;
    } catch (err) {
        ctx.response.body = { error: err.message };
        ctx.response.status = 500;
    }
});

