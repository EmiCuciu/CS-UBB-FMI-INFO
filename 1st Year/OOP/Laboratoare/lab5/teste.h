//
// Created by Emi on 3/26/2024.
//

#ifndef OOPLAB2_4_TESTE_H
#define OOPLAB2_4_TESTE_H

#include "domain.h"
#include "service.h"
#include "repo.h"
#include "validari.h"

///Domain
void test_creaza_materie_prima();


void test_copy_materie_prima();

///Repo
void test_creaza_vector_repo();

void test_get_elem_repo();

void test_size_repo();

void test_add_elem_repo();

void test_copy_vector_repo();

///Service
void test_adauga_materie_prima_service();

void test_sterge_materie_prima_service();

void test_update_materie_prima_service();

void test_undo();


///Validari
void test_validare();

void run_all_tests();

#endif //OOPLAB2_4_TESTE_H
