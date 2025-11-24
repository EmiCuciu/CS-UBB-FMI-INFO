%{
#include <iostream>
#include <string>
#include <vector>
#include <fstream>
#include "HashSymbolTable.h"

extern int yylex();
extern int yylineno;
extern char* yytext;
extern HashSymbolTable ts;
extern std::vector<std::pair<int, int>> fip;

void yyerror(const char *s);
%}

%token ID CONST_INT CONST_FLOAT CONST_STRING
%token INCLUDE LIBRARY MAIN
%token INT FLOAT DOUBLE STRING WORD
%token MAYBE ELSE LOOP FORLOOP HEAR SPEAK RETURN
%token CATTIMP EXECUTA SFCATTIMP

%token OP_ASSIGN OP_OUT OP_IN
%token LPARANTEZ RPARANTEZ LBRACKET RBRACKET SQ_LBRACKET SQ_RBRACKET
%token SEMICOLON COMMA

%left OP_EQ OP_NE
%left OP_LT OP_LTE OP_GT OP_GTE
%left OP_ADD OP_SUB
%left OP_MUL OP_DIV OP_MOD

%start program

%%


program : header main_block
        ;

header : INCLUDE LIBRARY
       ;

main_block : INT MAIN LPARANTEZ RPARANTEZ LBRACKET lista_instructiuni RBRACKET
           ;

lista_instructiuni : instructiune
                   | instructiune lista_instructiuni
                   ;

instructiune : declarare SEMICOLON
             | atribuire SEMICOLON
             | instr_citire SEMICOLON
             | instr_iesire SEMICOLON
             | instr_if
             | instr_while
             | instr_for
             | instr_mycattimp
             | instr_return
             ;

declarare : tip ID
          | tip ID SQ_LBRACKET SQ_RBRACKET
          ;

tip : INT | FLOAT | DOUBLE | STRING | WORD ;

atribuire : ID OP_ASSIGN expresie
          | ID SQ_LBRACKET CONST_INT SQ_RBRACKET OP_ASSIGN expresie
          ;

instr_citire : HEAR OP_IN ID
             ;

instr_iesire : SPEAK OP_OUT expresie
             | SPEAK OP_OUT CONST_STRING
             ;

instr_if : MAYBE LPARANTEZ expresie RPARANTEZ LBRACKET lista_instructiuni RBRACKET
         | MAYBE LPARANTEZ expresie RPARANTEZ LBRACKET lista_instructiuni RBRACKET ELSE LBRACKET lista_instructiuni RBRACKET
         ;

instr_while : LOOP LPARANTEZ expresie RPARANTEZ LBRACKET lista_instructiuni RBRACKET
            ;

instr_for : FORLOOP LPARANTEZ expresie RPARANTEZ LBRACKET lista_instructiuni RBRACKET
          ;

instr_mycattimp : CATTIMP LPARANTEZ expresie RPARANTEZ EXECUTA LBRACKET lista_instructiuni RBRACKET SFCATTIMP
                ;

instr_return : RETURN expresie SEMICOLON
             ;

expresie : operand
         | expresie OP_ADD expresie
         | expresie OP_SUB expresie
         | expresie OP_MUL expresie
         | expresie OP_DIV expresie
         | expresie OP_MOD expresie
         | expresie OP_LT expresie
         | expresie OP_LTE expresie
         | expresie OP_GT expresie
         | expresie OP_GTE expresie
         | expresie OP_EQ expresie
         | expresie OP_NE expresie
         ;

operand : ID
        | CONST_INT
        | CONST_FLOAT
        | CONST_STRING
        ;

%%

/* Sectiunea de Cod C++ */

HashSymbolTable ts;
std::vector<std::pair<int, int>> fip;

void save_fip(const std::string& filename) {
    std::ofstream f(filename);
    if (!f.is_open()) {
        std::cerr << "Eroare la scrierea FIP in: " << filename << std::endl;
        return;
    }
    f << "COD\tPoz_TS\n";
    f << "-----------------\n";
    for (const auto& pair : fip) {
        f << pair.first << "\t" << pair.second << "\n";
    }
    f.close();
}

void yyerror(const char *s) {
    std::cerr << "Eroare Sintactica [Linia " << yylineno << "]: " << s << std::endl;
}

int main(int argc, char** argv) {
    extern FILE* yyin;
    if (argc > 1) {
        yyin = fopen(argv[1], "r");
        if (!yyin) {
            std::cerr << "Eroare la deschiderea fisierului: " << argv[1] << std::endl;
            return 1;
        }
    } else {
        std::cerr << "Utilizare: ./analizator <fisier_input>" << std::endl;
        return 1;
    }

    // yyparse() este functia generata de Bison care face toata treaba
    // Ea apeleaza repetat yylex() pentru a obtine token-uri
    if (yyparse() == 0) {
        std::cout << "Analiza lexicala finalizata cu SUCCES!" << std::endl;
        std::cout << "Analiza sintactica finalizata cu SUCCES!" << std::endl;
    } else {
        std::cout << "Analiza sintactica a ESUAT." << std::endl;
    }

    ts.save_to_file("ts.txt");
    save_fip("fip.txt");

    return 0;
}