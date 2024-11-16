//
// Created by Emi on 4/14/2024.
//

#include "Repo.h"

void AbstractREPO::addCarte(const Carte &carte) {
    items.push_back(carte);
}

std::vector<Carte> & AbstractREPO::getCarti() {
    return items;
}

size_t AbstractREPO::getLen() const {
    return items.size();
}

/// Librarie


Carte Librarie::deleteCarte(std::vector<Carte>::iterator &position) {
    auto prev = *position;
    items.erase(position);
    return prev;}

Carte Librarie::updateCarte(std::vector<Carte>::iterator &position, const Carte &new_carte) {
    auto prev = *position;
    *position = new_carte;
    return prev;}

std::vector<Carte>::iterator Librarie::findCarte(const std::string &titlu) {
    return std::find_if(items.begin(), items.end(), [&titlu](const Carte &carte) {
        return carte.getTitlu() == titlu;
    });
}

bool Librarie::isValid(std::vector<Carte>::iterator &iterator) const {
    return iterator != items.end();
}

void Librarie::addCarte(const Carte &carte) {
    return items.push_back(carte);
}

std::vector<Carte> & Librarie::getCarti() {
    return items;
}

size_t Librarie::getLen() const {
    return items.size();
}

/// Cos
void Cos::deleteAllBooks() {
    items.clear();
}

void Cos::addCarte(const Carte &carte) {
    items.push_back(carte);
}

std::vector<Carte> &Cos::getCarti() {
    return items;
}

size_t Cos::getLen() const {
    return items.size();
}

bool Cos::isValid(std::vector<Carte>::iterator &iterator) const {
    return iterator != items.end();
}
