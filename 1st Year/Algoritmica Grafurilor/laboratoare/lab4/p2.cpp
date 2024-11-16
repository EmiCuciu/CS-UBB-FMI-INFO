

#include "p2.h"


std::vector<int> PruferDecode(std::vector<int>& cod, int n) {
    n++;
    std::vector<int> parinte(n, -1); // Vector pentru a stoca parintii nodurilor
    std::vector<int> grad(n, 1); // Vector pentru a stoca gradul fiecarui nod

    // Calculam gradul fiecarui nod
    for (int nod : cod) {
        grad[nod]++;
    }

    int ptr = 0;
    while (grad[ptr] != 1) ptr++;
    int frunza = ptr;

    // Decodam codul Prufer
    for (int nod : cod) {
        parinte[frunza] = nod;
        grad[nod]--;
        grad[frunza]--;

        if (grad[nod] == 1 && nod < ptr) {
            frunza = nod;
        } else {
            ptr++;
            while (grad[ptr] != 1) ptr++;
            frunza = ptr;
        }
    }
    parinte[frunza] = -1;
    return parinte;
}

void p2() {
    std ::ifstream in(R"(W:\Facultate S2\Algoritmica grafurilor\teme\lab4\p2_intrare.txt)");
    std ::ofstream out(R"(W:\Facultate S2\Algoritmica grafurilor\teme\lab4\p2_iesire.txt)");
    std ::vector<int> v;

    int n;
    in >> n;

    for (int i = 0; i < n; ++i) {
        int x;
        in >> x;
        v.push_back(x);
    }

    auto tree = PruferDecode(v, n);

    out << tree.size() << "\n";
    for (auto t : tree) {
        out << t << " ";
    }
    out << "\n";

    in.close();
    out.close();
}