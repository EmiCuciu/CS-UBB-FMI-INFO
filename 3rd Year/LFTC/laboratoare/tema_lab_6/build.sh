#!/bin/bash

# Curatare
rm -f lex.yy.c translator.tab.* translator *.o prog1 prog2 *.asm

echo ">>> 1. Compilare Translator (LEX+YACC)..."
flex scanner.l
bison -d translator.y
gcc lex.yy.c translator.tab.c -o translator -lfl

echo ">>> 2. Generare ASM pentru Prog 1 (Prioritati)..."
./translator prog1_suma.txt prog1.asm
nasm -f elf64 prog1.asm -o prog1.o
gcc -no-pie prog1.o -o prog1

echo ">>> 3. Generare ASM pentru Prog 2 (Paranteze)..."
./translator prog2_complex.txt prog2.asm
nasm -f elf64 prog2.asm -o prog2.o
gcc -no-pie prog2.o -o prog2

echo "---------------------------------------------------"
echo "VERIFICARE SEMANTICA (Cerinta 5)"
echo "---------------------------------------------------"

echo "[TEST 1] prog1_suma.txt: Calculam a + b * c"
echo "Intrare: a=2, b=3, c=4. Asteptat: 14"
echo "2 3 4" | ./prog1

echo ""
echo "[TEST 2] prog2_complex.txt: Calculam (a + b) * (c - 2)"
echo "Intrare: a=10, b=5, c=4. Asteptat: 30"
echo "10 5 4" | ./prog2