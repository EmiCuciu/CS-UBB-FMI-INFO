const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const jwt = require('jsonwebtoken');
const fs = require('fs');
const path = require('path');
const http = require('http');
const WebSocket = require('ws');

const app = express();
const server = http.createServer(app);
const wss = new WebSocket.Server({ server });

const PORT = 3000;
const SECRET_KEY = 'godfather-secret-key-2024';

// Store connected clients by username
const clients = new Map();

// Middleware
app.use(cors());
app.use(bodyParser.json());

// JSON file paths
const USERS_DB = path.join(__dirname, 'db', 'users.json');
const MAFIOTS_DB = path.join(__dirname, 'db', 'mafiots.json');

// Ensure db directory exists
if (!fs.existsSync(path.join(__dirname, 'db'))) {
    fs.mkdirSync(path.join(__dirname, 'db'));
}

// Helper functions for file operations
function readUsersDB() {
    try {
        if (!fs.existsSync(USERS_DB)) {
            fs.writeFileSync(USERS_DB, JSON.stringify([]));
            return [];
        }
        const data = fs.readFileSync(USERS_DB, 'utf8');
        return JSON.parse(data);
    } catch (error) {
        console.error('Error reading users DB:', error);
        return [];
    }
}

function writeUsersDB(users) {
    try {
        fs.writeFileSync(USERS_DB, JSON.stringify(users, null, 2));
    } catch (error) {
        console.error('Error writing users DB:', error);
    }
}

function readMafiotsDB() {
    try {
        if (!fs.existsSync(MAFIOTS_DB)) {
            fs.writeFileSync(MAFIOTS_DB, JSON.stringify([]));
            return [];
        }
        const data = fs.readFileSync(MAFIOTS_DB, 'utf8');
        return JSON.parse(data);
    } catch (error) {
        console.error('Error reading mafiots DB:', error);
        return [];
    }
}

function writeMafiotsDB(mafiots) {
    try {
        fs.writeFileSync(MAFIOTS_DB, JSON.stringify(mafiots, null, 2));
    } catch (error) {
        console.error('Error writing mafiots DB:', error);
    }
}

function generateId() {
    return Math.random().toString(36).substr(2, 16);
}

// Helper function to generate JWT
function generateToken(username) {
    return jwt.sign({ username }, SECRET_KEY, { expiresIn: '24h' });
}

// Broadcast function to notify all clients of a user about changes
function broadcastToUser(username, message) {
    const userClients = clients.get(username);
    if (userClients) {
        userClients.forEach(ws => {
            if (ws.readyState === WebSocket.OPEN) {
                ws.send(JSON.stringify(message));
            }
        });
    }
}

// WebSocket connection handler
wss.on('connection', (ws) => {
    console.log('New WebSocket connection');

    ws.on('message', (message) => {
        try {
            const data = JSON.parse(message);

            if (data.type === 'auth') {
                // Authenticate WebSocket connection
                try {
                    const decoded = jwt.verify(data.token, SECRET_KEY);
                    ws.username = decoded.username;

                    // Add client to the map
                    if (!clients.has(ws.username)) {
                        clients.set(ws.username, new Set());
                    }
                    clients.get(ws.username).add(ws);

                    console.log(`WebSocket authenticated for user: ${ws.username}`);
                    ws.send(JSON.stringify({ type: 'auth', status: 'success' }));
                } catch (error) {
                    console.error('WebSocket auth failed:', error);
                    ws.send(JSON.stringify({ type: 'auth', status: 'error', message: 'Invalid token' }));
                    ws.close();
                }
            }
        } catch (error) {
            console.error('WebSocket message error:', error);
        }
    });

    ws.on('close', () => {
        if (ws.username) {
            const userClients = clients.get(ws.username);
            if (userClients) {
                userClients.delete(ws);
                if (userClients.size === 0) {
                    clients.delete(ws.username);
                }
            }
            console.log(`WebSocket closed for user: ${ws.username}`);
        }
    });

    ws.on('error', (error) => {
        console.error('WebSocket error:', error);
    });
});

// Middleware to verify JWT
function verifyToken(req, res, next) {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1]; // Bearer TOKEN

    if (!token) {
        return res.status(401).json({ error: 'Access denied. No token provided.' });
    }

    try {
        const decoded = jwt.verify(token, SECRET_KEY);
        req.user = decoded;
        next();
    } catch (error) {
        res.status(403).json({ error: 'Invalid token.' });
    }
}

// ============= AUTH ROUTES =============

// Register
app.post('/api/auth/register', (req, res) => {
    const { username, password } = req.body;

    if (!username || !password) {
        return res.status(400).json({ error: 'Username and password are required' });
    }

    const users = readUsersDB();

    if (users.find(u => u.username === username)) {
        return res.status(400).json({ error: 'User already exists' });
    }

    const newUser = {
        _id: generateId(),
        username,
        password
    };

    users.push(newUser);
    writeUsersDB(users);

    const token = generateToken(username);

    console.log(`User registered: ${username}`);
    res.status(201).json({ token });
});

// Login
app.post('/api/auth/login', (req, res) => {
    const { username, password } = req.body;

    if (!username || !password) {
        return res.status(400).json({ error: 'Username and password are required' });
    }

    const users = readUsersDB();
    const user = users.find(u => u.username === username && u.password === password);

    if (!user) {
        return res.status(401).json({ error: 'Invalid credentials' });
    }

    const token = generateToken(username);

    console.log(`User logged in: ${username}`);
    res.json({ token });
});

// ============= CHARACTER ROUTES (Protected) =============

// Get all characters for the logged-in user
app.get('/api/characters', verifyToken, (req, res) => {
    const username = req.user.username;
    const mafiots = readMafiotsDB();
    const userMafiots = mafiots.filter(m => m.userId === username);

    console.log(`Fetching characters for user: ${username}, count: ${userMafiots.length}`);
    res.json(userMafiots);
});

// Get character by ID
app.get('/api/characters/:id', verifyToken, (req, res) => {
    const { id } = req.params;
    const mafiots = readMafiotsDB();
    const mafiot = mafiots.find(m => m._id === id);

    if (!mafiot) {
        return res.status(404).json({ error: 'Character not found' });
    }

    if (mafiot.userId !== req.user.username) {
        return res.status(403).json({ error: 'Access denied' });
    }

    res.json(mafiot);
});

// Create character
app.post('/api/characters', verifyToken, (req, res) => {
    const { name, balance } = req.body;
    const username = req.user.username;

    if (!name) {
        return res.status(400).json({ error: 'Name is required' });
    }

    const mafiots = readMafiotsDB();
    const newMafiot = {
        _id: generateId(),
        name,
        balance: balance || 0,
        userId: username
    };

    mafiots.push(newMafiot);
    writeMafiotsDB(mafiots);

    console.log(`Character created: ${name} by ${username}`);

    // Broadcast to all connected clients of this user
    broadcastToUser(username, {
        type: 'character_created',
        character: newMafiot
    });

    res.status(201).json(newMafiot);
});

// Update character
app.put('/api/characters/:id', verifyToken, (req, res) => {
    const { id } = req.params;
    const { name, balance } = req.body;
    const username = req.user.username;

    const mafiots = readMafiotsDB();
    const index = mafiots.findIndex(m => m._id === id);

    if (index === -1) {
        return res.status(404).json({ error: 'Character not found' });
    }

    if (mafiots[index].userId !== username) {
        return res.status(403).json({ error: 'Access denied' });
    }

    mafiots[index] = {
        ...mafiots[index],
        name: name !== undefined ? name : mafiots[index].name,
        balance: balance !== undefined ? balance : mafiots[index].balance
    };

    writeMafiotsDB(mafiots);

    console.log(`Character updated: ${id} by ${username}`);

    // Broadcast to all connected clients of this user
    broadcastToUser(username, {
        type: 'character_updated',
        character: mafiots[index]
    });

    res.json(mafiots[index]);
});

// Delete character
app.delete('/api/characters/:id', verifyToken, (req, res) => {
    const { id } = req.params;
    const username = req.user.username;

    const mafiots = readMafiotsDB();
    const index = mafiots.findIndex(m => m._id === id);

    if (index === -1) {
        return res.status(404).json({ error: 'Character not found' });
    }

    if (mafiots[index].userId !== username) {
        return res.status(403).json({ error: 'Access denied' });
    }

    mafiots.splice(index, 1);
    writeMafiotsDB(mafiots);

    console.log(`Character deleted: ${id} by ${username}`);

    // Broadcast to all connected clients of this user
    broadcastToUser(username, {
        type: 'character_deleted',
        characterId: id
    });

    res.status(200).json({ message: 'Character deleted successfully' });
});

// Start server
server.listen(PORT, '0.0.0.0', () => {
    console.log(`\n🎬 Godfather Server running on:`);
    console.log(`   - Local: http://localhost:${PORT}`);
    console.log(`   - WiFi: http://10.131.0.219:${PORT}`);
    console.log(`   - Android Emulator: http://10.0.2.2:${PORT}`);
    console.log(`\n🔌 WebSocket Server running on:`);
    console.log(`   - Local: ws://localhost:${PORT}`);
    console.log(`   - WiFi: ws://10.131.0.219:${PORT}`);
    console.log(`\nAPI Endpoints:`);
    console.log(`  POST /api/auth/register - Register new user`);
    console.log(`  POST /api/auth/login - Login user`);
    console.log(`  GET  /api/characters - Get all characters`);
    console.log(`  POST /api/characters - Create character`);
    console.log(`  PUT  /api/characters/:id - Update character`);
    console.log(`  DELETE /api/characters/:id - Delete character\n`);
});

