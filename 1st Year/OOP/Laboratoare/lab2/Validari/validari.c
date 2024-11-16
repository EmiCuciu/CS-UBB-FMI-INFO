#include "validari.h"
#include <string.h>


int validareTranzactie(int zi, float suma, char *tip, char *descriere) {
    if(zi < 1 || zi > 31) {
        return 0;
    }
    if(suma < 0) {
        return 0;
    }
    if(strcmp(tip, "intrare") != 0 && strcmp(tip, "iesire") != 0) {
        return 0;
    }
    if(strlen(descriere) < 3) {
        return 0;
    }
    return 1;
}