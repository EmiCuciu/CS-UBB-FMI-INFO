#include "test_domain.h"
#include <assert.h>
#include <string.h>

void test_creeazaTranzactie(){
    Tranzactie* tranzactie = creeazaTranzactie(1, 100, "intrare", "salariu");
    assert(get_zi(tranzactie) == 1);
    assert(get_suma(tranzactie) == 100);
    assert(strcmp(get_tip(tranzactie), "intrare") == 0);
    assert(strcmp(get_descriere(tranzactie), "salariu") == 0);
    distrugeTranzactie(tranzactie);
}

void test_get_zi(){
    Tranzactie* tranzactie = creeazaTranzactie(1, 100, "intrare", "salariu");
    assert(get_zi(tranzactie) == 1);
    distrugeTranzactie(tranzactie);
}

void test_get_suma(){
    Tranzactie* tranzactie = creeazaTranzactie(1, 100, "intrare", "salariu");
    assert(get_suma(tranzactie) == 100);
    distrugeTranzactie(tranzactie);
}

void test_get_tip(){
    Tranzactie* tranzactie = creeazaTranzactie(1, 100, "intrare", "salariu");
    assert(strcmp(get_tip(tranzactie), "intrare") == 0);
    distrugeTranzactie(tranzactie);
}

void test_get_descriere(){
    Tranzactie* tranzactie = creeazaTranzactie(1, 100, "intrare", "salariu");
    assert(strcmp(get_descriere(tranzactie), "salariu") == 0);
    distrugeTranzactie(tranzactie);
}

void test_set_zi(){
    Tranzactie* tranzactie = creeazaTranzactie(1, 100, "intrare", "salariu");
    set_zi(tranzactie, 2);
    assert(get_zi(tranzactie) == 2);
    distrugeTranzactie(tranzactie);
}

void test_set_suma(){
    Tranzactie* tranzactie = creeazaTranzactie(1, 100, "intrare", "salariu");
    set_suma(tranzactie, 200);
    assert(get_suma(tranzactie) == 200);
    distrugeTranzactie(tranzactie);
}

void test_set_tip(){
    Tranzactie* tranzactie = creeazaTranzactie(1, 100, "intrare", "salariu");
    set_tip(tranzactie, "iesire");
    assert(strcmp(get_tip(tranzactie), "iesire") == 0);
    distrugeTranzactie(tranzactie);
}

void test_set_descriere(){
    Tranzactie* tranzactie = creeazaTranzactie(1, 100, "intrare", "salariu");
    set_descriere(tranzactie, "chirie");
    assert(strcmp(get_descriere(tranzactie), "chirie") == 0);
    distrugeTranzactie(tranzactie);
}

