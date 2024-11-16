

#include "p5.h"

auto sorteazaArbore(std::vector<std::pair<std::pair<int, int>, int>>& arbore) {
    sort(arbore.begin(), arbore.end(),
         [](std::pair<std::pair<int, int>, int> element_1,
            std::pair<std::pair<int, int>, int> element_2) {
             return element_1.second < element_2.second;
         });
    return arbore;
}

auto Kruskal(std::vector<std::pair<std::pair<int, int>, int>>& arbore, int src) {
    std::vector<std::pair<std::pair<int, int>, int>> T;
    UniuneCautare uniune_cautare(src);
    for (const auto& t : arbore) {
        int nod1 = t.first.first;
        int nod2 = t.first.second;
        if (uniune_cautare.gasesteNod(nod1) != uniune_cautare.gasesteNod(nod2)) {
            uniune_cautare.unesteNoduri(nod2, nod1);
            auto m = make_pair(t.first, t.second);
            T.push_back(m);
            if (T.size() == src - 1) {
                break;
            }
        }
    }
    return T;
}

void p5() {
    std ::ifstream in(R"(W:\Facultate S2\Algoritmica grafurilor\teme\lab4\p5_intrare.txt)");
    std::ofstream out(R"(W:\Facultate S2\Algoritmica grafurilor\teme\lab4\p5_iesire.txt)");
    std::vector<std::pair<std::pair<int, int>, int>> arbore;

    int v, e;
    in >> v >> e;
    for (int i = 0; i < e; ++i) {
        int x, y, w;
        in >> x >> y >> w;
        auto pereche = std::make_pair(x, y);
        auto element = make_pair(pereche, w);
        arbore.push_back(element);
    }

    auto arb_sortat = sorteazaArbore(arbore);
    auto T = Kruskal(arb_sortat, v);

    int suma = 0;
    for (const auto& t : T) {
        suma += t.second;
    }

    out << suma << "\n";
    out << T.size() << "\n";
    for (const auto& t : T) {
        out << t.first.first << " " << t.first.second << " " << "\n";
    }

    in.close();
    out.close();
}