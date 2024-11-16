#!/bin/bash

DIR="dir"

find "$DIR" -type f -name "*.log" | while read FILE;do
	sort -o "$FILE" "$FILE"

	echo "Sorted content of $FILE"
done
