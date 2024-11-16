#include "Service.h"

ServiceException::ServiceException(const std::string &message) : message(message) {}

const char* ServiceException::what() const noexcept {
    return message.c_str();
}

Service::Service(const std::string &fisier) : repo(fisier) {
    // Initializeaza tipuriCount pe baza datelor existente
    for (const auto &produs : repo.getProduse()) {
        if (tipuriCount.find(produs.getTip()) != tipuriCount.end()) {
            tipuriCount[produs.getTip()]++;
        } else {
            tipuriCount[produs.getTip()] = 1;
        }
    }
}

void Service::validateProdus(const Produs &produs) {
    if (produs.getNume().empty()) {
        throw ServiceException("Numele produsului nu poate fi vid.");
    }

    if (produs.getPret() < 1.0 || produs.getPret() > 100.0) {
        throw ServiceException("Prețul produsului trebuie să fie între 1.0 și 100.0.");
    }

    for (const auto &p : repo.getProduse()) {
        if (p.getId() == produs.getId()) {
            throw ServiceException("Există deja un produs cu același ID.");
        }
    }
}

std::vector<Produs> Service::getProduse() const {
    return repo.getProduse();
}

std::vector<std::string> Service::getTipuri() const {
    std::vector<std::string> tipuri;
    for (const auto &pair : tipuriCount) {
        tipuri.push_back(pair.first);
    }
    return tipuri;
}

int Service::getNumarProduseTip(const std::string &tip) const {
    if (tipuriCount.find(tip) != tipuriCount.end()) {
        return tipuriCount.at(tip);
    }
    return 0;
}

void Service::adaugaProdus(int id, const std::string &nume, const std::string &tip, double pret) {
    Produs produs(id, nume, tip, pret);
    validateProdus(produs);

    repo.adaugaProdus(produs);

    // Actualizare număr de produse pentru tip
    if (tipuriCount.find(tip) != tipuriCount.end()) {
        tipuriCount[tip]++;
    } else {
        tipuriCount[tip] = 1;
    }
}
