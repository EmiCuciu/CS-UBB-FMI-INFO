cmake_minimum_required(VERSION 3.27)
project(lab2_dinamic C)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall -Wextra")

add_executable(lab2_dinamic main.c
        Domain/tranzactie.h
        Domain/tranzactie.c
        Repository/tranzactie_repo.h
        Repository/tranzactie_repo.c
        Teste/test_domain.c
        Teste/test_domain.h
        Teste/tests.c
        Teste/tests.h
        Teste/test_repo.c
        Teste/test_repo.h
        Service/tranzactie_service.c
        Service/tranzactie_service.h
        Teste/test_service.c
        Teste/test_service.h
        UI/tranzactie_ui.c
        UI/tranzactie_ui.h
        Validari/validari.c
        Validari/validari.h
        Teste/test_validari.c
        Teste/test_validari.h
)
