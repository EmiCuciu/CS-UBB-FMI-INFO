#include "Repository.h"
#include <iostream>

const std::vector<Carte> &Repository::getCarti() {
    return carti;
}

size_t Repository::getSize() {
    return carti.size();
}

void Repository::addCarte(const Carte &carte) {
    carti.push_back(carte);
}

void Repository::deleteCarte(const std::vector<Carte>::iterator &position) {
    carti.erase(position);
}

void Repository::modifyCarte(const std::vector<Carte>::iterator &position, const Carte &new_carte) {
    *position = new_carte;
}

std::vector<Carte>::iterator Repository::findCarte(const std::string &titlu) {
    return std::find_if(carti.begin(), carti.end(), [&](const Carte &carte) {
        return carte.getTitlu() == titlu;
    });
}

bool Repository::validatePosition(const std::vector<Carte>::iterator &position) {
    return position != carti.end();
}