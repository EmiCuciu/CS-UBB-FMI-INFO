//
// Created by emicuciu on 11/14/25.
//

#ifndef LAB3_SAPT8_TOKEN_H
#define LAB3_SAPT8_TOKEN_H

enum AtomCode {
    ID = 0,
    CONST_INT = 1,
    CONST_FLOAT = 2,
    CONST_STRING = 3,
    INCLUDE = 4,
    LIBRARY = 5, // <~library~>
    MAIN = 6,
    LPARANTEZ = 7, // (
    RPARANTEZ = 8, // )
    LBRACKET = 9, // {
    RBRACKET = 10, // }
    INT = 11,
    DOUBLE = 12,
    FLOAT = 13,
    STRING = 14,
    WORD = 15,
    MAYBE = 16,
    ELSE = 17,
    LOOP = 18,
    FORLOOP = 19,
    HEAR = 20,
    SPEAK = 21,
    OP_OUT = 22, // <<
    OP_IN = 23, // >>
    OP_ASSIGN = 24, // <-
    OP_ADD = 25, // +
    OP_SUB = 26, // -
    OP_MUL = 27, // *
    OP_DIV = 28, // /
    OP_MOD = 29, // %
    OP_LT = 30, // <
    OP_GT = 31, // >
    OP_LTE = 32, // <=
    OP_GTE = 33, // >=
    OP_EQ = 34, // ==
    OP_NE = 35, // !=
    SEMICOLON = 36, // ;
    COMMA = 37, // ,
    RETURN = 38,
    SQ_LBRACKET = 39, // [
    SQ_RBRACKET = 40 // ]
};

#endif //LAB3_SAPT8_TOKEN_H
