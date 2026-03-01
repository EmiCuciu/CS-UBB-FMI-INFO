#include <iostream>
#include <fstream>
#include <regex>
#include <unordered_set>
using namespace std;

unordered_set<string> keywords = {
    "int", "float", "word", "loop", "maybe", "else", "forloop", "hear", "speak", "return", "#include"
};
unordered_set<string> operators = {"+", "-", "*", "/", "%", "<", "<=", ">", ">=", "==", "!=", "<-", "<<", ">>"};
unordered_set<char> delimiters = {'(', ')', '{', '}', ';', ',', '[', ']'};

bool isIdentifier(const string& s)
{
    return !s.empty() && isalpha(s[0]);
}

bool isInteger(const string& s)
{
    return regex_match(s, regex("^[0-9]+$"));
}

bool isFloat(const string& s)
{
    return regex_match(s, regex("^[0-9]+\\.[0-9]+$"));
}

void printToken(const string& token, const string& type, int line)
{
    cout << token << "\t\t" << type << "\t\t" << line << "\n";
}

void analyzeLine(const string& line, int lineNumber)
{
    for (size_t i = 0; i < line.size();)
    {
        if (isspace(line[i]))
        {
            i++;
            continue;
        }

        if (line[i] == '"')
        {
            string str = "\"";
            i++;
            while (i < line.size() && line[i] != '"')
            {
                str += line[i++];
            }

            if (i < line.size())
            {
                str += '"';
                i++;
            }

            printToken(str, "CONST STRING", lineNumber);
            continue;
        }

        // Operator de 2 caractere (<<, >>, <=, >=, ==, !=, <-)
        if (i + 1 < line.size())
        {
            string two = line.substr(i, 2);
            if (operators.contains(two))
            {
                printToken(two, "OPERATOR", lineNumber);
                i += 2;
                continue;
            }
        }

        // Delimitator
        if (delimiters.contains(line[i]))
        {
            string d(1, line[i]); // string de lungime 1
            printToken(d, "DELIMITATOR", lineNumber);
            i++;
            continue;
        }

        // Token simplu (cuvânt, număr, etc.)
        string token;
        while (i < line.size() && !isspace(line[i]) && !delimiters.contains(line[i]) && line[i] != '"')
        {
            if (line.substr(i, 2) == "<<" || line.substr(i, 2) == ">>" || line.substr(i, 2) == "<-" ||
                line.substr(i, 2) == "<=" || line.substr(i, 2) == ">=" || line.substr(i, 2) == "==" ||
                line.substr(i, 2) == "!=")
                break;
            token += line[i++];
        }


        if (keywords.contains(token))
            printToken(token, "KEYWORD", lineNumber);
        else if (operators.contains(token))
            printToken(token, "OPERATOR", lineNumber);
        else if (isInteger(token))
            printToken(token, "CONST INT", lineNumber);
        else if (isFloat(token))
            printToken(token, "CONST FLOAT", lineNumber);
        else if (isIdentifier(token))
            printToken(token, "IDENTIFICATOR", lineNumber);
        else
            printToken(token, "LIBRARY", lineNumber);
    }
}

int main()
{
    ifstream fin("cod_example.txt");
    if (!fin.is_open())
    {
        cerr << "Eroare: nu pot deschide cod_example.txt\n";
        return 1;
    }

    string line;
    int lineNumber = 0;
    cout << "Token\t\tTip\t\tLinia\n";
    cout << "------------------------------------------\n";

    while (getline(fin, line))
    {
        analyzeLine(line, ++lineNumber);
    }

    fin.close();
    return 0;
}
