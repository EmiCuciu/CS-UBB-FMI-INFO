//
// Created by emicu on 10/12/2025.
//

#ifndef LINIIDINAMIC_H
#define LINIIDINAMIC_H



#include <vector>
#include <thread>

class LiniiDinamic {
    int N, M, n, p;
    int **matrix = nullptr, **convMatrix = nullptr, **resultMatrix = nullptr;

public:
    LiniiDinamic(int N, int M, int n, int p);
    ~LiniiDinamic();
    void allocate();
    void deallocate();
    void loadData(const std::vector<std::vector<int>>& mat, const std::vector<std::vector<int>>& conv);
    void run();
    void worker(int startRow, int endRow);
    static void writeToFile(const char* path, int** arr, int N, int M);
};


#endif //LINIIDINAMIC_H
