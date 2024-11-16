#include <stdio.h>

// Dacă utilizăm compilatorul C din Microsoft Visual Studio 2015, 2017, 2019
// este necesar să includem librăria legacy_stdio_definitions.lib la linkeditare
#pragma comment(lib, "legacy_stdio_definitions.lib")

// Această librărie este necesară doar pentru Microsoft Visual Studio 2019
// #pragma comment(lib, "legacy_stdio_wide_specifiers.lib")

// Forțăm linkeditorul să includă definiției funcției printf()
// în executabilul final, deoarece o vom apela din modulul ASM
#pragma comment(linker, "/INCLUDE:_printf")

// Declarație a funcției scrisă în limbaj de asamblare
extern int find_min(int* arr);

int main() {
    // Declarațiile și inițializările necesare
    int nr, val;
    int arr[25];

    // Citirea numărului de valori
    printf("Introdu numarul de valori: ");
    scanf("%d", &nr);

    // Citirea valorilor în array
    printf("Introdu valorile:\n");
    for (int i = 0; i < nr; i++) {
        printf("Valoare %d: ", i + 1);
        scanf("%d", &val);
        arr[i] = val;
    }

    printf("inainte \n");

    // Apelarea funcției find_min scrisă în limbaj de asamblare
    int min_val = find_min(arr);

    // Afișarea rezultatului
    printf("Valoarea minima este: %x\n", min_val);

    printf("dupa");

    return 0;
}
