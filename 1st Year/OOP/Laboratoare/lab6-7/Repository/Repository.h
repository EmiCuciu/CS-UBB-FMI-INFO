#ifndef LAB6_7_REPOSITORY_H
#define LAB6_7_REPOSITORY_H

#include "../Domain/Carte.h"
#include <vector>
#include <algorithm>


class Repository {
private:
    std::vector<Carte> carti; // vectorul de carti

public:
    // Constructor
    Repository() = default;

    // Destructor
    ~Repository() = default;

    // Copy constructor
    Repository(const Repository &other) = default;

    // Assignment operator
    Repository &operator=(const Repository &other) = default;

    // Move constructor
    Repository(Repository &&other) = default;

    // Move assignment operator
    Repository &operator=(Repository &&other) = default;

    /*
     * Returneaza toate cartile
     * @return - vectorul de carti
     */
    const std::vector<Carte> &getCarti();

    /*
     * Returneaza numarul de carti
     * @return - numarul de carti
     */
    size_t getSize();

    /*
     * Adauga o carte in lista de carti
     * @param carte - cartea care va fi adaugata
     */
    void addCarte(const Carte &carte);

    /*
     * Sterge o carte din lista de carti
     * @param carte - cartea care va fi stearsa
     */
    void deleteCarte(const std::vector<Carte>::iterator &position);

    /*
     * Modifica o carte din lista de carti
     * @param position - pozitia cartii care va fi modificata
     * @param new_carte - noua carte
     */
    static void modifyCarte(const std::vector<Carte>::iterator &position, const Carte &new_carte);

    /*
     * Cauta o carte dupa titlu
     * @param titlu - titlul cartii cautate
     * @return - Iterator catre cartea cautata
     */
    std::vector<Carte>::iterator findCarte(const std::string &titlu);

    /*
     * Valideaza positia iteratorului
     * @param position - pozitia iteratorului
     * @return - true daca iteratorul este valid, false altfel
     */
    bool validatePosition(const std::vector<Carte>::iterator &position);


};
#endif //LAB6_7_REPOSITORY_H
