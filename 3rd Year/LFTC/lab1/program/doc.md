# Lab 1

---

### _**Enunt general**_:

Scrierea unui ANALIZOR LEXICAL pentru un minilimbaj de programare (MLP),
ales ca subset al unui limbaj existent.
---

## 1) Specificarea minilibajului

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

### Restrictii identificatori si constante:

- pot contine: litere, cifre, underscore
- trebuie neaparat **_sa inceapa cu o litera_**
- nu pot fi identici cu cuvintele cheie (ex. int, for, return, class etc.)

## 2) Textele sursa a 3 mini-programe

### - calculeaza perimetrul si aria cercului pentru o raza data

limbajul original:

```
#include <iostream>

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
    std::cout << "Introdu raza: ";
    std::cin >> raza;
    float rezultat = calculeaza_perimetrul(raza);
    std::cout << "Circumferinta = " << rezultat;
    rezultat = calculeaza_aria(raza);
    std::cout << "\nAria = " << rezultat;
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

int main()
{
    int a, b, r;
    std::cout << "Primul numar: ";
    std::cin >> a;
    std::cout << "Al doilea numar: ";
    std::cin >> b;
    while (b != 0)
    {
        r = a % b;
        a = b;
        b = r;
    }
    std::cout << "CMMDC : " << a;
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
        r = a % b;
        a = b;
        b = r;
    }
    speak << "CMMDC : " << a;
    return 0;
}

```

### - calculeaza suma a n numere citite de la tastatura

limbajul original:

```
#include <iostream>

int main()
{
    int n, suma, x;
    suma = 0;
    std::cout << "Introdu n: ";
    std::cin >> n;

    while (n)
    {
        std::cin >> x;
        suma += x;
        n--;
    }

    std::cout << "Suma este: " << suma;
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

int main()
{
    integer n; // eroare
    std::cin >> n;      // hear >> n (in MLP)
    while (n != 0)      // loop (n != 0) (in MLP)
    {
        std::cout < "tema lftc \n";    //  speak < "tema lftc" (in MLP) | // eroare , trebuie << 
        n = n - 1;
    }

    return 0;
}
```

B) al doilea program are 2 erori in MLP, dar nu si in limbajul original

```
#include <iostream>

int main()
{
    std::string nume; // word nume;
    std::cin >> nume; // hear >> nume;
    std::cout << nume << std::endl; //  speak << nume << std::endl; | EROARE - in MLP nu exista endl, doar "\n"

    int a, b;
    std::cin >> a >> b; // hear >> a >> b;
    std::cout << a + b; // speak << a + b |  EROARE - in MLP nu se pot executa operatii in instructiunea de iesire ( ex. "a + b")

    return 0;
}
```