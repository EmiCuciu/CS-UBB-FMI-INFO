#ifndef DOMAIN_H
#define DOMAIN_H

#include <string>

class Produs {
private:
    int id;
    std::string nume;
    std::string tip;
    double pret;

public:
    Produs(int id, const std::string &nume, const std::string &tip, double pret);

    int getId() const;
    std::string getNume() const;
    std::string getTip() const;
    double getPret() const;
};

#endif // DOMAIN_H
