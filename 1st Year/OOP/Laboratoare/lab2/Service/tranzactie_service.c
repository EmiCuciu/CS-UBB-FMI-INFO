#include "tranzactie_service.h"
#include <stdlib.h>
#include <string.h>

/* Functie care creeaza un obiect de tip SERVICE
 * repo - pointer la un obiect de tip REPO
 * returneaza un pointer la un obiect de tip SERVICE
 * preconditii: repo - pointer la un obiect de tip REPO
 * postconditii: se returneaza un pointer la un obiect de tip SERVICE
 */
SERVICE *creeazaService(REPO *repo) {
    SERVICE *service = (SERVICE *) malloc(sizeof(SERVICE));
    service->repo = repo;
    return service;
}

/* Functie care distruge un obiect de tip SERVICE
 * service - pointer la un obiect de tip SERVICE
 * preconditii: service - pointer la un obiect de tip SERVICE
 * postconditii: obiectul de tip SERVICE este distrus
 */
void distrugeService(SERVICE *service) {
    free(service);
}

/* Functie care adauga o tranzactie in repository
 * service - pointer la un obiect de tip SERVICE
 * zi - int
 * suma - float
 * tip - char*
 * descriere - char*
 * preconditii: service - pointer la un obiect de tip SERVICE, zi - int, suma - float, tip, descriere - char*
 * postconditii: tranzactia este adaugata in repository
 */
void adaugaTranzactie_service(SERVICE *service, int zi, float suma, char *tip, char *descriere) {
    Tranzactie *tranzactie = creeazaTranzactie(zi, suma, tip, descriere);
    adaugaTranzactie_repo(service->repo, tranzactie);
}

/* Functie care sterge o tranzactie din repository
 * service - pointer la un obiect de tip SERVICE
 * zi - int
 * descriere - char*
 * preconditii: service - pointer la un obiect de tip SERVICE, zi - int, descriere - char*
 * postconditii: tranzactia este stearsa din repository
 */
void stergeTranzactie_service(SERVICE *service, int zi, char *descriere) {
    for (int i = 0; i < getNrTranzactii(service->repo); i++) {
        Tranzactie *tranzactie = cautaTranzactie(service->repo, i);
        if(get_zi(tranzactie) == zi && strcmp(get_descriere(tranzactie), descriere) == 0){
            stergeTranzactie_repo(service->repo, i);
            return;
        }
    }
}

/* Functie care modifica o tranzactie din repository
 * service - pointer la un obiect de tip SERVICE
 * zi - int
 * descriere - char*
 * suma_noua - float
 * tip_nou - char*
 * descriere_noua - char*
 * preconditii: service - pointer la un obiect de tip SERVICE, zi - int, descriere - char*, suma_noua - float, tip_nou - char*, descriere_noua - char*
 * postconditii: tranzactia este modificata in repository
 */
void modificaTranzactie_service(SERVICE *service, int zi, char* descriere, float suma_noua, char *tip_nou, char* descriere_noua){
    for (int i = 0; i < getNrTranzactii(service->repo); i++) {
        Tranzactie *tranzactie = cautaTranzactie(service->repo, i);
        if(get_zi(tranzactie) == zi && strcmp(get_descriere(tranzactie), descriere) == 0){
            Tranzactie *tranzactie_noua = creeazaTranzactie(zi, suma_noua, tip_nou, descriere_noua);
            modificaTranzactie_repo(service->repo, i, tranzactie_noua);
            return;
        }
    }
}

