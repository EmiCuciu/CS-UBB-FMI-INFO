#include "tranzactie_repo.h"
#include <stdlib.h>

/*
 * Creeaza repository-ul in care se vor stoca tranzactiile si aloca memorie pentru vectorul de tranzactii din repository
 * @return REPO*
 * preconditii: -
 * postconditii: se returneaza un repository gol
 */
REPO *creeazaRepo() {
    REPO *repository = (REPO *) malloc(sizeof(REPO));
    repository->nr_tranzactii = 0;
    repository->vector_tranzactii = (Tranzactie**) malloc(sizeof (Tranzactie*));    //Alocare de memorie pentru vectorul de tranzactii
    return repository;
}

/*
 * Distruge repository-ul si elibereaza memoria alocata pentru vectorul de tranzactii
 * @param repository - REPO*
 * preconditii: repository - REPO*
 * postconditii: memoria alocata pentru repository si vectorul de tranzactii este eliberata
 */
void distrugeRepo(REPO* repository){
    free(repository->vector_tranzactii);
    free(repository);
}

/*
 * Adauga o tranzactie in repository
 * @param repository - REPO*
 * @param tranzactie - Tranzactie*
 * preconditii: repository - REPO*, tranzactie - Tranzactie*
 * postconditii: tranzactia este adaugata in repository
 */
void adaugaTranzactie_repo(REPO *repository, Tranzactie *tranzactie){
    Tranzactie** temp = (Tranzactie**) realloc(repository->vector_tranzactii, (repository->nr_tranzactii + 1) * sizeof(Tranzactie*));
    repository->vector_tranzactii = temp;
    repository->vector_tranzactii[repository->nr_tranzactii] = tranzactie;
    repository->nr_tranzactii++;
}

/*
 * Sterge o tranzactie din repository
 * @param repository - REPO*
 * @param index - int
 * preconditii: repository - REPO*, index - int
 * postconditii: tranzactia de pe pozitia index este stearsa din repository
 */
void stergeTranzactie_repo(REPO *repository, int index){
    distrugeTranzactie(repository->vector_tranzactii[index]);
    for (int i = index; i < repository->nr_tranzactii - 1; i++) {
        repository->vector_tranzactii[i] = repository->vector_tranzactii[i + 1];
    }
    repository->nr_tranzactii--;
}

/*
 * Modifica o tranzactie din repository
 * @param repository - REPO*
 * @param index - int
 * @param tranzactie - Tranzactie*
 * preconditii: repository - REPO*, index - int, tranzactie - Tranzactie*
 * postconditii: tranzactia de pe pozitia index este modificata cu tranzactia primita ca parametru
 */
void modificaTranzactie_repo(REPO *repository, int index, Tranzactie *tranzactie){
    distrugeTranzactie(repository->vector_tranzactii[index]);
    repository->vector_tranzactii[index] = tranzactie;
}

/*
 * Cauta o tranzactie in repository
 * @param repository - REPO*
 * @param index - int
 * @return Tranzactie*
 * preconditii: repository - REPO*, index - int
 * postconditii: se returneaza tranzactia de pe pozitia index
 */
Tranzactie *cautaTranzactie(REPO *repository, int index){
    return repository->vector_tranzactii[index];
}

/*
 * Returneaza numarul de tranzactii din repository
 * @param repository - REPO*
 * @return int
 * preconditii: repository - REPO*
 * postconditii: se returneaza numarul de tranzactii din repository
 */
int getNrTranzactii(REPO *repository){
    return repository->nr_tranzactii;
}

/*
 * Returneaza vectorul de tranzactii din repository
 * @param repository - REPO*
 * @return Tranzactie**
 * preconditii: repository - REPO*
 * postconditii: se returneaza vectorul de tranzactii din repository
 */
Tranzactie **get_vector_tranzactii(REPO *repository){
    return repository->vector_tranzactii;
}