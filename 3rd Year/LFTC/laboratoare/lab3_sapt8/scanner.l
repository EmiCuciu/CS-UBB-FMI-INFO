/* Definitii si optiuni FLEX*/
%{
#include <iostream>
#include <string>
#include <vector>
#include "HashSymbolTable.h"
#include "parser.tab.h"

extern HashSymbolTable ts;
extern std::vector<std::pair<int,int>> fip;

void add_to_fip(int code, int ts_pos = -1) {
    fip.push_back({code, ts_pos});
}
%}

%option noyywrap
%option yylineno

%%

"#include"  { add_to_fip(INCLUDE); return INCLUDE; }
"main"      { add_to_fip(MAIN); return MAIN; }
"int"       { add_to_fip(INT); return INT; }
"double"    { add_to_fip(DOUBLE); return DOUBLE; }
"float"     { add_to_fip(FLOAT); return FLOAT; }
"string"    { add_to_fip(STRING); return STRING; }
"word"      { add_to_fip(WORD); return WORD; }
"maybe"     { add_to_fip(MAYBE); return MAYBE; }
"else"      { add_to_fip(ELSE); return ELSE; }
"loop"      { add_to_fip(LOOP); return LOOP; }
"forloop"   { add_to_fip(FORLOOP); return FORLOOP; }
"hear"      { add_to_fip(HEAR); return HEAR; }
"speak"     { add_to_fip(SPEAK); return SPEAK; }
"return"    { add_to_fip(RETURN); return RETURN; }
"cattimp"   { add_to_fip(CATTIMP); return CATTIMP; }
"executa"   { add_to_fip(EXECUTA); return EXECUTA; }
"sfcattimp" { add_to_fip(SFCATTIMP); return SFCATTIMP; }

"<"[^\n>]+">" {
                int pos = ts.insert(yytext, "LIBRARY");
                add_to_fip(LIBRARY, pos);
                return LIBRARY;
            }

"<-"        { add_to_fip(OP_ASSIGN); return OP_ASSIGN; }
"<<"        { add_to_fip(OP_OUT); return OP_OUT; }
">>"        { add_to_fip(OP_IN); return OP_IN; }
"<="        { add_to_fip(OP_LTE); return OP_LTE; }
">="        { add_to_fip(OP_GTE); return OP_GTE; }
"=="        { add_to_fip(OP_EQ); return OP_EQ; }
"!="        { add_to_fip(OP_NE); return OP_NE; }
"+"         { add_to_fip(OP_ADD); return OP_ADD; }
"-"         { add_to_fip(OP_SUB); return OP_SUB; }
"*"         { add_to_fip(OP_MUL); return OP_MUL; }
"/"         { add_to_fip(OP_DIV); return OP_DIV; }
"%"         { add_to_fip(OP_MOD); return OP_MOD; }
"<"         { add_to_fip(OP_LT); return OP_LT; }
">"         { add_to_fip(OP_GT); return OP_GT; }
"("         { add_to_fip(LPARANTEZ); return LPARANTEZ; }
")"         { add_to_fip(RPARANTEZ); return RPARANTEZ; }
"{"         { add_to_fip(LBRACKET); return LBRACKET; }
"}"         { add_to_fip(RBRACKET); return RBRACKET; }
";"         { add_to_fip(SEMICOLON); return SEMICOLON; }
","         { add_to_fip(COMMA); return COMMA; }
"["         { add_to_fip(SQ_LBRACKET); return SQ_LBRACKET; }
"]"         { add_to_fip(SQ_RBRACKET); return SQ_RBRACKET; }

[a-zA-Z][a-zA-Z0-9_]* {
    int pos = ts.insert(yytext, "ID");
    add_to_fip(ID, pos);
    return ID;
}

(0[xX][0-9a-fA-F]+)|(0[bB][01]+)|(0[0-7]+)|([1-9][0-9]*)|0 {
    int pos = ts.insert(yytext, "CONST_INT");
    add_to_fip(CONST_INT, pos);
    return CONST_INT;
}

[0-9]+\.[0-9]+\.[0-9a-zA-Z._]* {
    fprintf(stderr, "Eroare Lexicala [Linia %d]: Constanta float invalida: %s\n", yylineno, yytext);
}

[0-9]+\.[0-9]+ {
    int pos = ts.insert(yytext, "CONST_FLOAT");
    add_to_fip(CONST_FLOAT, pos);
    return CONST_FLOAT;
}

\"(\\.|[^\n\"])*\" {
    int pos = ts.insert(yytext, "CONST_STRING");
    add_to_fip(CONST_STRING, pos);
    return CONST_STRING;
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