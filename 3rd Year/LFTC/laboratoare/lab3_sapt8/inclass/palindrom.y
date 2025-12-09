%{
#include <stdio.h>
#include <stdlib.h>

extern int yylex();
extern int yyparse();
extern FILE* yyin;

void yyerror(const char* s);
%}

%token LITERA
%token MIJLOC

%%

propozitie:
      palindrom { printf("CORECT: Propozitia este un palindrom.\n"); }
    ;

palindrom:
      MIJLOC
      {
      }
    | LITERA palindrom LITERA
      {
        if ($1 != $3) {
            printf("GRESIT: Caracterul '%c' nu se potriveste cu '%c'\n", $1, $3);
            YYERROR;
        }
      }
    ;

%%

void yyerror(const char* s) {
    printf("Eroare: Propozitia NU este palindrom.\n");
}

int main(int argc, char** argv) {
    if (argc > 1) {
        yyin = fopen(argv[1], "r");
        if (!yyin) {
            perror("Eroare la deschiderea fisierului");
            return 1;
        }
    } else {
        printf("Introduceti propozitia (inclusiv # la mijloc):\n");
    }

    yyparse();
    return 0;
}