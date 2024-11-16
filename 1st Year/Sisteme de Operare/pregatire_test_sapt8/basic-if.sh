#!/bin/bash

for A in $@;do
	if [ -f $A ] ; then
		echo $A is a file
	elif test -d $A
	then
		echo $A is a directory
	elif echo $A | grep -E -q "^[0-9]+$"; then
		echo $A is a number
	else
		echo dont know $A is
	fi
done
