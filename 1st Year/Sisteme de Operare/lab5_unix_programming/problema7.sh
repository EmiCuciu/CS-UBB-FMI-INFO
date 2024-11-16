#!/bin/bash

USER_FILE="user.txt"

EMAILS=""

while IFS= read -r USER; do
	EMAIL="$USER@scs.ubbcluj.ro"

	EMAILS+="$EMAIL,"
done < "$USER_FILE"

EMAILS=$(echo "$EMAILS" | sed 's/,$//')

echo "$EMAILS"

