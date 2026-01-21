const WebSocket = require('ws');
const http = require('http');

console.log("--- TEST STARTED ---");
console.log("1. Connecting to WebSocket (ws://localhost:3000)...");
const ws = new WebSocket('ws://localhost:3000');

ws.on('open', () => {
    console.log("   WebSocket connection established.");
    console.log("   Waiting for inventory data (Server has a ~4s delay)...");
});

ws.on('message', (data) => {
    try {
        const items = JSON.parse(data);
        console.log(`
2. WebSocket received data!`);
        console.log(`   Items received: ${items.length}`);
        console.log(`   First item: ${JSON.stringify(items[0])}`);
        
        ws.close(); // Close WS connection
        
        // Proceed to test HTTP POST
        if (items.length > 0) {
            testPostAudit(items[0].code);
        } else {
            console.error("❌ Error: Received empty inventory list.");
            process.exit(1);
        }
    } catch (e) {
        console.error("❌ Error parsing WS data:", e);
        process.exit(1);
    }
});

ws.on('error', (err) => {
    console.error("❌ WebSocket Error:", err.message);
    process.exit(1);
});

function testPostAudit(itemCode) {
    console.log(`
3. Testing HTTP POST /audit for item code: ${itemCode}`);
    console.log("   (Server has a random delay of 1-9s, please wait...)");

    const postData = JSON.stringify({
        code: itemCode,
        counted: 42,
        zone: "TestZone-Alpha"
    });

    const options = {
        hostname: 'localhost',
        port: 3000,
        path: '/audit',
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Content-Length': postData.length
        }
    };

    const req = http.request(options, (res) => {
        let responseBody = '';

        res.on('data', (chunk) => {
            responseBody += chunk;
        });

        res.on('end', () => {
            console.log(`
4. HTTP Response received.`);
            console.log(`   Status Code: ${res.statusCode}`);
            console.log(`   Body: ${responseBody}`);

            if (res.statusCode === 200) {
                const parsed = JSON.parse(responseBody);
                if (parsed.code === itemCode && parsed.counted === 42) {
                    console.log("\n✅ TEST PASSED: Server handles WS and POST correctly.");
                    process.exit(0);
                } else {
                    console.log("\n❌ TEST FAILED: Response data mismatch.");
                    process.exit(1);
                }
            } else {
                console.log("\n❌ TEST FAILED: Status code is not 200.");
                process.exit(1);
            }
        });
    });

    req.on('error', (e) => {
        console.error(`❌ HTTP Request Error: ${e.message}`);
        process.exit(1);
    });

    // Write data to request body
    req.write(postData);
    req.end();
}
