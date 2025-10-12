//
// Created by emicu on 10/12/2025.
//

#ifndef SECVENTIALDINAMIC_H
#define SECVENTIALDINAMIC_H
#include <vector>


class SecventialDinamic
{
    int N, M, n;
    int **matrix = nullptr, **convMatrix = nullptr;

public:
    int **resultMatrix = nullptr;

    SecventialDinamic(int N, int M, int n);
    ~SecventialDinamic();
    void allocateMatrices();
    void deallocateMatrices() const;
    static void run(const std::vector<std::vector<int>>& srcMatrix, const std::vector<std::vector<int>>& srcConv);
    static void writeToFile(const char* path, int** arr, int N, int M);
};


#endif //SECVENTIALDINAMIC_H
