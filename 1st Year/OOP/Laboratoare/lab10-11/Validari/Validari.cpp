//
// Created by Emi on 4/14/2024.
//
#include <stdexcept>
#include "Validari.h"

const int AnCurent = 2024;

bool Validator::valTitlu(const std::string &titlu){
    return isupper(titlu[0]);
}

bool Validator::valAn(int an) {
    return 0<=an && an<=AnCurent;
}

void Validator::valCarte(const Carte &carte) {
    std::string error;

    if(!valTitlu(carte.getTitlu())){error.append("\nTitlul nu e valid.\n");
    }
    if(!valAn(carte.getAn())){error.append("\nAnul nu e valid.\n");
    }

    if (!error.empty()){
//        throw Validator_exception(error);
    }
}