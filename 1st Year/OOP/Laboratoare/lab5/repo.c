#include "repo.h"
#include <stdlib.h>

vectMaterieP *creazaVect() {
    vectMaterieP *rez = malloc(sizeof(vectMaterieP));
    rez->cpm = 1;
    rez->vector = malloc(sizeof(materiePrima *) * rez->cpm);
    rez->index = 0;
    return rez;
}

void eliberareVectMaterieP(vectMaterieP *p) {
    for (int i = 0; i < p->index; i++) {
        stergereMaterie(p->vector[i]);
    }
    p->index = 0;
    free(p->vector);
    free(p);
}

ElemType getElem(vectMaterieP *p, int poz) {
    return p->vector[poz];
}

int size(vectMaterieP *p) {
    return p->index;
}

void addElem(vectMaterieP *p, ElemType el) {
    if (p->cpm <= p->index) {
        ElemType *x = malloc((p->cpm + 100) * sizeof(ElemType));
        for (int i = 0; i < p->index; i++)
            x[i] = p->vector[i];
        free(p->vector);
        p->vector = x;
        p->cpm *= 2;
    }
    p->vector[p->index] = el;
    p->index++;
}

vectMaterieP *copyVect(vectMaterieP *p) {
    vectMaterieP *rez = creazaVect();
    for (int i = 0; i < size(p); i++) {
        ElemType el = getElem(p, i);
        addElem(rez, copieMateriePrima(el));
    }
    return rez;
}

