/* Definitii si optiuni FLEX*/
%{
#include <iostream>
#include <string>
#include <vector>
#include "HashSymbolTable.h"
#include "token.h"


extern HashSymbolTable ts;
extern std::vector<std::pair<int,int>> fip;

extern int yylineno;    // current line number


void add_to_fip(int code, int ts_pos = -1) {
    // -1 : poz default pentru tokenii care nu apar in TS
    fip.push_back({code, ts_pos});
}
%}


/* yywrap() : Flex apeleaza automat cand ajunge EOF. return 1 => succes, return => exista alte fisiere de procesat (scanner-ul continua)*/

/* Ne spune sa nu cautam functia yywrap() la final */
%option noyywrap

/* Flex va urmari automat numarul liniei in 'yylineno' */
%option yylineno

%%


"#include"  { add_to_fip(INCLUDE); }
"main"      { add_to_fip(MAIN); }
"int"       { add_to_fip(INT); }
"double"    { add_to_fip(DOUBLE); }
"float"     { add_to_fip(FLOAT); }
"string"    { add_to_fip(STRING); }
"word"      { add_to_fip(WORD); }
"maybe"     { add_to_fip(MAYBE); }
"else"      { add_to_fip(ELSE); }
"loop"      { add_to_fip(LOOP); }
"forloop"   { add_to_fip(FORLOOP); }
"hear"      { add_to_fip(HEAR); }
"speak"     { add_to_fip(SPEAK); }
"return"    { add_to_fip(RETURN); }

"<"[^>]+">" {
                int pos = ts.insert(yytext, "LIBRARY");
                add_to_fip(LIBRARY, pos);
            }

"<-"        { add_to_fip(OP_ASSIGN); }
"<<"        { add_to_fip(OP_OUT); }
">>"        { add_to_fip(OP_IN); }
"<="        { add_to_fip(OP_LTE); }
">="        { add_to_fip(OP_GTE); }
"=="        { add_to_fip(OP_EQ); }
"!="        { add_to_fip(OP_NE); }
"+"         { add_to_fip(OP_ADD); }
"-"         { add_to_fip(OP_SUB); }
"*"         { add_to_fip(OP_MUL); }
"/"         { add_to_fip(OP_DIV); }
"%"         { add_to_fip(OP_MOD); }
"<"         { add_to_fip(OP_LT); }
">"         { add_to_fip(OP_GT); }
"("         { add_to_fip(LPARANTEZ); }
")"         { add_to_fip(RPARANTEZ); }
"{"         { add_to_fip(LBRACKET); }
"}"         { add_to_fip(RBRACKET); }
";"         { add_to_fip(SEMICOLON); }
","         { add_to_fip(COMMA); }
"["         { add_to_fip(SQ_LBRACKET); }
"]"         { add_to_fip(SQ_RBRACKET); }



[a-zA-Z][a-zA-Z0-9_]* {
    int pos = ts.insert(yytext, "ID");
    add_to_fip(ID, pos);
}



(0[xX][0-9a-fA-F]+)|(0[bB][01]+)|(0[0-7]+)|([1-9][0-9]*)|0 {
    int pos = ts.insert(yytext, "CONST_INT");
    add_to_fip(CONST_INT, pos);
}



[0-9]+\.[0-9]+\.[0-9a-zA-Z._]* {
    fprintf(stderr, "Eroare Lexicala [Linia %d]: Constanta float invalida: %s\n", yylineno, yytext);
}

[0-9]+\.[0-9]+ {
    int pos = ts.insert(yytext, "CONST_FLOAT");
    add_to_fip(CONST_FLOAT, pos);
}



\"(\\.|[^\n\"])*\" {
    int pos = ts.insert(yytext, "CONST_STRING");
    add_to_fip(CONST_STRING, pos);
}

\"(\\.|[^\n\"])* {
    fprintf(stderr, "Eroare Lexicala [Linia %d]: String neterminat.\n", yylineno);
}

[ \t]+ {

}

\n {

}


.   {
    fprintf(stderr, "Eroare Lexicala [Linia %d]: Caracter necunoscut: %s\n", yylineno, yytext);
}

%%

HashSymbolTable ts;
std::vector<std::pair<int, int>>fip;