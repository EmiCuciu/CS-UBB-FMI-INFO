%{
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include "palindrom.tab.h" /* Include definitiile token-urilor generate de Bison */
%}

%option noyywrap

%%

[a-zA-Z] {
    /* Convertim la litera mica si salvam in yylval pentru a fi folosit in Bison */
    yylval = tolower(yytext[0]);
    return LITERA;
}

"#" {
    return MIJLOC;
}

. {

}

%%