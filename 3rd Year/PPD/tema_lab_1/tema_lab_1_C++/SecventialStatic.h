//
// Created by emicu on 10/12/2025.
//

#ifndef SECVENTIALSTATIC_H
#define SECVENTIALSTATIC_H


class SecventialStatic
{
    int N, M, n;
    int matrix[10][10]{}, convMatrix[5][5]{}, resultMatrix[10][10]{};

public:
    SecventialStatic(int N, int M, int n, const int srcMatrix[10][10], const int srcConv[5][5]);
    int (& run())[10][10];
    static void writeToFile(const char* path, int arr[10][10]);
};


#endif //SECVENTIALSTATIC_H
