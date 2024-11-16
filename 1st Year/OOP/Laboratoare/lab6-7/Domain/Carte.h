#ifndef LAB6_7_CARTE_H
#define LAB6_7_CARTE_H

#include <string>

class Carte {
private:
    std::string titlu;
    std::string autor;
    std::string gen;
    int an;

public:
    /*
     * Constructor
     * @param titlu - titlul cartii
     * @param autor - autorul cartii
     * @param gen - genul cartii
     * @param an - anul aparitiei cartii
     */
    Carte(std::string titlu, std::string autor, std::string gen, int an);

    // Copy constructor
    Carte(const Carte &new_carte);

    // Destructor
    ~Carte() = default;


    /*
     * Converteste cartea in string
     * @return - stringul care contine informatiile despre carte
     * Exemplu: "Titlu: Ion, Autor: Liviu Rebreanu, Gen: Roman, An: 1920"
     */
    [[nodiscard]]std::string intoString() const;

    /*
     * Getter pentru titlu
     * @return - titlul cartii
     */
    [[nodiscard]] std::string getTitlu() const;

    /*
     * Getter pentru autor
     * @return - autorul cartii
     */
    [[nodiscard]] std::string getAutor() const;

    /*
     * Getter pentru gen
     * @return - genul cartii
     */
    [[nodiscard]] std::string getGen() const;

    /*
     * Getter pentru an
     * @return - anul aparitiei cartii
     */
    [[nodiscard]] int getAn() const;

    /*
     * Setter pentru titlu
     * @param new_titlu - noul titlu al cartii
     */
    void setTitlu(std::string &new_titlu);

    /*
     * Setter pentru autor
     * @param new_autor - noul autor al cartii
     */
    void setAutor(std::string &new_autor);

    /*
     * Setter pentru gen
     * @param new_gen - noul gen al cartii
     */
    void setGen(std::string &new_gen);

    /*
     * Setter pentru an
     * @param new_an - noul an al cartii
     */
    void setAn(int new_an);
};
#endif //LAB6_7_CARTE_H
