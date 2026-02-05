#!/bin/bash

# Script pentru pornirea unui client

if [ $# -lt 1 ]; then
    echo "Usage: ./start_client.sh <client_id>"
    echo "Example: ./start_client.sh 1"
    exit 1
fi

CLIENT_ID=$1

echo "════════════════════════════════════════════════════════"
echo "  Starting Client $CLIENT_ID"
echo "════════════════════════════════════════════════════════"
echo ""

# Pornește clientul
java -cp build/libs/Sesiune-1.0-SNAPSHOT.jar org.example.Client.ClientMain $CLIENT_ID
