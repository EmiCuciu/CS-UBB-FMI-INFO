/* A Bison parser, made by GNU Bison 3.8.2.  */

/* Bison interface for Yacc-like parsers in C

   Copyright (C) 1984, 1989-1990, 2000-2015, 2018-2021 Free Software Foundation,
   Inc.

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <https://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* DO NOT RELY ON FEATURES THAT ARE NOT DOCUMENTED in the manual,
   especially those whose name start with YY_ or yy_.  They are
   private implementation details that can be changed or removed.  */

#ifndef YY_YY_PARSER_TAB_H_INCLUDED
# define YY_YY_PARSER_TAB_H_INCLUDED
/* Debug traces.  */
#ifndef YYDEBUG
# define YYDEBUG 0
#endif
#if YYDEBUG
extern int yydebug;
#endif

/* Token kinds.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
  enum yytokentype
  {
    YYEMPTY = -2,
    YYEOF = 0,                     /* "end of file"  */
    YYerror = 256,                 /* error  */
    YYUNDEF = 257,                 /* "invalid token"  */
    ID = 258,                      /* ID  */
    CONST_INT = 259,               /* CONST_INT  */
    CONST_FLOAT = 260,             /* CONST_FLOAT  */
    CONST_STRING = 261,            /* CONST_STRING  */
    INCLUDE = 262,                 /* INCLUDE  */
    LIBRARY = 263,                 /* LIBRARY  */
    MAIN = 264,                    /* MAIN  */
    INT = 265,                     /* INT  */
    FLOAT = 266,                   /* FLOAT  */
    DOUBLE = 267,                  /* DOUBLE  */
    STRING = 268,                  /* STRING  */
    WORD = 269,                    /* WORD  */
    MAYBE = 270,                   /* MAYBE  */
    ELSE = 271,                    /* ELSE  */
    LOOP = 272,                    /* LOOP  */
    FORLOOP = 273,                 /* FORLOOP  */
    HEAR = 274,                    /* HEAR  */
    SPEAK = 275,                   /* SPEAK  */
    RETURN = 276,                  /* RETURN  */
    CATTIMP = 277,                 /* CATTIMP  */
    EXECUTA = 278,                 /* EXECUTA  */
    SFCATTIMP = 279,               /* SFCATTIMP  */
    OP_ASSIGN = 280,               /* OP_ASSIGN  */
    OP_OUT = 281,                  /* OP_OUT  */
    OP_IN = 282,                   /* OP_IN  */
    LPARANTEZ = 283,               /* LPARANTEZ  */
    RPARANTEZ = 284,               /* RPARANTEZ  */
    LBRACKET = 285,                /* LBRACKET  */
    RBRACKET = 286,                /* RBRACKET  */
    SQ_LBRACKET = 287,             /* SQ_LBRACKET  */
    SQ_RBRACKET = 288,             /* SQ_RBRACKET  */
    SEMICOLON = 289,               /* SEMICOLON  */
    COMMA = 290,                   /* COMMA  */
    OP_EQ = 291,                   /* OP_EQ  */
    OP_NE = 292,                   /* OP_NE  */
    OP_LT = 293,                   /* OP_LT  */
    OP_LTE = 294,                  /* OP_LTE  */
    OP_GT = 295,                   /* OP_GT  */
    OP_GTE = 296,                  /* OP_GTE  */
    OP_ADD = 297,                  /* OP_ADD  */
    OP_SUB = 298,                  /* OP_SUB  */
    OP_MUL = 299,                  /* OP_MUL  */
    OP_DIV = 300,                  /* OP_DIV  */
    OP_MOD = 301                   /* OP_MOD  */
  };
  typedef enum yytokentype yytoken_kind_t;
#endif

/* Value type.  */
#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef int YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define YYSTYPE_IS_DECLARED 1
#endif


extern YYSTYPE yylval;


int yyparse (void);


#endif /* !YY_YY_PARSER_TAB_H_INCLUDED  */
