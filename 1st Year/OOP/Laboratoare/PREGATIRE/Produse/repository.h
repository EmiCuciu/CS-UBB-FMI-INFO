#ifndef REPOSITORY_H
#define REPOSITORY_H

#include "domain.h"
#include <vector>
#include <string>

class Repository {
private:
    std::vector<Product> products;
    std::string file_name;

public:
    explicit Repository(const std::string& file_name);

    void writeToFile();
    void loadFromFile();

    void addProduct(const Product& product);
    void deleteProduct(int id);

    const std::vector<Product>& getProducts() const;
    size_t getSize() const;
    std::vector<Product>::iterator findProduct(int id);
    bool validatePosition(std::vector<Product>::iterator pos);
};

#endif // REPOSITORY_H
