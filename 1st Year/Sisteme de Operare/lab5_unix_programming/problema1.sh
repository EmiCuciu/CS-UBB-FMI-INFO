#!/bin/bash

names=$(awk '{print $1}' who.fake)

for name in $names

do
  eval=$(awk '{print $1}' ps.fake | grep -E "^$name" | wc -l)
  echo $name $eval
done
