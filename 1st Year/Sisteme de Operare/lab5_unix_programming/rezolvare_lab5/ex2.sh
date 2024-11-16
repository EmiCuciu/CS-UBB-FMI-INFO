#!/bin/bash

if [ $# -eq 0 ]
then
    echo 'Numar insuficient de argumente.'
    echo 'Utilizare:' $0 'nume_director'                # afișez modul de utilizare
    exit 1                                              # termin execuția
fi

DIR=$1

for FILE in $(find $DIR -type f | sort)
do
    if file $FILE | grep -q 'ASCII text$'               # dacă fișierul e de tip text
    then
        echo -n "Fisier: "; echo $FILE | sed 's@.*/@@'	# extrag doar numele fișierului
        NL=$(cat $FILE | wc -l)                         # număr câte linii conține
        if [ $NL -lt 6 ]                                # dacă are mai puțin de 6 linii
        then
            cat -n $F                                   # îl afișez în întregime
        else
            echo "_____Head_____"
            head -n 3 $F                                # afișez primele 3 linii
            echo "_____Tail_____"
            tail -n 3 $F                                # afișez ultimele 3 linii
        fi
    fi
done

exit 0
