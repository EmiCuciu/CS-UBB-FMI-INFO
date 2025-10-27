#include <iostream>
#include <chrono>
#include <vector>
#include <fstream>
#include "Utils.h"
#include "SecventialDinamic.h"
#include "LiniiDinamic.h"

using namespace std;
using namespace chrono;

struct TestConfig {
    string inputFile;
    string outputSeq;
    string outputPar;
};

TestConfig getTestConfig(const string& inputFile) {
    TestConfig config;
    config.inputFile = inputFile;

    // Extract the base name from the input file path
    size_t lastSlash = inputFile.find_last_of("/\\");
    string fileName = (lastSlash == string::npos) ? inputFile : inputFile.substr(lastSlash + 1);

    // Remove "date_" prefix and ".txt" suffix
    string baseName;
    size_t datePos = fileName.find("date_");
    size_t txtPos = fileName.rfind(".txt");

    if (datePos != string::npos && txtPos != string::npos) {
        baseName = fileName.substr(datePos + 5, txtPos - (datePos + 5));
    } else {
        baseName = fileName;
    }

    // Get directory path from input file
    string dirPath = (lastSlash == string::npos) ? "" : inputFile.substr(0, lastSlash + 1);

    config.outputSeq = dirPath + "output_" + baseName + "_seq.txt";
    config.outputPar = dirPath + "output_" + baseName + ".txt";

    return config;
}

double runSequential(const string &inputFile, const string &outputFile) {
    int N, M, n, p;
    vector<vector<int>> matrix, convMatrix;

    Utils::readData(inputFile, N, M, n, p, matrix, convMatrix);

    SecventialDinamic seq(N, M, n);
    seq.loadData(matrix, convMatrix);

    auto start = high_resolution_clock::now();
    seq.run();
    auto end = high_resolution_clock::now();

    seq.writeToFile(outputFile.c_str());

    duration<double, milli> duration = end - start;
    return duration.count();
}

double runParallel(const string &inputFile, const string &outputFile, int threads) {
    int N, M, n, p;
    vector<vector<int>> matrix, convMatrix;

    Utils::readData(inputFile, N, M, n, p, matrix, convMatrix);

    LiniiDinamic par(N, M, n, threads);
    par.loadData(matrix, convMatrix);

    auto start = high_resolution_clock::now();
    par.run();
    auto end = high_resolution_clock::now();

    par.writeToFile(outputFile.c_str());

    duration<double, milli> duration = end - start;
    return duration.count();
}


int main(int argc, char *argv[]) {
    if (argc < 2) {
        cerr << "Usage: " << argv[0] << " <threads>" << endl;
        return 1;
    }

    const string basePath = "/home/emicuciu/Facultate/CS-UBB/3rd Year/PPD/tema_lab_2/tema_lab_2C++/cmake-build-release/";

    const string date_10_10_3_2 = basePath + "date_10_10_3_2.txt";
    const string date_1000_1000_3_2 = basePath + "date_1000_1000_3_2.txt";
    const string date_10000_10000_3_2 = basePath + "date_10000_10000_3_2.txt";

    int threads = stoi(argv[1]);

    const string& inputFile = date_10_10_3_2;
    // const string& inputFile = date_1000_1000_3_2;
    // const string& inputFile = date_10000_10000_3_2;

    TestConfig config = getTestConfig(inputFile);

    double time;
    string outputFile;

    if (threads == 0) {
        outputFile = config.outputSeq;
        time = runSequential(inputFile, outputFile);
        cout << time << endl;
    } else {
        outputFile = config.outputPar;
        time = runParallel(inputFile, outputFile, threads);
        cout << time << endl;

        Utils::compareFiles(config.outputSeq, config.outputPar);
    }

    return 0;
}
