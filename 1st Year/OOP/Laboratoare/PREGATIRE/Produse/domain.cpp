#include "domain.h"

Product::Product(int id, const std::string& nume, const std::string& tip, double pret)
        : id(id), nume(nume), tip(tip), pret(pret) {}

int Product::getId() const {
    return id;
}

const std::string& Product::getNume() const {
    return nume;
}

const std::string& Product::getTip() const {
    return tip;
}

double Product::getPret() const {
    return pret;
}

int Product::numarVocale() const {
    int numar = 0;
    for (char ch : nume) {
        if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u' ||
            ch == 'A' || ch == 'E' || ch == 'I' || ch == 'O' || ch == 'U') {
            numar++;
        }
    }
    return numar;
}
