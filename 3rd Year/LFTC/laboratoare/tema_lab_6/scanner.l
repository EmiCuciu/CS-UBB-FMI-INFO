%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "translator.tab.h"
%}

%option noyywrap
%option yylineno

%%

"#include"([ ]*)"<iostream>" { /* Ignora header */ }
"using namespace std;"        { /* Ignora namespace */ }
"int"                         { return INT; }
"main"                        { return MAIN; }
"return"                      { return RETURN; }

"hear"                        { return HEAR; }
"speak"                       { return SPEAK; }

"<-"                          { return ASSIGN; }
">>"                          { return OP_IN; }
"<<"                          { return OP_OUT; }
"+"                           { return PLUS; }
"-"                           { return MINUS; }
"*"                           { return MULT; }
"/"                           { return DIV; }

"("                           { return LPAREN; }
")"                           { return RPAREN; }
"{"                           { return LBRACE; }
"}"                           { return RBRACE; }
";"                           { return SEMICOLON; }

[a-zA-Z_][a-zA-Z0-9_]* {
    yylval.str_val = strdup(yytext);
    return ID;
}

[0-9]+ {
    yylval.str_val = strdup(yytext);
    return CONST_INT;
}

[ \t\r\n]+    { /* Ignora spatii */ }
.             { fprintf(stderr, "Eroare lexicala: %s la linia %d\n", yytext, yylineno); }

%%