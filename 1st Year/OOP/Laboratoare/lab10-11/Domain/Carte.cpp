//
// Created by Emi on 4/14/2024.
//

#include "Carte.h"
#include <sstream>
#include <utility>
#include "iostream"

Carte::Carte(std::string titlu, std::string autor, std::string gen, int an)
        : titlu(std::move(titlu)), autor(std::move(autor)), gen(std::move(gen)), an(an) {}

Carte::Carte(const Carte &other) {
    this->titlu = other.titlu;
    this->autor = other.autor;
    this->gen = other.gen;
    this->an = other.an;
 }

std::string Carte::getTitlu() const {
    return titlu;
}

std::string Carte::getAutor() const {
    return autor;
}

std::string Carte::getGen() const {
    return gen;
}

int Carte::getAn() const {
    return an;
}

void Carte::setTitlu(const std::string &new_titlu) {
    this->titlu = new_titlu;
}

void Carte::setAutor(const std::string &new_autor) {
    this->autor = new_autor;
}

void Carte::setGen(const std::string &new_gen) {
    this->gen = new_gen;
}

void Carte::setAn(int new_an) {
    this->an = new_an;
}

std::string Carte::toString() const {
    std::stringstream str;
    str << titlu << "\t" << autor << "\t" << gen << "\t" << an;
    return str.str();
}