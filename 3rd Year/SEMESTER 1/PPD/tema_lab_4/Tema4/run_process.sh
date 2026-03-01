#!/bin/bash

# Argumente:
# 1: Scriptul de executat (run_java.sh)
# 2: Numarul de repetari (ex: 10)
# 3: Numarul total de thread-uri p (ex: 4)
# 4: Numarul de readeri p_r (ex: 2)
# 5: Fisierul CSV de iesire

SCRIPT_PATH=$1
RETRIES=$2
P=$3
P_R=$4
FILE_OUTPUT=$5

# Verificam daca avem 5 argumente
if [ $# -ne 5 ]; then
    echo "Usage: ./run_process.sh <script_path> <retries> <p> <p_r> <output_file>"
    exit 1
fi

INDEX=1

# Gestionare Header CSV
if [ -f "$FILE_OUTPUT" ]; then
    echo "File $FILE_OUTPUT exists. Appending..."
    LAST_LINE=$(tail -n1 "$FILE_OUTPUT")
    LAST_INDEX=$(echo "$LAST_LINE" | cut -d"," -f1)
    # Verificam daca ultimul index e numar valid
    if [[ "$LAST_INDEX" =~ ^[0-9]+$ ]]; then
        INDEX=$((LAST_INDEX+1))
    fi
else
    echo "Creating new file $FILE_OUTPUT..."
    # Header actualizat cu coloana speedup
    echo "trial_no,p,p_r,average_time,speedup" > "$FILE_OUTPUT"
fi

total_sum=0

echo "Benchmarking P=$P, P_R=$P_R..."

for i in $(seq 1 $RETRIES); do
    # Transmitem ambii parametri catre scriptul Java ($P si $P_R)
    # Output-ul standard este capturat in variabila result
    result=$(./"$SCRIPT_PATH" "$P" "$P_R")

    # Verificare eroare (daca Java a dat System.exit(1) sau a crapat si a scos text)
    if ! [[ $result =~ ^[0-9]+$ ]]; then
        echo "Eroare la rularea $i: '$result'"
        exit 1
    fi

    # Adunam folosind bc pentru precizie
    total_sum=$(echo "$total_sum + $result" | bc -l)
done

# Calculam media cu 2 zecimale
average=$(echo "scale=2; $total_sum / $RETRIES" | bc -l)

T_SEQ=11

if (( $(echo "$average > 0" | bc -l) )); then
    speedup=$(echo "scale=2; $T_SEQ / $average" | bc -l)
else
    speedup="0"
fi

echo "Media Paralel: $average ms"
echo "Speedup (fata de $T_SEQ ms): ${speedup}x"

echo "$INDEX,$P,$P_R,$average,$speedup" >> "$FILE_OUTPUT"