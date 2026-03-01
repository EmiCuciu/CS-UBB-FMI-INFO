//
// Created by emicu on 10/12/2025.
//

#ifndef UTILS_H
#define UTILS_H

#include <string>
#include <vector>

using namespace std;

class Utils {
public:
    static void readData(const string &filePath, int &N, int &M, int &n, int &p, vector<vector<int> > &matrix,
                         vector<vector<int> > &convMatrix);

    static bool compareFiles(const string &file1, const string &file2);

    static void generateData(const string &filePath, int N, int M, int n, int p);
};

#endif //UTILS_H
