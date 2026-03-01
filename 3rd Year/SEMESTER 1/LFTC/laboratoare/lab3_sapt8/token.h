//
// Created by emicuciu on 11/14/25.
//

#ifndef LAB3_SAPT8_TOKEN_H
#define LAB3_SAPT8_TOKEN_H

enum AtomCode {
    ID = 0,                       //* 258
    CONST_INT = 1,                //* 259
    CONST_FLOAT = 2,              //* 260
    CONST_STRING = 3,             //* 261
    INCLUDE = 4,                  //* 262
    LIBRARY = 5, // <~library~>   //* 263
    MAIN = 6,                     //* 264
    LPARANTEZ = 7, // (           //* 283
    RPARANTEZ = 8, // )           //* 284
    LBRACKET = 9, // {            //* 285
    RBRACKET = 10, // }           //* 286
    INT = 11,                     //* 265
    DOUBLE = 12,                  //* 267
    FLOAT = 13,                   //* 266
    STRING = 14,                  //* 268
    WORD = 15,                    //* 269
    MAYBE = 16,                   //* 270
    ELSE = 17,                    //* 271
    LOOP = 18,                    //* 272
    FORLOOP = 19,                 //* 273
    HEAR = 20,                    //* 274
    SPEAK = 21,                   //* 275
    OP_OUT = 22, // <<            //* 281
    OP_IN = 23, // >>             //* 282
    OP_ASSIGN = 24, // <-         //* 280
    OP_ADD = 25, // +             //* 297
    OP_SUB = 26, // -             //* 298
    OP_MUL = 27, // *             //* 299
    OP_DIV = 28, // /             //* 300
    OP_MOD = 29, // %             //* 301
    OP_LT = 30, // <              //* 293
    OP_GT = 31, // >              //* 295
    OP_LTE = 32, // <=            //* 294
    OP_GTE = 33, // >=            //* 296
    OP_EQ = 34, // ==             //* 291
    OP_NE = 35, // !=             //* 292
    SEMICOLON = 36, // ;          //* 289
    COMMA = 37, // ,              //* 290
    RETURN = 38,                  //* 276
    SQ_LBRACKET = 39, // [        //* 287
    SQ_RBRACKET = 40, // ]        //* 288
    CATTIMP = 41,                 //* 277
    EXECUTA = 42,                 //* 278
    SFCATTIMP = 43                //* 279
};

#endif //LAB3_SAPT8_TOKEN_H