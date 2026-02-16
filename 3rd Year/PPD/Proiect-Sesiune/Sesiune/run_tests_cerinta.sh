#!/bin/bash

echo "Parametri:"
echo "  - Nr_locuri: 100"
echo "  - Nr_clienți: 10"
echo "  - Nr_spectacole: 3 (S1, S2, S3)"
echo "  - Prețuri: S1=100 RON, S2=200 RON, S3=150 RON"
echo "  - T_max (rezervare): 10s"
echo "  - Server runtime: 180s (3 minute)"
echo ""
echo "════════════════════════════════════════════════════════"
echo ""

# Compilare
echo "Building..."
./gradlew build -q

if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

mkdir -p test_results

# ============================================================
# TEST 1: Verificare la 5 secunde
# ============================================================

echo ""
echo "  TEST 1: 10 clienți, verificare 5s"
echo ""

rm -f shows.db verification_5s.txt performance_metrics_5s.csv

# Pornește serverul cu verificare 5s
echo "Starting server with 5s verification..."
java -cp build/libs/Sesiune-1.0-SNAPSHOT.jar org.example.Server.ServerMain 5 > test_results/test_5s_server.log 2>&1 &
SERVER_PID=$!

sleep 3

if ! ps -p $SERVER_PID > /dev/null; then
    echo "Server failed to start! Check test_results/test_5s_server.log"
    cat test_results/test_5s_server.log
    exit 1
fi

echo "Server started with PID: $SERVER_PID"

# Pornește 10 clienți
echo "Starting 10 clients..."
for i in {1..10}; do
    java -cp build/libs/Sesiune-1.0-SNAPSHOT.jar org.example.Client.ClientMain $i > test_results/test_5s_client_${i}.log 2>&1 &
    CLIENT_PIDS_5S[$i]=$!
    sleep 0.2
done

echo "All 10 clients started!"
echo "Test running for 180 seconds (3 minutes)..."
echo ""

# Așteaptă serverul (180s)
wait $SERVER_PID

echo "Test 1 completed!"

# Oprește clienții rămaşi
for pid in "${CLIENT_PIDS_5S[@]}"; do
    if ps -p $pid > /dev/null 2>&1; then
        kill $pid 2>/dev/null
    fi
done

# Mută rezultatele
mv -f verification_5s.txt test_results/ 2>/dev/null
mv -f performance_metrics_5s.csv test_results/ 2>/dev/null

echo ""
echo "  TEST 1 RESULTS (5s verification)"

if [ -f "test_results/verification_5s.txt" ]; then
    NUM_VERIFICATIONS=$(wc -l < test_results/verification_5s.txt)
    NUM_CORRECT=$(grep -c "CORECT" test_results/verification_5s.txt)
    echo "Verificări: $NUM_VERIFICATIONS (așteptate: ~36 pentru 180s/5s)"
    echo "Status: $NUM_CORRECT CORECTE"
fi

if [ -f "test_results/performance_metrics_5s.csv" ]; then
    echo ""
    echo "Performance Metrics:"
    tail -n 1 test_results/performance_metrics_5s.csv | while IFS=',' read time avg_time throughput success total succ fail; do
        echo "  - Avg Response Time: ${avg_time} ms"
        echo "  - Throughput: ${throughput} req/s"
        echo "  - Success Rate: ${success}%"
        echo "  - Total Requests: ${total}"
        echo "  - Successful: ${succ}"
        echo "  - Failed: ${fail}"
    done
fi


# Pauză între teste
sleep 5

# ============================================================
# TEST 2: Verificare la 10 secunde
# ============================================================

echo ""
echo "  TEST 2: 10 clienți, verificare 10s"
echo ""

rm -f shows.db verification_10s.txt performance_metrics_10s.csv

# Pornește serverul cu verificare 10s
echo "Starting server with 10s verification..."
java -cp build/libs/Sesiune-1.0-SNAPSHOT.jar org.example.Server.ServerMain 10 > test_results/test_10s_server.log 2>&1 &
SERVER_PID=$!

sleep 3

if ! ps -p $SERVER_PID > /dev/null; then
    echo "Server failed to start! Check test_results/test_10s_server.log"
    cat test_results/test_10s_server.log
    exit 1
fi

echo "Server started with PID: $SERVER_PID"

# Pornește 10 clienți
echo "Starting 10 clients..."
for i in {1..10}; do
    java -cp build/libs/Sesiune-1.0-SNAPSHOT.jar org.example.Client.ClientMain $i > test_results/test_10s_client_${i}.log 2>&1 &
    CLIENT_PIDS_10S[$i]=$!
    sleep 0.2
done

echo "All 10 clients started!"
echo "Test running for 180 seconds (3 minutes)..."
echo ""

# Așteaptă serverul (180s)
wait $SERVER_PID

echo "Test 2 completed!"

# Oprește clienții rămaşi
for pid in "${CLIENT_PIDS_10S[@]}"; do
    if ps -p $pid > /dev/null 2>&1; then
        kill $pid 2>/dev/null
    fi
done

# Mută rezultatele
mv -f verification_10s.txt test_results/ 2>/dev/null
mv -f performance_metrics_10s.csv test_results/ 2>/dev/null

echo ""
echo "  TEST 2 RESULTS (10s verification)"

if [ -f "test_results/verification_10s.txt" ]; then
    NUM_VERIFICATIONS=$(wc -l < test_results/verification_10s.txt)
    NUM_CORRECT=$(grep -c "CORECT" test_results/verification_10s.txt)
    echo "Verificări: $NUM_VERIFICATIONS (așteptate: ~18 pentru 180s/10s)"
    echo "Status: $NUM_CORRECT CORECTE"
fi

if [ -f "test_results/performance_metrics_10s.csv" ]; then
    echo ""
    echo "Performance Metrics:"
    tail -n 1 test_results/performance_metrics_10s.csv | while IFS=',' read time avg_time throughput success total succ fail; do
        echo "  - Avg Response Time: ${avg_time} ms"
        echo "  - Throughput: ${throughput} req/s"
        echo "  - Success Rate: ${success}%"
        echo "  - Total Requests: ${total}"
        echo "  - Successful: ${succ}"
        echo "  - Failed: ${fail}"
    done
fi

echo "════════════════════════════════════════════════════════"

# ============================================================
# SUMAR FINAL
# ============================================================


echo "Comparație 5s vs 10s:"
echo ""

# Comparație side-by-side
printf "%-25s | %-20s | %-20s\n" "Metric" "5s Verification" "10s Verification"
echo "──────────────────────────────────────────────────────────────────────"

if [ -f "test_results/performance_metrics_5s.csv" ] && [ -f "test_results/performance_metrics_10s.csv" ]; then
    METRICS_5S=$(tail -n 1 test_results/performance_metrics_5s.csv)
    METRICS_10S=$(tail -n 1 test_results/performance_metrics_10s.csv)

    AVG_TIME_5S=$(echo "$METRICS_5S" | cut -d',' -f2)
    AVG_TIME_10S=$(echo "$METRICS_10S" | cut -d',' -f2)
    printf "%-25s | %-20s | %-20s\n" "Avg Response Time (ms)" "$AVG_TIME_5S" "$AVG_TIME_10S"

    THROUGHPUT_5S=$(echo "$METRICS_5S" | cut -d',' -f3)
    THROUGHPUT_10S=$(echo "$METRICS_10S" | cut -d',' -f3)
    printf "%-25s | %-20s | %-20s\n" "Throughput (req/s)" "$THROUGHPUT_5S" "$THROUGHPUT_10S"

    SUCCESS_5S=$(echo "$METRICS_5S" | cut -d',' -f4)
    SUCCESS_10S=$(echo "$METRICS_10S" | cut -d',' -f4)
    printf "%-25s | %-20s | %-20s\n" "Success Rate (%)" "$SUCCESS_5S" "$SUCCESS_10S"

    TOTAL_5S=$(echo "$METRICS_5S" | cut -d',' -f5)
    TOTAL_10S=$(echo "$METRICS_10S" | cut -d',' -f5)
    printf "%-25s | %-20s | %-20s\n" "Total Requests" "$TOTAL_5S" "$TOTAL_10S"
fi

echo ""
echo "════════════════════════════════════════════════════════"
echo ""
