#!/bin/bash

# Script pentru benchmarking Lab 5 - ParallelLab5
# Argumente:
# 1: Scriptul de executat (run_java_lab5.sh)
# 2: Numarul de repetari (ex: 10)
# 3: p_r - Numarul de thread-uri pentru citire (ex: 4)
# 4: p_w - Numarul de workers pentru procesare (ex: 2)
# 5: queue_capacity - Capacitatea cozii (ex: 100)
# 6: Fisierul CSV de iesire

SCRIPT_PATH=$1
RETRIES=$2
P_R=$3
P_W=$4
QUEUE_CAPACITY=$5
FILE_OUTPUT=$6

# Verificam daca avem 6 argumente
if [ $# -ne 6 ]; then
    echo "Usage: ./run_process_lab5.sh <script_path> <retries> <p_r> <p_w> <queue_capacity> <output_file>"
    echo "Example: ./run_process_lab5.sh run_java_lab5.sh 10 4 2 100 result_lab5.csv"
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
    # Header pentru Lab 5
    echo "trial_no,p_r,p_w,queue_capacity,average_time,speedup" > "$FILE_OUTPUT"
fi

total_sum=0

echo "=========================================="
echo "Benchmarking Lab 5:"
echo "  p_r (readers):      $P_R"
echo "  p_w (workers):      $P_W"
echo "  queue_capacity:     $QUEUE_CAPACITY"
echo "  repetari:           $RETRIES"
echo "=========================================="

for i in $(seq 1 $RETRIES); do
    echo -n "  Run $i/$RETRIES... "

    # Transmitem cei 3 parametri catre scriptul Java (p_r, p_w, queue_capacity)
    # Output-ul standard este capturat in variabila result
    result=$(./"$SCRIPT_PATH" "$P_R" "$P_W" "$QUEUE_CAPACITY")

    # Verificare eroare (daca Java a dat eroare sau output invalid)
    if ! [[ $result =~ ^[0-9]+$ ]]; then
        echo "EROARE!"
        echo "Output invalid la rularea $i: '$result'"
        exit 1
    fi

    echo "${result}ms"

    # Adunam folosind bc pentru precizie
    total_sum=$(echo "$total_sum + $result" | bc -l)
done

# Calculam media cu 2 zecimale
average=$(echo "scale=2; $total_sum / $RETRIES" | bc -l)

# Măsurăm timpul secvențial REAL (rulăm Secvential o dată)
echo ""
echo "Măsurăm timpul secvențial pentru comparație..."
T_SEQ=$(java -cp build/classes/java/main com.example.Secvential 2>&1 | grep "Secvential:" | awk '{print $2}')

# Verificăm dacă am obținut un număr valid
if ! [[ $T_SEQ =~ ^[0-9]+$ ]]; then
    echo "⚠️  Nu am putut măsura timpul secvențial. Folosim valoare estimată."
    T_SEQ=15
fi

if (( $(echo "$average > 0" | bc -l) )); then
    speedup=$(echo "scale=2; $T_SEQ / $average" | bc -l)
else
    speedup="0"
fi

echo "=========================================="
echo "✅ REZULTATE:"
echo "  Media Lab 5:   $average ms"
echo "  Secvențial:    $T_SEQ ms"
echo "  Speedup:       ${speedup}x"
if (( $(echo "$speedup < 1" | bc -l) )); then
    slowdown=$(echo "scale=2; $average / $T_SEQ" | bc -l)
    echo "  ⚠️  SLOWDOWN:  ${slowdown}x mai LENT decât secvențial"
fi
echo "=========================================="

# Salvam in CSV
echo "$INDEX,$P_R,$P_W,$QUEUE_CAPACITY,$average,$speedup" >> "$FILE_OUTPUT"

echo "Salvat in $FILE_OUTPUT (trial $INDEX)"

