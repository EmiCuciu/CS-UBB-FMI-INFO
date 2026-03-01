#!/bin/bash

SCRIPT_PATH=$1
RETRIES=$2
THREADS=$3
FILE_OUTPUT=$4
INDEX=1

RESULTS=()

if [ -f "$FILE_OUTPUT" ]; then
    echo "$FILE_OUTPUT exists. Output will be appended"
    INDEX=$(tail -n1 $FILE_OUTPUT | cut -d"," -f1)
    INDEX=$((INDEX+1))
else
    echo "$FILE_OUTPUT does not exist. A new file will be created"
    echo "trial_no,average_time,threads" > $FILE_OUTPUT
fi

for i in $(seq 1 $RETRIES); do
    echo "Trial no:" $i "for" $SCRIPT_PATH "with" $THREADS "threads."

    # Folosește eval pentru a executa comanda completă
    result=$(eval "$SCRIPT_PATH $THREADS" 2>&1 | grep -E '^[0-9]+\.?[0-9]*$')

    if [ -n "$result" ]; then
        RESULTS+=($result)
    else
        echo "Warning: No valid numeric result for trial $i"
    fi
done

total=0
sum=0
for i in "${RESULTS[@]}"
do
    sum=$(echo "$sum + $i" | bc)
    total=$((total + 1))
done

if [ $total -gt 0 ]; then
    average=$(echo "scale=2; $sum / $total" | bc)
    echo "$INDEX,$average,$THREADS" >> $FILE_OUTPUT
else
    echo "Error: No valid results to average"
fi
