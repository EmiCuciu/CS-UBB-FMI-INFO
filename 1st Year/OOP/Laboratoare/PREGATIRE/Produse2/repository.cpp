#include "Repository.h"
#include <fstream>
#include <sstream>

Repository::Repository(const std::string &fisier) : fisier(fisier) {
    citesteProduse();
}

void Repository::citesteProduse() {
    std::ifstream file(fisier);
    if (!file.is_open()) {
        return;
    }

    produse.clear();

    std::string line;
    while (std::getline(file, line)) {
        std::stringstream ss(line);
        int id;
        std::string nume, tip;
        double pret;
        if (ss >> id >> nume >> tip >> pret) {
            produse.emplace_back(id, nume, tip, pret);
        }
    }

    file.close();
}

void Repository::salveazaProduse() {
    std::ofstream file(fisier);
    if (!file.is_open()) {
        return;
    }

    for (const auto &produs : produse) {
        file << produs.getId() << " "
             << produs.getNume() << " "
             << produs.getTip() << " "
             << produs.getPret() << "\n";
    }

    file.close();
}

std::vector<Produs> Repository::getProduse() const {
    return produse;
}

void Repository::adaugaProdus(const Produs &produs) {
    produse.push_back(produs);
    salveazaProduse();
}
