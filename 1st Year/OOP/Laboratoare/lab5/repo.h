//
// Created by paaull on 13.03.2024.
//

#ifndef OOPLAB2_4_REPO_H
#define OOPLAB2_4_REPO_H

#include "domain.h"
#include <stdlib.h>

typedef void* ElemType;
typedef struct  {
    ElemType *vector;  // vector de pointeri la materii prime
    int index;  // nr de elemente din vector
    int cpm;    // capacitatea maxima a vectorului
} vectMaterieP;

vectMaterieP *creazaVect();

void eliberareVectMaterieP(vectMaterieP *p);

ElemType getElem(vectMaterieP *p, int poz);

int size(vectMaterieP *p);

void addElem(vectMaterieP *p, ElemType el);

vectMaterieP *copyVect(vectMaterieP *p);


#endif //OOPLAB2_4_REPO_H
