#include <algorithm>
#include <fstream>
#include <iostream>
#include <vector>

using namespace std;

void read_number(const string& filename, vector<int>& number)
{
    ifstream in(filename);
    int n;
    in >> n;
    number.resize(n);
    for (int i = 0; i < n; i++)
    {
        in >> number[i];
    }
    in.close();

    reverse(number.begin(), number.end());
}

void write_number(const string& filename, const vector<int>& number)
{
    ofstream out(filename);
    out << number.size() << "\n";
    for (int i = number.size() - 1; i >= 0; --i)
    {
        out << number[i] << " ";
    }
    out.close();
}

bool compareFiles(const string &file1, const string &file2) {
    ifstream f1(file1), f2(file2);
    if (!f1.is_open() || !f2.is_open()) {
        cerr << "Eroare la deschiderea fisierelor \n";
        return false;
    }

    string line1, line2;

    vector<string> lines1, lines2;

    while (getline(f1, line1)) {
        line1.erase(line1.find_last_not_of(" \t\r\n") + 1);
        if (!line1.empty()) lines1.push_back(line1);
    }

    while (getline(f2, line2)) {
        line2.erase(line2.find_last_not_of(" \t\r\n") + 1);
        if (!line2.empty()) lines2.push_back(line2);
    }

    if (lines1.size() != lines2.size()) {
        cerr << "Fisierele au numere diferite de linii substantiale\n";
        return false;
    }

    for (size_t i = 0; i < lines1.size(); i++) {
        if (lines1[i] != lines2[i]) {
            cerr << "Diferenta la linia " << (i + 1) << "\n";
            return false;
        }
    }

    cout << "Sunt identice" << endl;

    return true;
}

void generateRandomNumber(int n, const string& filename) {
    ofstream file(filename);
    if (!file.is_open()) {
        cerr << "Eroare la deschiderea fișierului " << filename << endl;
        return;
    }

    vector<int> number(n);
    srand(time(nullptr));

    number[0] = rand() % 9 + 1;

    for (int i = 1; i < n; ++i) {
        number[i] = rand() % 10;
    }

    file << n << "\n";
    for (int digit : number) {
        file << digit << " ";
    }
    file << endl;

    file.close();
    cout << "Numărul aleatoriu de " << n << " cifre a fost salvat în " << filename << endl;
}


int main()
{
    // generateRandomNumber(100, "test3/N_1.txt");
    // generateRandomNumber(100000, "test3/N_2.txt");

    vector<int> N_1, N_2, N_3;

    //? Test1
    // read_number("test1/N_1.txt", N_1);
    // read_number("test1/N_2.txt", N_2);

    //? Test2
    read_number("test2/N_1.txt", N_1);
    read_number("test2/N_2.txt", N_2);

    //? Test3
    // read_number("test3/N_1.txt", N_1);
    // read_number("test3/N_2.txt", N_2);

    int n = max(N_1.size(), N_2.size());
    int carry = 0;
    int n1 = N_1.size();
    int n2 = N_2.size();

    for (int i = 0; i < n; i++)
    {
        int d1 = (i < n1) ? N_1[i] : 0;
        int d2 = (i < n2) ? N_2[i] : 0;
        int sum = d1 + d2 + carry;
        N_3.push_back(sum % 10);
        carry = sum / 10;
    }

    if (carry)
    {
        N_3.push_back(carry);
    }

    // write_number("test1/N_3.txt", N_3);
    write_number("test2/N_3.txt", N_3);
    // write_number("test3/N_3.txt", N_3);

    // compareFiles("test1/N_3.txt", "test1/RezultatCorect.txt");


    return 0;
}
