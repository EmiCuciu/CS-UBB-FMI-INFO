#include "Carte.h"
#include <sstream>  /// std::stringstream, folosit pentru a converti int in string
#include <iostream>

Carte::Carte(std::string titlu, std::string autor, std::string gen, int an) : titlu(std::move(titlu)), autor(std::move(autor)), gen(std::move(gen)), an(an) {}

Carte::Carte(const Carte &new_carte): titlu(new_carte.titlu), autor(new_carte.autor), gen(new_carte.gen), an(new_carte.an) {
    std::cout<<"Copy\n";
}

std::string Carte::intoString() const {
    std::stringstream string_stream;
    string_stream << titlu << "\t" << autor << "\t" << gen << "\t" << an;
    return string_stream.str();
}

// Getter pentru titlu
std::string Carte::getTitlu() const {
    return titlu;
}

// Getter pentru autor
std::string Carte::getAutor() const {
    return autor;
}

// Getter pentru gen
std::string Carte::getGen() const {
    return gen;
}

// Getter pentru an
int Carte::getAn() const {
    return an;
}

// Setter pentru titlu
void Carte::setTitlu(std::string &new_titlu) {
    this->titlu = new_titlu;
}

// Setter pentru autor
void Carte::setAutor(std::string &new_autor) {
    this->autor = new_autor;
}

// Setter pentru gen
void Carte::setGen(std::string &new_gen) {
    this->gen = new_gen;
}

// Setter pentru an
void Carte::setAn(int new_an) {
    this->an = new_an;
}