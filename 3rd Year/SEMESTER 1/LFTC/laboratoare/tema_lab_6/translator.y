%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdarg.h>

extern int yylex();
extern int yylineno;
extern FILE *yyin;
void yyerror(const char *s);

FILE *asm_file;
int temp_count = 0;

char* new_temp();
void emit(const char* format, ...);
%}

%union {
    char* str_val;
}

%token <str_val> ID CONST_INT
%token INT MAIN RETURN HEAR SPEAK ASSIGN OP_IN OP_OUT
%token PLUS MINUS MULT DIV LPAREN RPAREN LBRACE RBRACE SEMICOLON

%type <str_val> expr term factor

%left PLUS MINUS
%left MULT DIV

%start program

%%

program
    : {
        emit("section .note.GNU-stack noalloc noexec nowrite progbits");

        emit("section .data");
        emit("    format_in db \"%%d\", 0");
        emit("    format_out db \"Rezultat: %%d\", 10, 0");
        emit("");
        emit("section .bss");
        for(int i=0; i<10; i++) emit("    t%d resd 1", i);
      }
      main_block
    ;

main_block
    : INT MAIN LPAREN RPAREN LBRACE decl_list
      {
        emit("");
        emit("section .text");
        emit("    global main");
        emit("    extern printf, scanf");
        emit("");
        emit("main:");
        emit("    push rbp");
        emit("    mov rbp, rsp");
      }
      stmt_list RBRACE
      {
        emit("    mov rax, 0");
        emit("    pop rbp");
        emit("    ret");
      }
    ;

decl_list
    : decl decl_list
    | /* epsilon */
    ;

decl
    : INT ID SEMICOLON
      {
        emit("    %s resd 1", $2);
        free($2);
      }
    ;

stmt_list
    : stmt stmt_list
    | /* epsilon */
    ;

stmt
    : assign_stmt
    | io_stmt
    | return_stmt
    ;

assign_stmt
    : ID ASSIGN expr SEMICOLON
      {
        emit("    ; %s <- expr", $1);
        emit("    mov eax, %s", $3);
        emit("    mov [rel %s], eax", $1);
        free($1); free($3);
      }
    ;

io_stmt
    : HEAR OP_IN ID SEMICOLON
      {
        emit("    ; hear >> %s", $3);
        emit("    lea rdi, [rel format_in]");
        emit("    lea rsi, [rel %s]", $3);
        emit("    xor rax, rax");
        emit("    call scanf");
        free($3);
      }
    | SPEAK OP_OUT expr SEMICOLON
      {
        emit("    ; speak << expr");
        emit("    lea rdi, [rel format_out]");

        // ESI primeste valoarea de afisat
        emit("    mov esi, %s", $3);

        emit("    xor rax, rax");
        emit("    call printf");
        free($3);
      }
    ;

return_stmt
    : RETURN CONST_INT SEMICOLON
    ;


expr
    : expr PLUS term
      {
        $$ = new_temp();
        emit("    mov eax, %s", $1);
        emit("    add eax, %s", $3);
        emit("    mov %s, eax", $$);
      }
    | expr MINUS term
      {
        $$ = new_temp();
        emit("    mov eax, %s", $1);
        emit("    sub eax, %s", $3);
        emit("    mov %s, eax", $$);
      }
    | term
      { $$ = $1; }
    ;

term
    : term MULT factor
      {
        $$ = new_temp();
        emit("    mov eax, %s", $1);
        emit("    imul eax, %s", $3);
        emit("    mov %s, eax", $$);
      }
    | factor
      { $$ = $1; }
    ;

factor
    : LPAREN expr RPAREN
      { $$ = $2; }
    | ID
      {
        char* ref = (char*)malloc(30);
        sprintf(ref, "[rel %s]", $1);
        $$ = ref;
      }
    | CONST_INT
      {
        $$ = strdup($1);
      }
    ;

%%

char* new_temp() {
    char* temp = (char*)malloc(20);
    sprintf(temp, "[rel t%d]", temp_count++);
    if(temp_count > 9) temp_count = 0;
    return temp;
}

void emit(const char* format, ...) {
    va_list args;
    va_start(args, format);
    vfprintf(asm_file, format, args);
    va_end(args);
    fprintf(asm_file, "\n");
}

void yyerror(const char *s) {
    fprintf(stderr, "Eroare sintactica la linia %d: %s\n", yylineno, s);
    exit(1);
}

int main(int argc, char** argv) {
    if (argc < 3) return 1;
    yyin = fopen(argv[1], "r");
    asm_file = fopen(argv[2], "w");
    if(!yyin || !asm_file) return 1;
    yyparse();
    fclose(asm_file);
    fclose(yyin);
    return 0;
}