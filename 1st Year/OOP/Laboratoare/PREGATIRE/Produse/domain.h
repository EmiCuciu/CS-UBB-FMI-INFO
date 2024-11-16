#ifndef DOMAIN_H
#define DOMAIN_H

#include <string>

class Product {
private:
    int id;
    std::string nume;
    std::string tip;
    double pret;

public:
    Product(int id, const std::string& nume, const std::string& tip, double pret);

    int getId() const;
    const std::string& getNume() const;
    const std::string& getTip() const;
    double getPret() const;
    int numarVocale() const; // Metoda pentru numărul de vocale din numele produsului
};

#endif // DOMAIN_H
