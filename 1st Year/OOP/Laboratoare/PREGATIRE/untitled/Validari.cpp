//
// Created by Emi on 5/22/2024.
//

#include "Validari.h"
#include "vector"

bool Validari::validateID(int id) {
    return id>0;
}

bool Validari::validateVG(const std::string& ver_gresita) {
    return !ver_gresita.empty();
}

bool Validari::validateVC(const std::string& ver_corecta) {
    return !ver_corecta.empty();
}

bool Validari::validateTip(const std::string& tip) {
    std::vector<std::string> tipuri;
    tipuri.emplace_back("typo");
    tipuri.emplace_back("grammar");
    tipuri.emplace_back("incorrect tense");

    for(auto item : tipuri){
        if((item == "typo")||(item == "grammar")||(item == "incorrect tense")){
            return true;
        }
    }
    return false;
}

bool Validari::validateSeverity(std::string severity) {
    std::vector<std::string> severitati;
    severitati.emplace_back("minors");
    severitati.emplace_back("mediums");
    severitati.emplace_back("majors");

    for(auto item : severitati){
        if((item == "minors")||(item == "mediums")||(item == "majors")){
            return true;
        }
    }
    return false;
}

void Validari::validateGreseala(Greseala greseala) {
    std::string errors;

    if(!validateID(greseala.getID())){
        errors.append("ID invalid\n");
    }

    if(!validateVG(greseala.getVG())){
        errors.append("Greseala gresita invalida\n");
    }

    if(!validateVC(greseala.getVC())){
        errors.append("Greseala corecta invalida\n");
    }

    if(!validateTip(greseala.getTIP())){
        errors.append("Tip invalid\n");
    }

    if(!validateSeverity(greseala.getSeverity())){
        errors.append("SEveritatte invalid\n");
    }

    if(!errors.empty()){
        throw ValidariException(errors);
    }

}
