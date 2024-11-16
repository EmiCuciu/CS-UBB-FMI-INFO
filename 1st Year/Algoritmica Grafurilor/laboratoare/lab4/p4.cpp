
#include "p4.h"

Nod* construiesteArboreHuffman(const std::map<char, int>& frecvente) {
    std::priority_queue<Nod*, std::vector<Nod*>, Compara> coada;
    for (auto& p : frecvente) {
        coada.push(new Nod(p.first, p.second));
    }

    while (coada.size() > 1) {
        Nod* stanga = coada.top();
        coada.pop();
        Nod* dreapta = coada.top();
        coada.pop();
        Nod* combinat = new Nod('\0', stanga->frecventa + dreapta->frecventa, stanga, dreapta);
        coada.push(combinat);
    }

    return coada.top();
}

std::string decodeazaHuffman(Nod* radacina, const std::string& sirCodificat) {
    std::string textDecodificat;
    Nod* curent = radacina;
    for (char bit : sirCodificat) {
        if (bit == '0')
            curent = curent->stanga;
        else
            curent = curent->dreapta;

        if (!curent->stanga && !curent->dreapta) {
            textDecodificat += curent->caracter;
            curent = radacina;
        }
    }
    return textDecodificat;
}

void p4(){
    std::ifstream in(R"(W:\Facultate S2\Algoritmica grafurilor\teme\lab4\p4_intrare.txt)");
    std::ofstream out(R"(W:\Facultate S2\Algoritmica grafurilor\teme\lab4\p4_iesire.txt)");

    int N;
    in >> N;

    std::map<char, int> frecvente;
    char ch;
    int freq;
    for (int i = 0; i < N; ++i) {
        in >> ch >> freq;
        frecvente[ch] = freq;
    }

    std::string sirCodificat;
    in >> sirCodificat;
    in.close();

    Nod* radacina = construiesteArboreHuffman(frecvente);
    std::string sirDecodificat = decodeazaHuffman(radacina, sirCodificat);
    out << sirDecodificat << '\n';

}