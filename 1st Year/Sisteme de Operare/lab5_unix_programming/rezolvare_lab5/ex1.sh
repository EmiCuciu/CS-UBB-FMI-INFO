#!/bin/bash

if [ $# -eq 0 ]; then
	echo "Numar de argumente insuficient"
	echo "Utilizare: " $0 "numar natural"
	exit 1
fi

N=$1

for X in $(seq 1 $N)
do
	touch "file_$X.txt"
	sed -n ''$X',+5p' passwd.fake > "file_$X.txt"
done
