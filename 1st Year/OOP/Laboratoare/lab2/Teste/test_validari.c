#include "test_validari.h"
#include <assert.h>
#include "../Domain/tranzactie.h"

void test_validari(){
    Tranzactie* tranzactie = creeazaTranzactie(1, 100, "intrare", "salariu");
    assert(validareTranzactie(get_zi(tranzactie), get_suma(tranzactie), get_tip(tranzactie), get_descriere(tranzactie)) == 1);
    distrugeTranzactie(tranzactie);

    Tranzactie* tranzactie1 = creeazaTranzactie(32,100,"intrare","salariu");
    assert(validareTranzactie(get_zi(tranzactie1), get_suma(tranzactie1), get_tip(tranzactie1), get_descriere(tranzactie1)) == 0);
    distrugeTranzactie(tranzactie1);

    Tranzactie* tranzactie2 = creeazaTranzactie(1,-12,"intrare","salariu");
    assert(validareTranzactie(get_zi(tranzactie2), get_suma(tranzactie2), get_tip(tranzactie2), get_descriere(tranzactie2)) == 0);
    distrugeTranzactie(tranzactie2);

    Tranzactie* tranzactie3 = creeazaTranzactie(1,100,"intr","salariu");
    assert(validareTranzactie(get_zi(tranzactie3), get_suma(tranzactie3), get_tip(tranzactie3), get_descriere(tranzactie3)) == 0);
    distrugeTranzactie(tranzactie3);

    Tranzactie* tranzactie4 = creeazaTranzactie(1,100,"intrare","sa");
    assert(validareTranzactie(get_zi(tranzactie4), get_suma(tranzactie4), get_tip(tranzactie4), get_descriere(tranzactie4)) == 0);
    distrugeTranzactie(tranzactie4);

}