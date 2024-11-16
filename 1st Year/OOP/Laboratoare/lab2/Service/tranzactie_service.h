#ifndef LAB2_DINAMIC_TRANZACTIE_SERVICE_H
#define LAB2_DINAMIC_TRANZACTIE_SERVICE_H

#include "../Repository/tranzactie_repo.h"

typedef struct {
    REPO* repo;
}SERVICE;

SERVICE* creeazaService(REPO* repo);

void distrugeService(SERVICE* service);

void adaugaTranzactie_service(SERVICE* service, int zi, float suma, char* tip, char* descriere);

void stergeTranzactie_service(SERVICE* service, int zi, char* descriere);

void modificaTranzactie_service(SERVICE* service, int zi, char* descriere, float suma_noua, char* tip_nou, char* descriere_noua);

#endif //LAB2_DINAMIC_TRANZACTIE_SERVICE_H
