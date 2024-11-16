cmake_minimum_required(VERSION 3.28)
project(lab5)

set(CMAKE_CXX_STANDARD 17)

add_executable(lab5
        ciclu_eulerian.cpp
        ford_fulkerson.cpp
        pompare_topologica.cpp
        main.cpp
        ciclu_eulerian.h
        ford_fulkerson.h
        pompare_topologica.h
)
