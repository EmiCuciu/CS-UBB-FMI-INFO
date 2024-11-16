#ifndef LAB2_DINAMIC_TRANZACTIE_UI_H
#define LAB2_DINAMIC_TRANZACTIE_UI_H

#include "../Service/tranzactie_service.h"

void run_ui();

void adauga_tranzactie_ui(SERVICE* service);

void sterge_tranzactie_ui(SERVICE* service);

void modifica_tranzactie_ui(SERVICE* service);

void afiseaza_tranzactii_ui(SERVICE* service);

void afiseaza_tranzactii_ui_tip(SERVICE* service);

void afiseaza_tranzactii_ui_suma_mai_mare(SERVICE* service);

void afiseaza_tranzactii_ui_suma_mai_mica(SERVICE* service);

void afiseaza_tranzactii_ui_suma_crescator(SERVICE* service);

void afiseaza_tranzactii_ui_suma_descrescator(SERVICE* service);

void afiseaza_tranzactii_ui_zi_crescator(SERVICE* service);

void afiseaza_tranzactii_ui_zi_descrescator(SERVICE* service);

#endif //LAB2_DINAMIC_TRANZACTIE_UI_H
