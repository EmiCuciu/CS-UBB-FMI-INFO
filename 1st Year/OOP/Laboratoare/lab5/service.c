//
// Created by paaull on 18.03.2024.
//

#include "service.h"

int addMateriePrima(service *s, char *nume, char *producator, int cantitate) {
    materiePrima *m = creazaMateriePrima(nume, producator, cantitate);

    addElem(s->lista, m);

    addElem(s->undo, copyVect(s->lista));

    return 0;
}


vectMaterieP *deleteMateriePrima(service *s, char *nume, char *producator) {
    add_undo_state(s);

    vectMaterieP *rez = creazaVect();
    for (int i = 0; i < size(s->lista); i++) {
        materiePrima *m = getElem(s->lista, i);
        if (strcmp(m->nume, nume) != 0 || strcmp(m->producator, producator) != 0){
            addElem(rez, creazaMateriePrima(m->nume, m->producator, m->cantitate));
        }
    }
    eliberareVectMaterieP(s->lista);
    s->lista = rez;
    return s->lista;
}


void updateMateriePrima(service *s, char *nume, char *producator, char *numeNou, char *producatorNou, int cantitateNoua) {
    add_undo_state(s); // adaugă starea curentă în lista de undo

    for (int i = 0; i < size(s->lista); i++) {
        materiePrima *m = getElem(s->lista, i);
        if (strcmp(m->nume, nume) == 0 && strcmp(m->producator, producator) == 0) {
            stergereMaterie(s->lista->vector[i]);
            s->lista->vector[i] = creazaMateriePrima(numeNou, producatorNou, cantitateNoua);
        }
    }
}


void add_undo_state(service *s) {
    addElem(s->undo, copyVect(s->lista));
}


void undo_action(service *s) {
    if (size(s->undo) > 0) {
        // Ia ultima acțiune din istoricul undo
        vectMaterieP *last_action = getElem(s->undo, size(s->undo) - 1);

        // Aplică undo pentru acea acțiune
        s->lista = copyVect(last_action);

        // Șterge ultima acțiune din istoricul undo
        eliberareVectMaterieP(last_action);
        s->undo->index--;
    }
}

