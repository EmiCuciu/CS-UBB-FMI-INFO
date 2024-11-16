#include "Service.h"

Service::Service(Repository &repo): repo(repo) {}

const std::vector<Carte> &Service::getCarti() {
    return repo.getCarti();
}

void Service::addCarte(const std::string &titlu, const std::string &autor, const std::string &gen, int an) {
    auto position = repo.findCarte(titlu);
    if (repo.validatePosition(position)){
        return;
    }
    Carte new_carte(titlu, autor, gen, an);
    repo.addCarte(new_carte);
}

void Service::deleteCarte(const std::string &titlu) {
    auto position = repo.findCarte(titlu);
    if (!repo.validatePosition(position)){
        return;
    }
    repo.deleteCarte(position);
}

void Service::modifyCarte(const std::string &titlu, const std::string &new_autor, const std::string &new_gen, int new_an) {
    auto position = repo.findCarte(titlu);
    if (!repo.validatePosition(position)) {
        return;
    }
    Carte new_carte(titlu, new_autor, new_gen, new_an);
    repo.modifyCarte(position, new_carte);
}

std::vector<Carte> Service::findCarte(const std::string &titlu) {
    std::vector<Carte> output;

    for (const Carte &x: repo.getCarti()) {
        if (x.getTitlu().find(titlu) == 0) {
            output.push_back(x);
        }
    }
    return output;}
