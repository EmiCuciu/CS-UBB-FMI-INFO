const Koa = require('koa');
const app = new Koa();
const server = require('http').createServer(app.callback());
const WebSocket = require('ws');
const wss = new WebSocket.Server({server});
const Router = require('koa-router');
const cors = require('koa-cors');
const bodyparser = require('koa-bodyparser');

app.use(bodyparser());
app.use(cors());

app.use(async function (ctx, next) {
  const start = new Date();
  await next();
  const ms = new Date() - start;
  console.log(`${ctx.method} ${ctx.url} ${ctx.response.status} - ${ms}ms`);
});

// Data structures
function Product(code, name) {
  this.code = code;
  this.name = name;
}

function Item(code, quantity) {
  this.code = code;
  this.quantity = quantity;
}

// Initialize sample products
const products = [
  new Product(1, 'Laptop Dell XPS'),
  new Product(2, 'Mouse Logitech'),
  new Product(3, 'Keyboard Mechanical'),
  new Product(4, 'Monitor Samsung'),
  new Product(5, 'Headphones Sony'),
  new Product(6, 'Webcam Logitech'),
  new Product(7, 'Microphone Blue Yeti'),
  new Product(8, 'Router TP-Link'),
  new Product(9, 'Printer HP LaserJet'),
  new Product(10, 'Scanner Epson'),
  new Product(11, 'Tablet iPad'),
  new Product(12, 'Smartphone Samsung'),
  new Product(13, 'Charger USB-C'),
  new Product(14, 'Cable HDMI'),
  new Product(15, 'Adapter DisplayPort'),
  new Product(16, 'Mouse Pad Large'),
  new Product(17, 'Desk Lamp LED'),
  new Product(18, 'Chair Ergonomic'),
  new Product(19, 'Desk Standing'),
  new Product(20, 'Notebook A4'),
  new Product(21, 'Pen Ballpoint'),
  new Product(22, 'Pencil Mechanical'),
  new Product(23, 'Eraser White'),
  new Product(24, 'Ruler 30cm'),
  new Product(25, 'Calculator Scientific'),
];

const items = [];

// Add delay middleware
app.use(async (ctx, next) => {
  await new Promise(resolve => {
    setTimeout(resolve, 500); // 500ms delay for realistic network simulation
  });
  await next();
});

const router = new Router();

// GET /product?page=n - Get paginated products
router.get('/product', ctx => {
  const page = parseInt(ctx.query.page) || 0;
  const pageSize = 10;
  const total = products.length;
  const totalPages = Math.ceil(total / pageSize);

  if (page < 0 || page >= totalPages) {
    ctx.response.status = 404;
    ctx.response.body = { error: 'Page not found' };
    return;
  }

  const startIndex = page * pageSize;
  const endIndex = Math.min(startIndex + pageSize, total);
  const pageProducts = products.slice(startIndex, endIndex);

  ctx.response.status = 200;
  ctx.response.body = {
    total: total,
    page: page,
    products: pageProducts
  };
});

// POST /item - Add item to inventory
router.post('/item', ctx => {
  const itemData = ctx.request.body;

  if (!itemData || !itemData.code || !itemData.quantity) {
    ctx.response.status = 400;
    ctx.response.body = { error: 'Missing code or quantity' };
    return;
  }

  // Validate that product exists
  const product = products.find(p => p.code === itemData.code);
  if (!product) {
    ctx.response.status = 404;
    ctx.response.body = { error: 'Product not found' };
    return;
  }

  // Check if item already exists, update quantity
  const existingItem = items.find(i => i.code === itemData.code);
  if (existingItem) {
    existingItem.quantity += itemData.quantity;
  } else {
    items.push(new Item(itemData.code, itemData.quantity));
  }

  ctx.response.status = 201;
  ctx.response.body = { success: true, code: itemData.code, quantity: itemData.quantity };
  console.log(`Item added: code=${itemData.code}, quantity=${itemData.quantity}`);
});

// GET /item - Get all items (optional, for testing)
router.get('/item', ctx => {
  ctx.response.status = 200;
  ctx.response.body = items;
});

// WebSocket broadcast function
const broadcast = (data) => {
  wss.clients.forEach((client) => {
    if (client.readyState === WebSocket.OPEN) {
      client.send(JSON.stringify(data));
    }
  });
};

// WebSocket connection handler
wss.on('connection', (ws) => {
  console.log('Client connected via WebSocket');

  ws.on('message', (message) => {
    console.log('Received:', message);
  });

  ws.on('close', () => {
    console.log('Client disconnected');
  });
});

// Simulate product list changes every 30 seconds
setInterval(() => {
  console.log('Broadcasting product list change notification');
  broadcast({ type: 'PRODUCT_LIST_CHANGED', timestamp: new Date().toISOString() });
}, 30000);

// Manual trigger to add a new product (for testing)
// Uncomment to test WebSocket notifications
/*
setInterval(() => {
  const newCode = products.length + 1;
  const newProduct = new Product(newCode, `New Product ${newCode}`);
  products.push(newProduct);
  console.log(`Added new product: ${newProduct.name}`);
  broadcast({ type: 'PRODUCT_LIST_CHANGED', timestamp: new Date().toISOString() });
}, 60000);
*/

app.use(router.routes());
app.use(router.allowedMethods());

server.listen(3000, '0.0.0.0', () => {
  console.log('========================================');
  console.log('📦 Inventory Server Started');
  console.log('========================================');
  console.log('Server running on http://0.0.0.0:3000');
  console.log('WebSocket server running on ws://0.0.0.0:3000');
  console.log('');
  console.log('🌐 Access URLs:');
  console.log('- Local:            http://localhost:3000');
  console.log('- Android Emulator: http://10.0.2.2:3000');
  console.log('- Wi-Fi Device:     http://10.99.138.25:3000');
  console.log('- Ethernet/USB:     http://10.231.147.116:3000');
  console.log('- USB Tethering:    http://10.231.147.206:3000 (gateway)');
  console.log('');
  console.log('📡 WebSocket URLs:');
  console.log('- Android Emulator: ws://10.0.2.2:3000');
  console.log('- Wi-Fi Device:     ws://10.99.138.25:3000');
  console.log('- Ethernet/USB:     ws://10.231.147.116:3000');
  console.log('- USB Tethering:    ws://10.231.147.206:3000 (gateway)');
  console.log('========================================');
  console.log('Press Ctrl+C to stop the server');
  console.log('');
});

