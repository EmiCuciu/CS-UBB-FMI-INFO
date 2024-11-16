#!/bin/bash

DIR="dir"

find "$DIR" -type f -perm -o=w | while read FILE;do
	ls -l "$FILE"

	chmod o-w "$FILE"

	ls -l "$FILE"
done
