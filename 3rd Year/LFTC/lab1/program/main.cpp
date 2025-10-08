#include <iostream>

int main()
{
    std::string nume; // word nume;
    std::cin >> nume; // hear >> nume;
    std::cout << nume << std::endl; //  speak << nume << std::endl; | EROARE - in MLP nu exista endl, doar "\n"

    int a, b;
    std::cin >> a >> b; // hear >> a >> b;
    std::cout << a + b; // speak << a + b |  EROARE - in MLP nu se pot executa operatii in instructiunea de iesire ( ex. "a + b")

    return 0;
}
