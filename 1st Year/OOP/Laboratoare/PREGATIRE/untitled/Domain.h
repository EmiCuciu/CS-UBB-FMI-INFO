//
// Created by Emi on 5/22/2024.
//

#ifndef UNTITLED_DOMAIN_H
#define UNTITLED_DOMAIN_H

#include "string"

class Greseala {
private:
    int id;
    std::string versiune_gresita;
    std::string versiune_corecta;
    std::string tip;
    std::string severity;

public:
    Greseala(int id, std::string versiune_gresita, std::string versiune_corecta, std::string tip, std::string severity)
            : id(id), versiune_gresita(versiune_gresita), versiune_corecta(versiune_corecta), tip(tip),
              severity(severity) {}

    int getID();

    std::string getVC();

    std::string getVG();

    std::string getTIP();

    std::string getSeverity();
};

#endif //UNTITLED_DOMAIN_H
