#include "repository.h"
#include <fstream>
#include <sstream>
#include <algorithm>

Repository::Repository(const std::string& file_name) : file_name(file_name) {
    loadFromFile();
}

void Repository::writeToFile() {
    std::ofstream out(file_name);
    for (const auto& product : products) {
        out << product.getId() << "," << product.getNume() << "," << product.getTip() << "," << product.getPret() << "\n";
    }
    out.close();
}

void Repository::loadFromFile() {
    products.clear();
    std::ifstream in(file_name);
    std::string line;
    while (std::getline(in, line)) {
        std::stringstream ss(line);
        std::string token;
        std::vector<std::string> split;
        while (std::getline(ss, token, ',')) {
            split.push_back(token);
        }
        if (split.size() == 4) {
            int id = std::stoi(split[0]);
            std::string nume = split[1];
            std::string tip = split[2];
            double pret = std::stod(split[3]);
            Product product(id, nume, tip, pret);
            products.push_back(product);
        }
    }
    in.close();
}

void Repository::addProduct(const Product& product) {
    products.push_back(product);
    writeToFile();
}

void Repository::deleteProduct(int id) {
    auto pos = findProduct(id);
    if (validatePosition(pos)) {
        products.erase(pos);
        writeToFile();
    }
}

const std::vector<Product>& Repository::getProducts() const {
    return products;
}

size_t Repository::getSize() const {
    return products.size();
}

std::vector<Product>::iterator Repository::findProduct(int id) {
    return std::find_if(products.begin(), products.end(), [&](const Product& product) {
        return product.getId() == id;
    });
}

bool Repository::validatePosition(std::vector<Product>::iterator pos) {
    return pos != products.end();
}
