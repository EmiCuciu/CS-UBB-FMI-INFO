//
// Created by Emi on 4/14/2024.
//

#include <limits>
#include "UI.h"
#include "iostream"

UI::UI(Service &service) : service(service) {}

void UI::printMenu() {
    std::cout << "MENU\n";
    std::cout << "1. Adauga carte\n";
    std::cout << "2. Update carte\n";
    std::cout << "3. Sterge carte\n";
    std::cout << "4. Cauta carte\n";
    std::cout << "5. Filtreaza carti\n";
    std::cout << "6. Sorteaza carti\n";
    std::cout << "7. Intra in meniul de cos\n";
    std::cout << "8. Afiseaza cartile\n";
    std::cout << "9. Nr carti per gen\n";
    std::cout << "u. Undo\n";
    std::cout << "0. Exit\n";
}

void UI::uiPrintCarti() {
    auto carti = service.getAllLib();

    if (carti.empty()) {
        std::cout << "Nu exista carti in librarie.\n";
        return;
    }

    std::cout << "Cartile din librarie sunt:\n";
    for (const Carte &carte: carti) {
        std::cout << carte.toString() << "\n";
    }
}

void UI::uiAddCarte() {
    std::string titlu, autor, gen;
    int an{0};

    try {
        std::cout << "Introduceti titlul cartii: ";
        std::getline(std::cin, titlu);

        std::cout << "Introduceti autorul cartii: ";
        std::getline(std::cin, autor);

        std::cout << "Introduceti genul cartii: ";
        std::getline(std::cin, gen);

        std::cout << "Introduceti anul cartii: ";
        std::cin >> an;

        if (std::cin.fail()) {
            std::cin.clear();
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
            throw std::runtime_error("Anul trebuie sa fie un numar intreg.\n");
        }

        service.addCarteLibrarie(titlu, autor, gen, an);
        std::cout << "Cartea a fost adaugata cu succes.\n";
    }
    catch (const std::exception &e) {
        throw std::runtime_error("Eroare la adaugare: " + std::string(e.what()));
    }
}

void UI::uiUpdateCarte() {
    std::string titlu, new_autor, new_gen;
    int new_an{0};

    try {
        std::cout << "Introduceti titlul cartii pe care doriti sa o modificati: ";
        std::getline(std::cin, titlu);

        std::cout << "Introduceti noul autor al cartii: ";
        std::getline(std::cin, new_autor);

        std::cout << "Introduceti noul gen al cartii: ";
        std::getline(std::cin, new_gen);

        std::cout << "Introduceti noul an al cartii: ";
        std::cin >> new_an;

        if (std::cin.fail()) {
            std::cin.clear();
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
            throw std::runtime_error("Anul trebuie sa fie un numar intreg.\n");
        }

        service.updateCarteLibrarie(titlu, new_autor, new_gen, new_an);
        std::cout << "Cartea a fost modificata cu succes.\n";
    }
    catch (const std::exception &e) {
        throw std::runtime_error("Eroare la modificare: " + std::string(e.what()));
    }
}

void UI::uiDeleteCarte() {
    std::string titlu;

    try {
        std::cout << "Introduceti titlul cartii pe care doriti sa o stergeti: ";
        std::getline(std::cin, titlu);

        service.deleteCarteLibrarie(titlu);

        std::cout << "Cartea a fost stearsa cu succes.\n";
    }
    catch (const std::exception &e) {
        throw std::runtime_error("Eroare la stergere: " + std::string(e.what()));
    }
}

void UI::uiCautaCarte() {
    std::string titlu;

    try {
        std::cout << "Introduceti titlul cartii pe care doriti sa o cautati: ";
        std::getline(std::cin, titlu);

        auto carti = service.cautaCarteLibrarie(titlu);

        if (carti.empty()) {
            std::cout << "Nu exista carti cu acest titlu.\n";
            return;
        }

        std::cout << " Cartile cu titlul " << titlu << " sunt: \n";
        for (const Carte &carte: carti) {
            std::cout << carte.toString() << "\n";
        }
    }
    catch (const std::exception &e) {
        throw std::runtime_error("Eroare la cautare: " + std::string(e.what()));
    }
}

void UI::uiFiltreazaCarti() {
    int an_minim{0};

    try {
        std::cout << "Introduceti anul minim: ";
        std::cin >> an_minim;

        if (std::cin.fail()) {
            std::cin.clear();
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
            throw std::runtime_error("Anul trebuie sa fie un numar intreg.\n");
        }

        auto carti = service.filtreazaCarti(an_minim);

        if (carti.empty()) {
            std::cout << "Nu exista carti cu anul mai mare sau egal cu " << an_minim << ".\n";
            return;
        }

        std::cout << "Cartile cu anul mai mare sau egal cu " << an_minim << " sunt:\n";
        for (const Carte &carte: carti) {
            std::cout << carte.toString() << "\n";
        }
    }
    catch (const std::exception &e) {
        throw std::runtime_error("Eroare la filtrare: " + std::string(e.what()));
    }
}

void UI::uiSorteazaCarti() {
    std::string criteriu;

    try {
        std::cout << "Introduceti criteriul de sortare (titlu, autor, gen, an): ";
        std::getline(std::cin, criteriu);

        if (criteriu == "titlu") {
            auto carti = service.sorteazaCarti([](const Carte &c1, const Carte &c2) {
                return c1.getTitlu() < c2.getTitlu();
            });

            std::cout << "Cartile sortate dupa titlu sunt:\n";
            for (const Carte &carte: carti) {
                std::cout << carte.toString() << "\n";
            }
        } else if (criteriu == "autor") {
            auto carti = service.sorteazaCarti([](const Carte &c1, const Carte &c2) {
                return c1.getAutor() < c2.getAutor();
            });

            std::cout << "Cartile sortate dupa autor sunt:\n";
            for (const Carte &carte: carti) {
                std::cout << carte.toString() << "\n";
            }
        } else if (criteriu == "gen") {
            auto carti = service.sorteazaCarti([](const Carte &c1, const Carte &c2) {
                return c1.getGen() < c2.getGen();
            });

            std::cout << "Cartile sortate dupa gen sunt:\n";
            for (const Carte &carte: carti) {
                std::cout << carte.toString() << "\n";
            }
        } else if (criteriu == "an") {
            auto carti = service.sorteazaCarti([](const Carte &c1, const Carte &c2) {
                return c1.getAn() < c2.getAn();
            });

            std::cout << "Cartile sortate dupa an sunt:\n";
            for (const Carte &carte: carti) {
                std::cout << carte.toString() << "\n";
            }
        } else {
            throw std::runtime_error("Criteriul de sortare nu este valid.\n");
        }
    }
    catch (const std::exception &e) {
        throw std::runtime_error("Eroare la sortare: " + std::string(e.what()));
    }
}

char UI::getUserInput() {
    char userInput;
    std::cout << "Introduceti optiunea: ";
    std::cin >> userInput;
    std::cin.ignore();
    return userInput;
}

void UI::addPredef() {
    service.addCarteLibrarie("TATA", "Mama", "SF", 2012);
    service.addCarteLibrarie("BAU", "BALAUR", "KALA", 1056);
    service.addCarteLibrarie("MAra", "Tara", "SF", 2021);
    service.addCarteLibrarie("Masdas", "Dasa", "KALA", 2000);
    service.addCarteLibrarie("ORAS", "Tara", "UBB", 2010);
}

void UI::run() {
    char userInput;
    addPredef();

    while (true) {
        printMenu();
        userInput = getUserInput();
        try {
            switch (userInput) {
                case '1':
                    uiAddCarte();
                    break;
                case '2':
                    uiUpdateCarte();
                    break;
                case '3':
                    uiDeleteCarte();
                    break;
                case '4':
                    uiCautaCarte();
                    break;
                case '5':
                    uiFiltreazaCarti();
                    break;
                case '6':
                    uiSorteazaCarti();
                    break;
                case '7':
                    uiCosMenu();
                    break;
                case '8':
                    std::cout<<"\n";
                    uiPrintCarti();
                    std::cout<<"\n";
                    break;
                case '9':
                    uinrCartiGen();
                    break;
                case 'u':
                    uiUndo();
                    break;
                case '0':
                    return;
                default:
                    std::cout << "Optiune invalida.\n";
            }
        }
        catch (const std::exception &e) {
            std::cout << e.what() << "\n";
        }
    }
}

void UI::printCosMenu() {
    std::cout << "COS MENU\n";
    std::cout << "1. Adauga carte in cos\n";
    std::cout << "2. Sterge cos\n";
    std::cout << "3. Populeaza cos cu carti random\n";
    std::cout << "4. Afiseaza cos\n";
    std::cout << "5. Exporta cos in fisier HTML\n";
    std::cout << "0. Exit\n";
}

void UI::uiPrintCos() {
    auto carti = service.getAllCos();

    if (carti.empty()) {
        std::cout << "Cosul este gol.\n";
        return;
    }

    std::cout << "Cartile din cos sunt:\n";
    for (const Carte &carte: carti) {
        std::cout << carte.toString() << "\n";
    }
}

void UI::uiAddCarteCos() {
    std::string titlu;

    try {
        std::cout << "Introduceti titlul cartii pe care doriti sa o adaugati in cos: ";
        std::getline(std::cin, titlu);

        service.addCarteCos(titlu);

        std::cout << "Cartea a fost adaugata in cos cu succes.\n";

    }
    catch (const std::exception &e) {
        throw std::runtime_error("Eroare la adaugare: " + std::string(e.what()));
    }
}

void UI::uiDeleteCos() {
    service.deleteCos();
    std::cout << "Cosul a fost sters cu succes.\n";
}

void UI::uiPopulateRandomCos() {
    size_t nr_carti;

    try {
        std::cout << "Introduceti numarul de carti pe care doriti sa le adaugati in cos: ";
        std::cin >> nr_carti;

        if (std::cin.fail()) {
            std::cin.clear();
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
            throw std::runtime_error("Numarul de carti trebuie sa fie un numar intreg.\n");
        }

        service.populateRandomCos(nr_carti);
        std::cout << "Cosul a fost populat cu " << nr_carti << " carti random.\n";
    }
    catch (const std::exception &e) {
        throw std::runtime_error("Eroare la populare: " + std::string(e.what()));
    }
}

void UI::uiExportCos() {
    std::string filename;

    try {
        std::cout << "Introduceti numele fisierului in care doriti sa exportati cosul: ";
        std::getline(std::cin, filename);

        service.exportCos(filename);
        std::cout << "Cosul a fost exportat cu succes in fisierul " << filename << ".\n";
    }
    catch (const std::exception &e) {
        throw std::runtime_error("Eroare la export: " + std::string(e.what()));
    }
}

void UI::uiCosMenu() {
    char userInput;

    while (true) {
        printCosMenu();
        userInput = getUserInput();

        try {
            switch (userInput) {
                case '1':
                    uiAddCarteCos();
                    break;
                case '2':
                    uiDeleteCos();
                    break;
                case '3':
                    uiPopulateRandomCos();
                    break;
                case '4':
                    uiPrintCos();
                    break;
                case '5':
                    uiExportCos();
                    break;
                case '0':
                    return;
                default:
                    std::cout << "Optiune invalida.\n";
            }
        }
        catch (const std::exception &e) {
            std::cout << e.what() << "\n";
        }
    }
}


void UI::uinrCartiGen() {
    std::string gen_dorit;
    std::cout << "Introduceti genul dupa care vreti sa aflati numarul de carti: ";
    std::getline(std::cin, gen_dorit);
    std::unordered_map<std::string, int> genCount = service.numarCartiGen(gen_dorit);

    for (const auto &pair: genCount) {
        std::cout << "Gen: " << pair.first << ", Numar de carti cu genul " << gen_dorit << ": " << pair.second << "\n";
    }
}

void UI::uiUndo() {
    service.undo();
}