cmake_minimum_required(VERSION 3.31)
project(tema_lab_1_C__)

set(CMAKE_CXX_STANDARD 20)

add_executable(tema_lab_1_C__ main.cpp
        Utils.cpp
        Utils.h
        SecventialStatic.cpp
        SecventialStatic.h
        SecventialDinamic.cpp
        SecventialDinamic.h
        LiniiStatic.cpp
        LiniiStatic.h
        LiniiDinamic.cpp
        LiniiDinamic.h
        ColoaneStatic.cpp
        ColoaneStatic.h
        ColoaneDinamic.cpp
        ColoaneDinamic.h
)
