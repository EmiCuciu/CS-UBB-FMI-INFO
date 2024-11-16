

#include "p3.h"
// Functia care codifica arborele Huffman
void codifica(Nod* radacina, const std::string& str,
              std::map<char, std::string>& huffman) {
    if (radacina == nullptr) return;

    if (!radacina->stanga && !radacina->dreapta) {
        huffman[radacina->caracter] = str;
    }

    codifica(radacina->stanga, str + "0", huffman);
    codifica(radacina->dreapta, str + "1", huffman);
}

// Functia care construieste arborele Huffman si il codifica
void Huffman(const std::string& text) {
    std::ofstream out(R"(W:\Facultate S2\Algoritmica grafurilor\teme\lab4\p3_iesire.txt)");
    std::map<char, int> frecventa_caracter;
    for (char caracter : text) {
        frecventa_caracter[caracter]++;
    }

    std::priority_queue<Nod*, std::vector<Nod*>, Compara> coada_prioritati;

    for (auto pereche : frecventa_caracter) {
        coada_prioritati.push(new Nod(pereche.first, pereche.second));
    }

    while (coada_prioritati.size() != 1) {
        Nod* stanga = coada_prioritati.top();
        coada_prioritati.pop();
        Nod* dreapta = coada_prioritati.top();
        coada_prioritati.pop();

        int suma = stanga->frecventa + dreapta->frecventa;
        coada_prioritati.push(new Nod('\0', suma, stanga, dreapta));
    }

    Nod* radacina = coada_prioritati.top();

    std::map<char, std::string> huffman;
    codifica(radacina, "", huffman);

    out << frecventa_caracter.size() << '\n';
    for (auto pereche : frecventa_caracter) {
        out << pereche.first << " " << pereche.second << '\n';
    }

    std::string sir_nou;
    for (char ch : text) {
        sir_nou += huffman[ch];
    }
    out << sir_nou << '\n';
}

void p3() {
    std::ifstream in(R"(W:\Facultate S2\Algoritmica grafurilor\teme\lab4\p3_intrare.txt)");

    std::stringstream ss;
    ss << in.rdbuf();
    std::string text = ss.str();
    in.close();

    Huffman(text);

}