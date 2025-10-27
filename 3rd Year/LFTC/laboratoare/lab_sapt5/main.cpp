#include <iostream>

#include "AutomatFinit.h"

using namespace std;

void afisareMeniu() {
    cout << "1. Afisare multimea starilor" << endl;
    cout << "2. Afisare alfabetul" << endl;
    cout << "3. Afisare tranzitiile" << endl;
    cout << "4. Afisare multimea starilor finale" << endl;
    cout << "5. Verifica secventa" << endl;
    cout << "6. Cel mai lung prefix acceptat" << endl;
    cout << "0. Iesire" << endl;
}

int main() {
    AutomatFinit af;
    int optiune;

    cout << "Citire automat finit din fisier (1) sau tastatura (2): ";
    cin >> optiune;

    if (optiune == 1) {
        string filename;
        // af.citireFisier("automat.txt");
        // af.citireFisier("automatNedeterminist.txt");
        af.citireFisier("automatLiteraliIntregic++.txt");
    } else {
        af.citireTastatura();
    }


    do {
        afisareMeniu();
        cout << "Selectati o optiune: ";
        cin >> optiune;

        switch (optiune) {
            case 1:
                af.afisareStari();
                break;
            case 2:
                af.afisareAlfabet();
                break;
            case 3:
                af.afisareTranzitii();
                break;
            case 4:
                af.afisareStariFinale();
                break;
            case 5: {
                af.afisareTranzitii();
                string secventa;
                cout << "Introduceti secventa de verificat: ";
                cin >> secventa;
                if (af.verificaSecventa(secventa)) {
                    cout << "Secventa este acceptata.\n" << endl;
                } else {
                    cout << "Secventa nu este acceptata.\n" << endl;
                }
                break;
            }
            case 6: {
                af.afisareTranzitii();
                string secventa;
                cout << "Introduceti secventa: ";
                cin >> secventa;
                string prefix = af.celMaiLungPrefixAcceptat(secventa);
                cout << "Cel mai lung prefix acceptat: " << prefix << endl;
                break;
            }
        }
    } while (optiune != 0);

    return 0;
}