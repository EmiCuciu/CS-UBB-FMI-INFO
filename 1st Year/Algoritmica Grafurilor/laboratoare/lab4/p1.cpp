

#include "p1.h"


std::vector<int> PruferCode(std::vector<int>& parinte, int n) {
    std::vector<int> grad(n, 0); // Vector pentru a stoca gradul fiecarui nod
    std::set<int> frunze; // Set pentru a stoca frunzele arborelui
    std::vector<int> cod; // Vector pentru a stoca codul Prufer

    // Calculam gradul fiecarui nod
    for (int i = 0; i < n; ++i) {
        if (parinte[i] != -1) {
            grad[parinte[i]]++;
        }
    }

    // Adaugam frunzele in set
    for (int i = 0; i < n; ++i) {
        if (grad[i] == 0 && parinte[i] != -1) {
            frunze.insert(i);
        }
    }

    // Generam codul Prufer
    while (cod.size() <= n - 2) {
        int frunza = *frunze.begin();
        frunze.erase(frunze.begin());

        int p = parinte[frunza];
        cod.push_back(p);

        if (--grad[p] == 0 && parinte[p] != -1) {
            frunze.insert(p);
        }
    }

    return cod;
}

void p1(){
    std::ifstream in(R"(W:\Facultate S2\Algoritmica grafurilor\teme\lab4\p1_intrare.txt)");
    std::ofstream out(R"(W:\Facultate S2\Algoritmica grafurilor\teme\lab4\p1_iesire.txt)");

    int n;
    in >> n;

    std::vector<int> parinte;
    std::vector<int> cod;

    for (int i = 0; i < n; ++i) {
        int x;
        in >> x;
        parinte.push_back(x);
    }

    cod = PruferCode(parinte, n);

    out << cod.size() << "\n";
    for (const auto& x : cod) {
        out << x << " ";
    }

    in.close();
    out.close();
}