#!/bin/bash

if [ $# -eq 0 ]
then
    echo 'Numar insuficient de argumente.'
    echo 'Utilizare:' $0 'numar_natural'    # afișez modul de utilizare
    exit 1                                  # termin execuția
fi


N=$1

X=1

while [ $X -le $N ];do
	touch "file_$X.txt"
	sed -n ''$X',+5p' passwd.fake > "file_$X.txt"
	X=$[X+1]
done
exit 0
