#!/bin/bash

while true;do
	read X
	if [ $X == "stop" ] ; then
		echo S-a citit ~stop~
		break
	fi
done
