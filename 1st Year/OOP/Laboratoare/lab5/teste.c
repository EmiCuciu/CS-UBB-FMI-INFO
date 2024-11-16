//
// Created by Emi on 3/26/2024.
//

#include "teste.h"
#include <assert.h>
#include <string.h>

void test_creaza_materie_prima() {
    materiePrima *m = creazaMateriePrima("cafea", "illy", 100);
    assert(m->cantitate == 100);
    assert(strcmp(m->nume, "cafea") == 0);
    assert(strcmp(m->producator, "illy") == 0);
    stergereMaterie(m);
}

void test_copy_materie_prima() {
    materiePrima *m = creazaMateriePrima("cafea", "illy", 100);
    materiePrima *m2 = copieMateriePrima(m);
    assert(m2->cantitate == 100);
    assert(strcmp(m2->nume, "cafea") == 0);
    assert(strcmp(m2->producator, "illy") == 0);
    stergereMaterie(m);
    stergereMaterie(m2);
}

void test_creaza_vector_repo() {
    vectMaterieP *v = creazaVect();
    assert(v->index == 0);
    assert(v->cpm == 1);
    assert(v->vector != NULL);
    eliberareVectMaterieP(v);
}


void test_get_elem_repo() {
    vectMaterieP *v = creazaVect();
    materiePrima *m = creazaMateriePrima("cafea", "illy", 100);
    addElem(v, m);
    assert(getElem(v, 0) == m);
    eliberareVectMaterieP(v);
}

void test_size_repo() {
    vectMaterieP *v = creazaVect();
    materiePrima *m = creazaMateriePrima("cafea", "illy", 100);
    addElem(v, m);
    assert(size(v) == 1);
    eliberareVectMaterieP(v);
}

void test_add_elem_repo() {
    vectMaterieP *v = creazaVect();
    materiePrima *m1 = creazaMateriePrima("cafea", "illy", 100);
    materiePrima *m2 = creazaMateriePrima("ceai", "lipton", 50);

    // Test adding an element when the capacity of the vector is not full
    addElem(v, m1);
    assert(getElem(v, 0) == m1);
    assert(size(v) == 1);

    // Test adding an element when the capacity of the vector is full
    // This should trigger the resizing of the vector
    addElem(v, m2);
    assert(getElem(v, 1) == m2);
    assert(size(v) == 2);

    eliberareVectMaterieP(v);
}

void test_copy_vector_repo() {
    vectMaterieP *v = creazaVect();
    materiePrima *m = creazaMateriePrima("cafea", "illy", 100);
    addElem(v, m);
    vectMaterieP *v2 = copyVect(v);
    assert(getElem(v2, 0) != getElem(v, 0));
    eliberareVectMaterieP(v);
    eliberareVectMaterieP(v2);
}

void test_stergere_materie() {
    materiePrima *m = creazaMateriePrima("cafea", "illy", 100);
    stergereMaterie(m);
}

void test_validare() {
    assert(validare(100, "cafea", "illy") == 1);
    assert(validare(0, "cafea", "illy") == 0);
    assert(validare(100, "", "illy") == 0);
    assert(validare(100, "cafea", "") == 0);
}

void test_adauga_materie_prima_service() {
    service *s = malloc(sizeof(service));
    s->lista = creazaVect();
    s->undo = creazaVect();
    addMateriePrima(s, "cafea", "illy", 100);
    assert(size(s->lista) == 1);
    free(s);
}

void test_sterge_materie_prima_service() {
    service *s = malloc(sizeof(service));
    s->lista = creazaVect();
    s->undo = creazaVect();
    addMateriePrima(s, "cafea", "illy", 100);
    addMateriePrima(s, "ceai", "lipton", 50);
    deleteMateriePrima(s, "cafea", "illy");
    assert(size(s->lista) == 1);
    deleteMateriePrima(s, "ceai", "lipton");
    assert(size(s->lista) == 0);
    addMateriePrima(s, "cafea", "illy", 100);
    deleteMateriePrima(s, "ceai", "lipton");
    assert(size(s->lista) == 1);
    free(s);
}

void test_update_materie_prima_service(){
    service *s = malloc(sizeof(service));
    s->lista = creazaVect();
    s->undo = creazaVect();
    addMateriePrima(s, "cafea", "illy", 100);
    updateMateriePrima(s, "cafea", "illy", "ceai", "lipton", 50);
    materiePrima *m = getElem(s->lista, 0);
    assert(strcmp(m->nume, "ceai") == 0);
    assert(strcmp(m->producator, "lipton") == 0);
    assert(m->cantitate == 50);
    free(s);
}

void test_undo() {
    service *s = malloc(sizeof(service));
    s->lista = creazaVect();
    s->undo = creazaVect();
    addMateriePrima(s, "cafea", "illy", 100);
    addMateriePrima(s, "ceai", "lipton", 50);
    deleteMateriePrima(s, "cafea", "illy");
    undo_action(s);
    assert(size(s->lista) == 2);
    undo_action(s);
    assert(size(s->lista) == 2);
    undo_action(s);
    assert(size(s->lista) == 1);
    free(s);
}

void run_all_tests() {
    test_creaza_materie_prima();
    test_copy_materie_prima();
    test_creaza_vector_repo();
    test_get_elem_repo();
    test_size_repo();
    test_add_elem_repo();
    test_copy_vector_repo();
    test_stergere_materie();
    test_validare();

    test_adauga_materie_prima_service();
    test_sterge_materie_prima_service();
    test_update_materie_prima_service();
    test_undo();

}