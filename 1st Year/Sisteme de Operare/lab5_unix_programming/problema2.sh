#!/bin/bash

MAX_FILES=2
DIR="dir"

find "$DIR" -type f -name "*.c" | while read FILE;do
	LINE_COUNT=$(wc -l < "$FILE")

	if [ "$LINE_COUNT" -gt 500 ];then
		echo "$FILE has more 500 lines"

		MAX_FILES=$((MAX_FILES - 1))

		if [ "$MAX_FILES" -eq 0 ];then
			break
		fi
	fi
done
