#include "ui.h"
#include <iostream>
#include <iomanip>

UI::UI(Service &service) : service(service) {}

void UI::printMenu() {
    std::cout << "\n MENIU\n";
    std::cout << "1. Adauga carte\n";
    std::cout << "2. Sterge carte\n";
    std::cout << "3. Modifica carte\n";
    std::cout << "4. Cauta carte\n";
    std::cout << "5. Afiseaza toate cartile\n";
    std::cout << "0. Iesire\n";
}

void validateTitlu(const std::string &titlu) {
    if (titlu.empty()) {
        throw std::invalid_argument("Titlul nu poate fi vid!");
    }
    if (titlu.size() < 3) {
        throw std::invalid_argument("Titlul trebuie sa aiba cel putin 3 caractere!");
    }
    if (titlu[0] < 'A' || titlu[0] > 'Z') {
        throw std::invalid_argument("Titlul trebuie sa inceapa cu litera mare!");
    }
}

void validateAutor(const std::string &autor) {
    if (autor.empty()) {
        throw std::invalid_argument("Autorul nu poate fi vid!");
    }
    if (autor.size() < 3) {
        throw std::invalid_argument("Autorul trebuie sa aiba cel putin 3 caractere!");
    }
    if (autor[0] < 'A' || autor[0] > 'Z') {
        throw std::invalid_argument("Autorul trebuie sa inceapa cu litera mare!");
    }
}

void validateGen(const std::string &gen) {
    if (gen.empty()) {
        throw std::invalid_argument("Genul nu poate fi vid!");
    }
}

void validateAn(int an){
    if (an < 0){
        throw std::invalid_argument("Anul nu poate fi negativ!");
    }
}


void UI::printAll() {
    auto all = service.getCarti();

    // Printeaza un header pentru date
    std::cout << "----------------------------------------------------------------------------------\n";
    std::cout << std::setw(20) << "Titlu"
              << std::setw(20) << "Autor"
              << std::setw(20) << "Gen"
              << std::setw(10) << "An\n";
    std::cout << "----------------------------------------------------------------------------------\n";

    // Printeaza fiecare carte
    for (const Carte &x: all) {
        std::cout << std::setw(20) << x.getTitlu()
                  << std::setw(20) << x.getAutor()
                  << std::setw(20) << x.getGen()
                  << std::setw(10) << x.getAn() << '\n';
    }

    std::cout << "----------------------------------------------------------------------------------\n";
}

void UI::addCarte_UI() {
    std::string titlu, autor, gen;
    int an =0;

    std::cout << "Introduceti titlul: ";
    std::cin.ignore();    // am folosit cin.ignore() pentru a evita citirea unui newline
    std::getline(std::cin, titlu);       // am folosit getline pentru a citi un string cu spatii

    std::cout << "Introduceti autorul: ";
    std::getline(std::cin, autor);


    std::cout << "Introduceti genul: ";
    std::getline(std::cin, gen);


    std::cout << "Introduceti anul: ";
    std::cin >> an;

    validateTitlu(titlu);
    validateAutor(autor);
    validateGen(gen);
    validateAn(an);

    service.addCarte(titlu, autor, gen, an);
    std::cout << "Cartea a fost adaugata cu succes\n";
}

void UI::deleteCarte_UI() {
    std::string titlu;

    std::cout << "Introduceti titlul cartii pe care vreti sa o stergeti: ";
    std::cin.ignore();
    std::getline(std::cin, titlu);

    service.deleteCarte(titlu);
    std::cout << "Cartea a fost stearsa cu succes\n";
}

void UI::modifyCarte_UI() {
    std::string titlu, new_autor, new_gen;
    int new_an = 0;

    std::cout << "Introduceti titlul cartii pe care vreti sa o modificati: ";
    std::cin.ignore();
    std::getline(std::cin, titlu);

    std::cout << "Introduceti noul autor: ";
    std::getline(std::cin, new_autor);

    std::cout << "Introduceti noul gen: ";
    std::getline(std::cin, new_gen);

    std::cout << "Introduceti noul an: ";
    std::cin >> new_an;

    service.modifyCarte(titlu, new_autor, new_gen, new_an);
    std::cout << "Cartea a fost modificata cu succes\n";
}

void UI::findCarte_UI() {
    std::string titlu;

    std::cout << "Introduceti titlul cartii pe care vreti sa o cautati: ";
    std::cin.ignore();
    std::getline(std::cin, titlu);

    auto carti_gasite = service.findCarte(titlu);

    if (carti_gasite.empty()) {
        std::cout << "Nu exista carti cu titlul dat\n";
        return;
    }

    std::cout << "Cartile cu titlul dat sunt:\n";
    for (const Carte &x: carti_gasite) {
        std::cout << x.intoString() << '\n';
    }
}

char UI::getUserInput() {
    char option;
    std::cout << "Introduceti optiunea: ";
    std::cin >> option;

    std::cout << '\n';
    return option;
}

void UI::addPredefinite() {
    service.addCarte("Ion", "Liviu Rebreanu", "Roman Realist", 1920);
    service.addCarte("Enigma Otiliei", "George Calinescu", "Roman Interbelic", 1938);
    service.addCarte("Morometii", "Marin Preda", "Roman Realist", 1955);
    service.addCarte("Ultima noapte", "Camil Petrescu", "Roman Interbelic", 1930);
    service.addCarte("Maitreyi", "Mircea Eliade", "Roman Psihologic", 1933);
    service.addCarte("Baltagul", "Mihail Sadoveanu", "Roman Realist", 1930);
}

void UI::run() {
    char option;
    //addPredefinite();
    do {
        printAll();
        printMenu();
        option = getUserInput();
        switch (option) {
            case '1':
                addCarte_UI();
                break;
            case '2':
                deleteCarte_UI();
                break;
            case '3':
                modifyCarte_UI();
                break;
            case '4':
                findCarte_UI();
                break;
            case '5':
                continue;
            case '0':
                std::cout << "La revedere!\n";
                break;
            default:
                std::cout << "Optiune invalida\n";
        }
    } while (option != '0');
}