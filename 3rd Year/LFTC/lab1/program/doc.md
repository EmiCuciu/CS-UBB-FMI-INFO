# Lab 1

---

### _**Enunt general**_:

Scrierea unui ANALIZOR LEXICAL pentru un minilimbaj de programare (MLP),
ales ca subset al unui limbaj existent.
---

## 1) Specificarea minilibajului

```
<program>            ::= <header><main_block>
<header>             ::= "#include<iostream>"
<main_block>         ::= "int main()" "{" <lista_instructiuni> "}"
<lista_instructiuni> ::= <instructiune> | <instructiune> <lista_instructiuni>
<instructiune>       ::= <declarare> ";" |   
                         <atribuire> ";" |
                         <instr_citire> ";"   |
                         <instr_iesire> ";"    |
                         <instr_if>   |
                         <instr_while> 
<declarare>          ::= <tip> ID 
<atribuire>          ::= ID "<-" <expresie>  
<instr_citire>       ::= "hear" ">>" ID 
<instr_iesire>       ::= "speak" "<<" ID 
<instr_if>           ::= "maybe" "(" <expresie> ")" "{" <lista_instructiuni> "}" "else" "{" <lista_instructiuni> "}"
<instr_while>        ::= "loop" "(" <expresie> ")" "{" <lista_instructiuni> "}"

<expresie>           ::= <operand> | <operand> <operatie> <expresie>
<operand>            ::= ID | CONST
<operatie>           ::= "+" | "-" | "*" | "/" | "%" | "<" | "<=" | ">" | ">=" | "==" | "!="
<tip>                ::= "int" | "double" | "float" | "string" | "word"

ID                   ::= <litera> | <litera> <cifra> | <litera> "_"
CONST                ::= <int_const> | <float_const> | <string_const>

<litera>             ::= "a" | ... | "z" | "A" | ... | "Z"
<cifra>              ::= "0" | ... | "9"

<int_const>          ::= <cifra> | <int_const>
<float_const>        ::= <int_const> "." <int_const>
<string_const>       ::= '"' <sir_caractere> '"'
<sir_caractere>      ::= <caracter> | <caracter> <sir_caractere>
<caracter>           ::= <litera> | <cifra> | " " | "." | ","
```

### Tipuri de date:

1. Integer: `int` numere intregi
2. Float: `float` numere reale

-definit de utilizator:

3. Word: un sir de litere, echivalent cu `string`

### Instructiuni:

1. Atribuire: `<-`
2. input/output
    1. Intrare: `hear` -- cin
    2. Iesire: `speak` -- cout
3. Selectie (conditionata)

```
maybe (conditie) {
    // instructiuni
} else {
    // instructiuni
}
```

4. Ciclare

```
loop (conditie) {
    // instructiuni
}
```

### Restrictii identificatori :

- pot contine: litere, cifre, underscore
- trebuie neaparat **_sa inceapa cu o litera_**
- nu pot fi identici cu cuvintele cheie (ex. int, for, return, class etc.)

## 2) Textele sursa a 3 mini-programe

### - calculeaza perimetrul si aria cercului pentru o raza data

limbajul original:

```
#include <iostream>

using namespace std;

float calculeaza_perimetrul(float raza)
{
    float pi = 3.14;
    float perimetrul = 2 * pi * raza;
    return perimetrul;
}

float calculeaza_aria(float raza)
{
    float pi = 3.14;
    float aria = pi * raza * raza;
    return aria;
}

int main()
{
    float raza;
    cout << "Introdu raza: ";
    cin >> raza;
    float rezultat = calculeaza_perimetrul(raza);
    cout << "Circumferinta = " << rezultat;
    rezultat = calculeaza_aria(raza);
    cout << "\nAria = " << rezultat;
    return 0;
}

```

MLP:

```
#include <iostream>

float calculeaza_perimetrul(float raza)
{
    float pi <- 3.14;
    float perimetrul <- 2 * pi * raza;
    return perimetrul;
}

float calculeaza_aria(float raza)
{
    float pi <- 3.14;
    float aria <- pi * raza * raza;
    return aria;
}

int main()
{
    float raza;
    speak << "Introdu raza: ";
    hear >> raza;
    float rezultat <- calculeaza_perimetrul(raza);
    speak << "Circumferinta = " << rezultat;
    rezultat <- calculeaza_aria(raza);
    speak << "\nAria = " << rezultat;
    return 0;
}

```

### - determina cmmdc a 2 numere naturale

limbajul original:

```
#include <iostream>

using namespace std;

int main()
{
    int a, b, r;
    cout << "Primul numar: ";
    cin >> a;
    cout << "Al doilea numar: ";
    cin >> b;
    while (b != 0)
    {
        r = a % b;
        a = b;
        b = r;
    }
    cout << "CMMDC : " << a;
    return 0;
}
```

MLP:

```
#include <iostream>

int main()
{
    int a, b, r;
    speak << "Primul numar: ";
    hear >> a;
    speak << "Al doilea numar: ";
    hear >> b;
    loop (b != 0)
    {
        r <- a % b;
        a <- b;
        b <- r;
    }
    speak << "CMMDC : " << a;
    return 0;
}

```

### - calculeaza suma a n numere citite de la tastatura

limbajul original:

```
#include <iostream>

using namespace std;

int main()
{
    int n, suma, x;
    suma = 0;
    cout << "Introdu n: ";
    cin >> n;

    while (n)
    {
        cin >> x;
        suma += x;
        n--;
    }

    cout << "Suma este: " << suma;
    return 0;
}
```

MLP:
```
#include <iostream>

int main()
{
    int n, suma, x;
    suma <- 0;
    speak << "Introdu n: ";
    hear >> n;

    loop (n != 0)
    {
        hear >> x;
        suma <- suma + x;
        n <- n - 1;
    }

    speak << "Suma este: " << suma;
    return 0;
}
```

## 3) 2 Texte sursa cu erori
A) primul program are erori atat in limbajul original cat si in MLP
```
#include <iostream>

using namespace std;

int main()
{
    integer n; // eroare
    cin >> n;      // hear >> n (in MLP)
    while (n != 0)      // loop (n != 0) (in MLP)
    {
        cout < "tema lftc \n";    //  speak < "tema lftc" (in MLP) | // eroare , trebuie << 
        n = n - 1;
    }

    return 0;
}
```

B) al doilea program are 2 erori in MLP, dar nu si in limbajul original

```
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
```