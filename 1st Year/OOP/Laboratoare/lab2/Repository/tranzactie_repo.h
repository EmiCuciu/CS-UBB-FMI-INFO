#ifndef LAB2_DINAMIC_TRANZACTIE_REPO_H
#define LAB2_DINAMIC_TRANZACTIE_REPO_H

#include "../Domain/tranzactie.h"

typedef struct{
    Tranzactie** vector_tranzactii;
    int nr_tranzactii;
}REPO;

REPO* creeazaRepo();

void distrugeRepo(REPO* repo);

void adaugaTranzactie_repo(REPO* repo, Tranzactie* tranzactie);

void stergeTranzactie_repo(REPO* repo, int index);

void modificaTranzactie_repo(REPO* repo, int index, Tranzactie* tranzactie);

Tranzactie* cautaTranzactie(REPO* repo, int index);

int getNrTranzactii(REPO* repo);

Tranzactie **get_vector_tranzactii(REPO* repo);

#endif //LAB2_DINAMIC_TRANZACTIE_REPO_H
