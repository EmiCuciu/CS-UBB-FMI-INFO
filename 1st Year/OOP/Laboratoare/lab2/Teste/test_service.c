#include "test_service.h"
#include <assert.h>
#include <stdio.h>
#include <string.h>

void test_creeazaService() {
    REPO *repo = creeazaRepo();
    SERVICE *service = creeazaService(repo);

    assert(service != NULL);
    assert(service->repo == repo);

    distrugeService(service);
    distrugeRepo(repo);
}

void test_distrugeService() {
    REPO *repo = creeazaRepo();
    SERVICE *service = creeazaService(repo);

    distrugeService(service);
    distrugeRepo(repo);
}

void test_adaugaTranzactie_service() {
    REPO *repo = creeazaRepo();
    SERVICE *service = creeazaService(repo);
    adaugaTranzactie_service(service, 1, 100, "intrare", "salariu");

    Tranzactie *tranzactie = cautaTranzactie(service->repo, 0);
    assert(get_zi(tranzactie) == 1);
    assert(get_suma(tranzactie) == 100);
    assert(strcmp(get_tip(tranzactie), "intrare") == 0);
    assert(strcmp(get_descriere(tranzactie), "salariu") == 0);

    distrugeService(service);
    distrugeRepo(repo);
}

void test_stergeTranzactie_service() {
    REPO *repo = creeazaRepo();
    SERVICE *service = creeazaService(repo);

    adaugaTranzactie_service(service, 1, 100, "intrare", "salariu");
    adaugaTranzactie_service(service, 2, 200, "iesire", "chirie");
    stergeTranzactie_service(service, 1, "salariu");

    assert(getNrTranzactii(service->repo) == 1);
    Tranzactie *tranzactie = cautaTranzactie(service->repo, 0);
    assert(get_zi(tranzactie) == 2);
    assert(get_suma(tranzactie) == 200);
    assert(strcmp(get_tip(tranzactie), "iesire") == 0);
    assert(strcmp(get_descriere(tranzactie), "chirie") == 0);

    distrugeService(service);
    distrugeRepo(repo);
}

void test_modificaTranzactie_service() {
    REPO *repo = creeazaRepo();
    SERVICE *service = creeazaService(repo);
    adaugaTranzactie_service(service, 1, 100, "intrare", "salariu");
    adaugaTranzactie_service(service, 2, 200, "iesire", "chirie");
    modificaTranzactie_service(service, 1, "salariu", 300, "iesire", "cheltuieli");

    Tranzactie *tranzactie = cautaTranzactie(service->repo, 0);
    assert(get_zi(tranzactie) == 1);
    assert(get_suma(tranzactie) == 300);
    assert(strcmp(get_tip(tranzactie), "iesire") == 0);
    assert(strcmp(get_descriere(tranzactie), "cheltuieli") == 0);

    distrugeService(service);
    distrugeRepo(repo);
}