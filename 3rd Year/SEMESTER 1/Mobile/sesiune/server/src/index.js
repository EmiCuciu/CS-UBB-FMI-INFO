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

app.use(async (ctx, next) => {
  const start = new Date();
  await next();
  const ms = new Date() - start;
  console.log(`${ctx.method} ${ctx.url} - ${ms}ms`);
});

let delayIndex = 1;
app.use(async (ctx, next) => {
  if (delayIndex < 10) {
    delayIndex += 2;
  } else {
    delayIndex = 1;
  }
  await new Promise(resolve => setTimeout(resolve, delayIndex * 1000));
  await next();
});

const inventoryItems = Array.from(Array(15).keys()).map(code => ({
  code,
  name: `Product ${code}`,
  quantity: Math.floor(Math.random() * 50) + 1
}));

wss.on('connection', async ws => {
  console.log('socket connection opened');
  await new Promise(resolve => setTimeout(resolve, 4000));
  ws.send(JSON.stringify(inventoryItems));
});

const router = new Router();

const audits = [];

router.post('/audit', ctx => {
  const { code, counted, zone } = ctx.request.body || {};
  
  const product = inventoryItems.find(it => it.code === code);

  if (typeof code !== 'number' || !product) {
    ctx.response.body = { text: 'Product code not found' };
    ctx.response.status = 404;
  } else if (typeof counted !== 'number' || counted < 0) {
    ctx.response.body = {
      code: product ? product.code : code,
      text: 'Counted quantity must be non-negative'
    };
    ctx.response.status = 400;
  } else if (!zone || typeof zone !== 'string') {
    ctx.response.body = { text: 'Zone is missing' };
    ctx.response.status = 400;
  } else {
    const auditRecord = { id: audits.length + 1, code, counted, zone, timestamp: new Date() };
    audits.push(auditRecord);
    ctx.response.body = auditRecord;
    ctx.response.status = 200;
  }
});

app.use(router.routes());
app.use(router.allowedMethods());

server.listen(3000, '0.0.0.0');
console.log("Inventory Server running on port 3000");
