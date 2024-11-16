cmake_minimum_required(VERSION 3.27)
project(L4)

set(CMAKE_CXX_STANDARD 17)

add_executable(L4 main.cpp
        Colectie.cpp
        IteratorColectie.cpp
        TestExtins.cpp
        TestScurt.cpp)
