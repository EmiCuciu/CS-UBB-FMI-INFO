#include "domain.h"

Produs::Produs(int id, const std::string &nume, const std::string &tip, double pret)
        : id(id), nume(nume), tip(tip), pret(pret) {}

int Produs::getId() const {
    return id;
}

std::string Produs::getNume() const {
    return nume;
}

std::string Produs::getTip() const {
    return tip;
}

double Produs::getPret() const {
    return pret;
}
