//
// Created by emicu on 10/12/2025.
//

#ifndef COLOANEDINAMIC_H
#define COLOANEDINAMIC_H



#include <vector>
#include <thread>

class ColoaneDinamic {
    int N, M, n, p;
    int **matrix = nullptr, **convMatrix = nullptr, **resultMatrix = nullptr;

public:
    ColoaneDinamic(int N, int M, int n, int p);
    ~ColoaneDinamic();
    void allocate();
    void deallocate();
    void loadData(const std::vector<std::vector<int>>& mat, const std::vector<std::vector<int>>& conv);
    void run();
    void worker(int startCol, int endCol);
    static void writeToFile(const char* path, int** arr, int N, int M);
};



#endif //COLOANEDINAMIC_H
