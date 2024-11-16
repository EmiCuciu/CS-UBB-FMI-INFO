#include "test_repo.h"
#include <string.h>
#include <assert.h>

void test_creeazaRepo(){
    REPO *repo = creeazaRepo();
    assert(getNrTranzactii(repo) == 0);
    assert(repo->nr_tranzactii == 0);
    distrugeRepo(repo);
}

void test_distrugeRepo(){
    REPO *repo = creeazaRepo();
    adaugaTranzactie_repo(repo, creeazaTranzactie(1, 100, "intrare", "in"));
    adaugaTranzactie_repo(repo, creeazaTranzactie(2, 200, "iesire", "out"));
    assert(repo->nr_tranzactii == 2);
    distrugeRepo(repo);
}

void test_adaugaTranzactie(){
    REPO *repo = creeazaRepo();

    adaugaTranzactie_repo(repo, creeazaTranzactie(1, 100, "intrare", "in"));
    adaugaTranzactie_repo(repo, creeazaTranzactie(2, 200, "iesire", "out"));
    assert(repo->nr_tranzactii == 2);
    distrugeRepo(repo);
}

void test_stergeTranzactie(){
    REPO *repo = creeazaRepo();
    adaugaTranzactie_repo(repo, creeazaTranzactie(1, 100, "intrare", "in"));
    adaugaTranzactie_repo(repo, creeazaTranzactie(2, 200, "iesire", "out"));
    stergeTranzactie_repo(repo, 0);
    assert(repo->nr_tranzactii == 1);
    distrugeRepo(repo);
}

void test_modificaTranzactie(){
    REPO *repo = creeazaRepo();
    adaugaTranzactie_repo(repo, creeazaTranzactie(1, 100, "intrare", "in"));
    adaugaTranzactie_repo(repo, creeazaTranzactie(2, 200, "iesire", "out"));
    modificaTranzactie_repo(repo, 0, creeazaTranzactie(3, 300, "intrare", "cioroi"));
    assert(get_zi(repo->vector_tranzactii[0]) == 3);
    assert(get_suma(repo->vector_tranzactii[0]) == 300);
    assert(strcmp(get_tip(repo->vector_tranzactii[0]), "intrare") == 0);
    assert(strcmp(get_descriere(repo->vector_tranzactii[0]), "cioroi") == 0);
    assert(repo->nr_tranzactii == 2);
    assert(get_zi(repo->vector_tranzactii[1]) == 2);
    assert(get_suma(repo->vector_tranzactii[1]) == 200);
    assert(strcmp(get_tip(repo->vector_tranzactii[1]), "iesire") == 0);
    assert(strcmp(get_descriere(repo->vector_tranzactii[1]), "out") == 0);
    assert(getNrTranzactii(repo) == 2);
    distrugeRepo(repo);
}

void test_cautaTranzactie(){
    REPO *repo = creeazaRepo();
    adaugaTranzactie_repo(repo, creeazaTranzactie(1, 100, "intrare", "in"));
    adaugaTranzactie_repo(repo, creeazaTranzactie(2, 200, "iesire", "out"));
    Tranzactie *tranzactie = cautaTranzactie(repo, 0);
    assert(get_zi(tranzactie) == 1);
    assert(get_suma(tranzactie) == 100);
    assert(strcmp(get_tip(tranzactie), "intrare") == 0);
    assert(strcmp(get_descriere(tranzactie), "in") == 0);
    distrugeRepo(repo);
}

void test_getNrTranzactii(){
    REPO *repo = creeazaRepo();
    adaugaTranzactie_repo(repo, creeazaTranzactie(1, 100, "intrare", "in"));
    adaugaTranzactie_repo(repo, creeazaTranzactie(2, 200, "iesire", "out"));
    assert(getNrTranzactii(repo) == 2);
    distrugeRepo(repo);
}

void test_get_vector_tranzactii(){
    REPO *repo = creeazaRepo();
    adaugaTranzactie_repo(repo, creeazaTranzactie(1, 100, "intrare", "in"));
    adaugaTranzactie_repo(repo, creeazaTranzactie(2, 200, "iesire", "out"));
    Tranzactie **vector = get_vector_tranzactii(repo);
    assert(get_zi(vector[0]) == 1);
    assert(get_suma(vector[0]) == 100);
    assert(strcmp(get_tip(vector[0]), "intrare") == 0);
    assert(strcmp(get_descriere(vector[0]), "in") == 0);
    assert(get_zi(vector[1]) == 2);
    assert(get_suma(vector[1]) == 200);
    assert(strcmp(get_tip(vector[1]), "iesire") == 0);
    assert(strcmp(get_descriere(vector[1]), "out") == 0);
    distrugeRepo(repo);
}