#!/bin/bash

./gradlew build -q

if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

VERIFICATION_INTERVAL=${1:-5}

echo "Starting server with verification interval: ${VERIFICATION_INTERVAL}s"
echo ""

# Pornește serverul
java -cp build/libs/Sesiune-1.0-SNAPSHOT.jar:~/.gradle/caches/modules-2/files-2.1/org.xerial/sqlite-jdbc/3.45.1.0/7cd66c2793e4d9511f6da8fc6c621575171c2da1/sqlite-jdbc-3.45.1.0.jar \
    org.example.Server.ServerMain $VERIFICATION_INTERVAL
