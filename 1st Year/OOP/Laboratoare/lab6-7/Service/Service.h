#ifndef LAB6_7_SERVICE_H
#define LAB6_7_SERVICE_H

#include "../Repository/Repository.h"

class Service {
private:
    Repository &repo;  // referinta catre repository

public:
    /*
     * Constructor
     * @param repo - referinta catre repository
     */
    explicit Service(Repository &repo);

    /*
     * Destructor
     */
    ~Service() = default;

    /*
     * Returneaza toate cartile din repository
     * @return - vectorul de carti din repository
     */
    const std::vector<Carte> &getCarti();

    /*
     * Adauga o carte in repository
     * @param titlu - titlul cartii
     * @param autor - autorul cartii
     * @param gen - genul cartii
     * @param an - anul aparitiei cartii
     */
    void addCarte(const std::string &titlu, const std::string &autor, const std::string &gen, int an);

    /*
     * Sterge o carte din repository
     * @param titlu - titlul cartii
     */
    void deleteCarte(const std::string &titlu);

    /*
     * Modifica o carte din repository
     * @param titlu - titlul cartii
     * @param new_titlu - noul titlu
     * @param new_autor - noul autor
     * @param new_gen - noul gen
     * @param new_an - noul an
     */
    void modifyCarte(const std::string &titlu, const std::string &new_autor, const std::string &new_gen, int new_an);

    /*
     * Cauta o carte dupa titlu in repository
     * @param titlu - titlul cartii
     * @return - cartile cu titlul dat
     */
    std::vector<Carte> findCarte(const std::string &titlu);

};

#endif //LAB6_7_SERVICE_H
