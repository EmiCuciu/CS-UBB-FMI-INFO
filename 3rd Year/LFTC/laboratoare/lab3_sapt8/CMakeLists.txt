cmake_minimum_required(VERSION 4.0)
project(lab3_sapt8)

set(CMAKE_CXX_STANDARD 20)

add_executable(lab3_sapt8 main.cpp
        HashSymbolTable.cpp
        HashSymbolTable.h
        token.h
        lex.yy.c)
