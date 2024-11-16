//
// Created by Emi on 3/23/2024.
//

#include "validari.h"
#include "string.h"

int validare(int cantitate, char *nume, char *tip) {
    if(cantitate <= 0) {
        return 0;
    }
    if(strlen(nume) < 3) {
        return 0;
    }
    if(strlen(tip) < 3){
        return 0;
    }
    return 1;
}
