#ifndef TEMA_LAB_2C___LINIIDINAMIC_H
#define TEMA_LAB_2C___LINIIDINAMIC_H
#include <condition_variable>
#include <mutex>
#include <vector>

using namespace std;

class my_barrier {
private:
    mutex m;
    condition_variable cv;
    int counter;
    int thread_count;
    int generation;

public:
    explicit my_barrier(int count) : thread_count(count), counter(0), generation(0) {
    }

    void wait() {
        unique_lock<mutex> lk(m);
        int gen = generation;
        counter++;
        if (counter == thread_count) {
            generation++;
            counter = 0;
            cv.notify_all();
            return;
        }
        cv.wait(lk, [this, gen]() {
            return gen != generation;
        });
    }
};

class LiniiDinamic {
    int N, M, n, p;
    int **matrix = nullptr, **convMatrix = nullptr;

public:
    LiniiDinamic(int N, int M, int n, int p);

    ~LiniiDinamic();

    void allocate();

    void deallocate() const;

    void loadData(const vector<vector<int> > &mat, const vector<vector<int> > &conv) const;

    void run();

    void writeToFile(const char *path) const;

    void worker(int threadId, int totalThreads, my_barrier &barrierCompute, my_barrier &barrierCopy,
                my_barrier &barrierUpdate, vector<int> &sharedPrev, vector<int> &sharedCur,
                vector<int> &sharedTemp) const;
};

#endif //TEMA_LAB_2C___LINIIDINAMIC_H
