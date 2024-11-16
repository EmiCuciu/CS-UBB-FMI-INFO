#!/bin/bash

for X in `ps -ef | grep -E -v "^root" | tail -n +2 | awk '{print $1 ":" $2}' `; do
	U = `echo $X | cut -d: -f1`
	P = `echo $X | cut -d: -f2`

	echo $U $P
	if grep -E "^$U:" /etc/passwd | cut -d: -f6 | grep -E -q "/scs/"; then
 		A=`ps -o etime $P | tail -n 1 | awk -F: '{print ($1*60+$2)}'`
		if [ $A -ge $1 ]; then
 			echo "Should kill $U $P $A"
 		fi
 	fi
done
