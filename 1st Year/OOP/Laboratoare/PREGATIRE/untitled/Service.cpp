//
// Created by Emi on 5/22/2024.
//

#include <algorithm>
#include "Service.h"


void Service::addService(int id,std::string& vg,  std::string& vc,  std::string& tip,  std::string& severity) {
    auto pos = repository.findGreseala(id);

    if(repository.validatePosition(pos))
        throw ServiceException("Greseala exista deja");

    Greseala greseala(id,vg,vc,tip,severity);
    Validari validari;
    validari.validateGreseala(greseala);
    repository.addGreseala(greseala);
}

void Service::deleteService(int id) {
    auto pos = repository.findGreseala(id);

    if(!repository.validatePosition(pos)){
        throw ServiceException("Nu exista greseala cu acest id");
    }

    repository.deleteGreseala(pos);
}

std::vector<Greseala> Service::getService() {
    return repository.getGreseli();
}

std::vector<Greseala> Service::sorteaza() {
    auto vector = repository.getGreseli();

    std::sort(vector.begin(),vector.end(),[](Greseala &greseala1,Greseala &greseala2){
        return greseala1.getSeverity()>greseala2.getSeverity();
    });
    return vector;
}
