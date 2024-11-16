#!/bin/bash

FILE="df.fake"

awk 'NR>1 && (($2 < 1000000) || ($5 < 20)) {print $6}' "$FILE" | sed 's/^/plm /'
