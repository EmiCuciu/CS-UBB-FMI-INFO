#include "domain.h"
#include <stdlib.h>

materiePrima * creazaMateriePrima(char * nume, char * producator, int cantitate)
{
    materiePrima * materie = (materiePrima*)malloc(sizeof(materiePrima));
    materie->nume = (char*)malloc(sizeof(char) * (strlen(nume) + 1));
    strcpy(materie->nume, nume);
    materie->producator = (char*)malloc(sizeof(char) * (strlen(producator) + 1));
    strcpy(materie->producator, producator);
    materie->cantitate = cantitate;
    return materie;
}

void stergereMaterie(materiePrima * a)
{
    free(a->nume);
    free(a->producator);
    free(a);
}


materiePrima * copieMateriePrima(materiePrima * a)
{
    return creazaMateriePrima(a->nume, a->producator, a->cantitate);
}