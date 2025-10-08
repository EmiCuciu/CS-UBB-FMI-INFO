#include <iostream>

using namespace std;

int main()
{
    string nume; // word nume;
    cin >> nume; // hear >> nume;
    cout << nume << std::endl; //  speak << nume << std::endl; | EROARE - in MLP nu exista endl, doar "\n"

    int a, b;
    cin >> a >> b; // hear >> a >> b;
    cout << a + b; // speak << a + b |  EROARE - in MLP nu se pot executa operatii in instructiunea de iesire ( ex. "a + b")

    return 0;
}
