#include "tranzactie.h"
#include <string.h>
#include <stdlib.h>

/*
 * Creeaza o tranzactie cu datele primite ca parametrii si o returneaza
 * @param zi - int
 * @param suma - int
 * @param tip - char*
 * @param descriere - char*
 * @return Tranzactie
 * preconditii: zi, suma - int, tip, descriere - char*
 * postconditii: se returneaza o tranzactie cu datele primite ca parametrii
 */
Tranzactie *creeazaTranzactie(int zi, float suma, char *tip, char *descriere) {
    Tranzactie *tranzactie = (Tranzactie *) malloc(sizeof(Tranzactie));
    tranzactie->zi = zi;
    tranzactie->suma = suma;
    tranzactie->tip = (char *) malloc((strlen(tip) + 1) * sizeof(char));
    strcpy(tranzactie->tip, tip);
    tranzactie->descriere = (char *) malloc((strlen(descriere) + 1) * sizeof(char));
    strcpy(tranzactie->descriere, descriere);
    return tranzactie;
}

/*
 * Distruge o tranzactie
 * @param tranzactie - Tranzactie*
 * preconditii: tranzactie - Tranzactie*
 * postconditii: tranzactia este distrusa
 */
void distrugeTranzactie(Tranzactie *tranzactie) {
    free(tranzactie->tip);
    free(tranzactie->descriere);
    free(tranzactie);
}

/*
 * Returneaza ziua unei tranzactii
 * @param tranzactie - Tranzactie*
 * @return int
 * preconditii: tranzactie - Tranzactie*
 * postconditii: se returneaza ziua tranzactiei
 */
int get_zi(Tranzactie *tranzactie) {
    return tranzactie->zi;
}

/*
 * Returneaza suma unei tranzactii
 * @param tranzactie - Tranzactie*
 * @return float
 * preconditii: tranzactie - Tranzactie*
 * postconditii: se returneaza suma tranzactiei
 */
float get_suma(Tranzactie *tranzactie) {
    return tranzactie->suma;
}

/*
 * Returneaza tipul unei tranzactii
 * @param tranzactie - Tranzactie*
 * @return char*
 * preconditii: tranzactie - Tranzactie*
 * postconditii: se returneaza tipul tranzactiei
 */
char *get_tip(Tranzactie *tranzactie) {
    return tranzactie->tip;
}

/*
 * Returneaza descrierea unei tranzactii
 * @param tranzactie - Tranzactie*
 * @return char*
 * preconditii: tranzactie - Tranzactie*
 * postconditii: se returneaza descrierea tranzactiei
 */
char *get_descriere(Tranzactie *tranzactie) {
    return tranzactie->descriere;
}

/*
 * Seteaza ziua unei tranzactii
 * @param tranzactie - Tranzactie*
 * @param zi - int
 * preconditii: tranzactie - Tranzactie*, zi - int
 * postconditii: ziua tranzactiei este setata
 */
void set_zi(Tranzactie *tranzactie, int zi) {
    tranzactie->zi = zi;
}

/*
 * Seteaza suma unei tranzactii
 * @param tranzactie - Tranzactie*
 * @param suma - float
 * preconditii: tranzactie - Tranzactie*, suma - float
 * postconditii: suma tranzactiei este setata
 */
void set_suma(Tranzactie *tranzactie, float suma) {
    tranzactie->suma = suma;
}

/*
 * Seteaza tipul unei tranzactii
 * @param tranzactie - Tranzactie*
 * @param tip - char*
 * preconditii: tranzactie - Tranzactie*, tip - char*
 * postconditii: tipul tranzactiei este setat
 */
void set_tip(Tranzactie *tranzactie, char *tip) {
    free(tranzactie->tip);
    tranzactie->tip = (char *) malloc((strlen(tip) + 1) * sizeof(char));
    strcpy(tranzactie->tip, tip);
}

/*
 * Seteaza descrierea unei tranzactii
 * @param tranzactie - Tranzactie*
 * @param descriere - char*
 * preconditii: tranzactie - Tranzactie*, descriere - char*
 * postconditii: descrierea tranzactiei este setata
 */
void set_descriere(Tranzactie *tranzactie, char *descriere) {
    free(tranzactie->descriere);
    tranzactie->descriere = (char *) malloc((strlen(descriere) + 1) * sizeof(char));
    strcpy(tranzactie->descriere, descriere);
}
