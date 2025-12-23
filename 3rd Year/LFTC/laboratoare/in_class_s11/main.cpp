#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <set>

using namespace std;

struct Regula {
    char stanga;
    string dreapta;
};

class Gramatica {
    vector<Regula> reguli;
    set<char> neterminale;
    set<char> terminale;

public:
    void readFromFile(const string& fisier) {
        ifstream f(fisier);

        char stanga;
        string sageata, dreapta;

        while (f >> stanga >> sageata >> dreapta) {
            reguli.push_back({stanga, dreapta});
            neterminale.insert(stanga);
        }
        f.close();

        for (const auto& r : reguli) {
            for (char c : r.dreapta) {
                if (!neterminale.contains(c) && c != '|') {
                    terminale.insert(c);
                }
            }
        }
    }

    void afiseazaReguliCuTerminali(char cautat) const
    {
        if (!terminale.contains(cautat)) {
            cout << "Atentie: '" << cautat << "' nu este un terminal cunoscut!" << endl;
            return;
        }

        cout << "Reguli care contin '" << cautat << "':" << endl;
        bool gasit = false;

        for (const auto& r : reguli) {
            bool exista = false;
            for (char c : r.dreapta) {
                if (c == cautat) {
                    exista = true;
                    break;
                }
            }

            if (exista) {
                cout << r.stanga << " -> " << r.dreapta << endl;
                gasit = true;
            }
        }

        if (!gasit)
            cout << "(Nicio regula gasita)" << endl;
    }

    void afiseazaTerminale() const
    {
        cout << "Terminale: ";
        for (char t : terminale) cout << t << " ";
        cout << endl;
    }
};

int main() {
    Gramatica g;
    g.readFromFile("gramatica.txt");
    g.afiseazaTerminale();

    char c;
    cout << "\nTerminal cautat: ";
    cin >> c;

    g.afiseazaReguliCuTerminali(c);

    return 0;
}