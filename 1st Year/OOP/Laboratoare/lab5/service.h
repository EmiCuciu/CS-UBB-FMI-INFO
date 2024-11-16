//
// Created by paaull on 18.03.2024.
//

#ifndef OOPLAB2_4_SERVICE_H
#define OOPLAB2_4_SERVICE_H

#include "repo.h"
#include "validari.h"

typedef struct {
    vectMaterieP *lista;
    vectMaterieP *undo;
} service;


int addMateriePrima(service *s, char *nume, char *producator, int cantitate);

vectMaterieP *deleteMateriePrima(service *s, char *nume, char *producator);

void updateMateriePrima(service *s, char *nume, char *producator, char *numeNou, char *producatorNou, int cantitateNoua);



void add_undo_state(service *s);
void undo_action(service *s);

#endif //OOPLAB2_4_SERVICE_H
