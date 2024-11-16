#!/bin/bash

declare -A md5sums

find "." -type f | while read -r FILE;do
	md5=$(md5sum "$FILE" | awk '{print $1}')

	if [ "${md5sums[$md5]}" ];then
		echo "Duplicate: $FILE"
		echo "		: ${md5sums[$md5]}"
	else
		md5sums[$md5]="$FILE"
	fi
done
