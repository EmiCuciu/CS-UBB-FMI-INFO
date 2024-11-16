#!/bin/bash

DIR="dir"

find "$DIR" -type l | while read LINK;do
	if [ ! -e "$LINK" ];then
		echo "$LINK"
	fi
done
