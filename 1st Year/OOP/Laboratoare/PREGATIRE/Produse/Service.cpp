#include "service.h"
#include <algorithm>

ServiceException::ServiceException(const std::string& msg) : msg(msg) {}

const char* ServiceException::what() const noexcept {
return msg.c_str();
}

Service::Service(Repository& repository) : repository(repository) {}

void Service::addProduct(int id, const std::string& nume, const std::string& tip, double pret) {
    auto pos = repository.findProduct(id);

    if (repository.validatePosition(pos)) {
        throw ServiceException("Produsul cu acest ID există deja!\n");
    }

    Product product(id, nume, tip, pret);
    Validari validari;
    validari.validateProduct(product);
    repository.addProduct(product);
}

void Service::deleteProduct(int id) {
    auto pos = repository.findProduct(id);

    if (!repository.validatePosition(pos)) {
        throw ServiceException("Nu există produs cu acest ID!\n");
    }

    repository.deleteProduct(id);
}

const std::vector<Product>& Service::getProducts() const {
    return repository.getProducts();
}

std::vector<Product> Service::sorteaza() const {
    auto products = repository.getProducts();
    std::sort(products.begin(), products.end(), [](const Product& a, const Product& b) {
        return a.getPret() < b.getPret();
    });
    return products;
}

std::unordered_map<std::string, int> Service::NumarTip() const {
    std::unordered_map<std::string, int> tip_count;

    for (const auto& product : repository.getProducts()) {
        tip_count[product.getTip()]++;
    }

    return tip_count;
}
