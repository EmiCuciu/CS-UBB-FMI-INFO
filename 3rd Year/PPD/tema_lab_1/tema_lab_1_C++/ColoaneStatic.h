//
// Created by emicu on 10/12/2025.
//

#ifndef COLOANESTATIC_H
#define COLOANESTATIC_H



#include <thread>
#include <vector>

class ColoaneStatic {
    int N, M, n, p;
    int matrix[1000][10]{}, convMatrix[5][5]{}, resultMatrix[1000][10]{};

public:
    ColoaneStatic(int N, int M, int n, int p, const int srcMatrix[1000][10], const int srcConv[5][5]);
    void run();
    void worker(int startCol, int endCol);
    static void writeToFile(const char* path, int arr[1000][10]);
};


#endif //COLOANESTATIC_H
