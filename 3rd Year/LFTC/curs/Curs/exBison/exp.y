%{
#include <stdio.h>
%}

%token DIGIT
%left '+'			
%left '*'

%%
line : 	expr '\n'    {  printf("%d\n",$1); }
	 ;

expr : expr '+' expr 		{ $$ = $1 + $3;}
		| expr '*' expr 	{ $$ = $1 * $3;}
		| DIGIT				{ $$ = $1}
     ;


%%

yyerror()
{
    printf("syntax error\n");
}

main()
{
    yyparse();
}
