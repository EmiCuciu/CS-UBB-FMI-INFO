%{
/* 
* Lexical analyzer for the toy language 
*/
#include <string.h> /* for strcpy */

#include "attrib.h"
#include "micro.tab.h" /* token type definitions from .y file */

extern int lineno;  /* defined in micro.y */

%}

alpha   [A-Za-z]
digit   [0-9]
alphanum  [A-Za-z0-9]

%%
[ \t\r]        /*   ignore white spaces */
[\n]                    lineno++;
                       
"begin"                 return BEGINPAR;
"end"                   return ENDPAR;

"("                     return LPAREN;
")"                     return RPAREN;
":="                    return ASSIGN;
";"                     return SEMICOLON;
"+"                     return PLUS;
"."                     return DOT;

{alpha}{alphanum}*      {
                         strcpy(yylval.varname,yytext);
                         return IDENTIFIER;
                         }

{digit}+                {strcpy(yylval.varname,yytext);    
                        return CONST;
                        }



.                       { 
                        printf( "My: Illegal character %s on line #%d\n",
                                yytext,lineno);
                        
                        }

%%

int
yywrap() {
  return 1; /* tell scanner no further files need to be processed */
}

