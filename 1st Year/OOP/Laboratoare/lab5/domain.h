#include <stdio.h>
#include <string.h>

#ifndef OOPLAB2_4_DOMAIN_H
#define OOPLAB2_4_DOMAIN_H

typedef struct {
    char * nume;        //numele materiei prime
    char * producator;      //producatorul materiei prime
    int cantitate;    //cantitatea materiei prime
}materiePrima;

materiePrima* creazaMateriePrima(char * nume, char * producator, int cantitate);

void stergereMaterie(materiePrima *a);

materiePrima * copieMateriePrima(materiePrima * a);



#endif //OOPLAB2_4_DOMAIN_H
