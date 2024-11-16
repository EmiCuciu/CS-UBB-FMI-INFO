//
// Created by Emi on 5/17/2024.
//

#ifndef LAB5_FORD_FULKERSON_H
#define LAB5_FORD_FULKERSON_H
#include <iostream>
#include <fstream>
#include <vector>
#include <queue>

bool BFS(std::vector<std::vector<int>>& graf, int sursa, int destinatie, std::vector<int>& parinte);

int FORD_FULKERSON(std::vector<std::vector<int>>& graf, int sursa, int destinatie);

void ff();

#endif //LAB5_FORD_FULKERSON_H
