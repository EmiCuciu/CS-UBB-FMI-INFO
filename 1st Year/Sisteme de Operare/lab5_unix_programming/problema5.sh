#!/bin/bash

# Lista cu programele periculoase
DANGEROUS_PROGRAMS=("program1" "program2" "program3")

# Bucla infinită pentru monitorizarea proceselor
while true; do
    # Obținem lista proceselor din sistem
    PROCESSES=$(ps -eo comm=)

    # Parcurgem fiecare program periculos
    for PROGRAM in "${DANGEROUS_PROGRAMS[@]}"; do
        # Verificăm dacă programul periculos este în execuție
        if echo "$PROCESSES" | grep -q "$PROGRAM"; then
            # Obținem PID-urile proceselor care rulează programul periculos
            PIDS=$(pgrep "$PROGRAM")
            
            # Afișăm un mesaj de avertizare
            echo "Dangerous program $PROGRAM is running! Terminating..."

            # Oprim procesele
            kill $PIDS

            # Afișăm un mesaj de confirmare
            echo "Processes terminated."
        fi
    done

    # Pauză de 1 minut între verificări
    sleep 60
done

