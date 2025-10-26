#ifndef SECVENTIALDINAMIC_H
#define SECVENTIALDINAMIC_H
#include <vector>

class SecventialDinamic {
    int N, M, n;
    int **matrix = nullptr, **convMatrix = nullptr;

public:
    SecventialDinamic(int N, int M, int n);

    ~SecventialDinamic();

    void allocateMatrices();

    void deallocateMatrices() const;

    void loadData(const std::vector<std::vector<int> > &srcMatrix, const std::vector<std::vector<int> > &srcConv) const;

    void run() const;

    void writeToFile(const char *path) const;
};

#endif
