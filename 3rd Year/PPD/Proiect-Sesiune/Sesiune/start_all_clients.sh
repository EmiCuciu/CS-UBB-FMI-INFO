#!/bin/bash


echo "════════════════════════════════════════════════════════"
echo "  Starting 10 Clients"
echo "════════════════════════════════════════════════════════"
echo ""

# Compilare
echo "Compiling..."
./gradlew build -q

if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

echo "Starting 10 clients..."
echo ""

for i in {1..10}; do
    echo "Starting Client $i..."
    ./start_client.sh $i > "client_${i}.log" 2>&1 &
    sleep 0.5  # Delay mic între porniri
done

echo ""
echo "All 10 clients started!"
echo "Check individual logs: client_1.log, client_2.log, etc."
echo ""
echo "To stop all clients, run: pkill -f ClientMain"
