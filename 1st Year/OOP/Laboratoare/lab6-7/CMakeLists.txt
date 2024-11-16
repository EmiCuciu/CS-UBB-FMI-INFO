cmake_minimum_required(VERSION 3.27)
project(lab6_7)

set(CMAKE_CXX_STANDARD 17)

add_executable(lab6_7 main.cpp
        Domain/Carte.cpp
        Domain/Carte.h
        Repository/Repository.cpp
        Repository/Repository.h
        Service/Service.cpp
        Service/Service.h
        UI/ui.cpp
        UI/ui.h
        Teste/Teste.cpp
        Teste/Teste.h
        Validari/validari.cpp
        Validari/validari.h)
