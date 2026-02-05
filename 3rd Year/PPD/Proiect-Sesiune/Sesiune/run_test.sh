#!/bin/bash

# Script de testare cu măsurarea performanței
# Usage: ./run_test.sh <num_clients> <verification_interval> <test_name>

NUM_CLIENTS=${1:-10}
VERIFICATION_INTERVAL=${2:-5}
TEST_NAME=${3:-"test"}

RESULTS_DIR="test_results"
mkdir -p $RESULTS_DIR

echo "════════════════════════════════════════════════════════"
echo "  Performance Test Runner"
echo "════════════════════════════════════════════════════════"
echo "  Number of clients: $NUM_CLIENTS"
echo "  Verification interval: ${VERIFICATION_INTERVAL}s"
echo "  Test name: $TEST_NAME"
echo "════════════════════════════════════════════════════════"
echo ""

# Compilare
echo "Compiling..."
./gradlew build -q

if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

# Șterge baza de date veche
rm -f shows.db

# Pornește serverul în background
echo "Starting server..."
./start_server.sh $VERIFICATION_INTERVAL > "$RESULTS_DIR/${TEST_NAME}_server.log" 2>&1 &
SERVER_PID=$!

# Așteaptă ca serverul să pornească
sleep 3

# Verifică dacă serverul a pornit cu succes
if ! ps -p $SERVER_PID > /dev/null; then
    echo "Server failed to start! Check $RESULTS_DIR/${TEST_NAME}_server.log"
    exit 1
fi

echo "Server started with PID: $SERVER_PID"

# Pornește clienții
echo "Starting $NUM_CLIENTS clients..."
START_TIME=$(date +%s)

for i in $(seq 1 $NUM_CLIENTS); do
    ./start_client.sh $i > "$RESULTS_DIR/${TEST_NAME}_client_${i}.log" 2>&1 &
    CLIENT_PIDS[$i]=$!
    sleep 0.2  # Delay mic între porniri
done

echo "All $NUM_CLIENTS clients started!"
echo ""
echo "Test running... (will run for 3 minutes)"
echo "Press Ctrl+C to stop early"
echo ""

# Așteaptă ca serverul să se închidă (după 3 minute)
wait $SERVER_PID

END_TIME=$(date +%s)
DURATION=$((END_TIME - START_TIME))

echo ""
echo "════════════════════════════════════════════════════════"
echo "  Test Completed"
echo "════════════════════════════════════════════════════════"
echo "  Duration: ${DURATION}s"
echo "  Results saved to: $RESULTS_DIR/"
echo ""

# Oprește clienții rămaşi (dacă mai sunt activi)
echo "Stopping remaining clients..."
for pid in "${CLIENT_PIDS[@]}"; do
    if ps -p $pid > /dev/null 2>&1; then
        kill $pid 2>/dev/null
    fi
done

# Mută fișierele de rezultate
echo "Moving result files..."
mv -f verification_${VERIFICATION_INTERVAL}s.txt "$RESULTS_DIR/${TEST_NAME}_verification.txt" 2>/dev/null
mv -f performance_metrics_${VERIFICATION_INTERVAL}s.csv "$RESULTS_DIR/${TEST_NAME}_performance.csv" 2>/dev/null

# Generează raport sumar
echo ""
echo "════════════════════════════════════════════════════════"
echo "  Performance Summary"
echo "════════════════════════════════════════════════════════"

if [ -f "$RESULTS_DIR/${TEST_NAME}_performance.csv" ]; then
    echo "Performance Metrics:"
    tail -n 1 "$RESULTS_DIR/${TEST_NAME}_performance.csv" | while IFS=',' read timestamp avg_time throughput success_rate total success failed; do
        echo "  - Average Response Time: ${avg_time}ms"
        echo "  - Throughput: ${throughput} req/s"
        echo "  - Success Rate: ${success_rate}%"
        echo "  - Total Requests: ${total}"
        echo "  - Successful: ${success}"
        echo "  - Failed: ${failed}"
    done
fi

echo ""
echo "Verification Status:"
if [ -f "$RESULTS_DIR/${TEST_NAME}_verification.txt" ]; then
    LAST_VERIFICATION=$(tail -n 1 "$RESULTS_DIR/${TEST_NAME}_verification.txt")
    if echo "$LAST_VERIFICATION" | grep -q "CORECT"; then
        echo "  ✓ CORRECT - All consistency checks passed"
    else
        echo "  ✗ INCORRECT - Consistency issues detected"
    fi
else
    echo "  ? No verification file found"
fi

echo ""
echo "════════════════════════════════════════════════════════"
echo ""
echo "Full logs available in: $RESULTS_DIR/"
echo "  - Server: ${TEST_NAME}_server.log"
echo "  - Clients: ${TEST_NAME}_client_*.log"
echo "  - Verification: ${TEST_NAME}_verification.txt"
echo "  - Performance: ${TEST_NAME}_performance.csv"
echo ""

# Output pentru scriptul de benchmark
echo "$DURATION"
