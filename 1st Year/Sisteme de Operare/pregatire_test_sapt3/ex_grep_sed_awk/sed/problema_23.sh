#!/bin/bash

#sed '1 i DATE DESPRE PERSONAL\n' angajati.txt

sed '/49$/d' angajati.txt

#sed -E 's/([^ ]+) ([^ ]+)/\2 \1/' 

#sed '$ a Terminat' 
