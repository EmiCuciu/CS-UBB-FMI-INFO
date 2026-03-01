#ifndef TEMA_LAB_2C___LINIIDINAMIC_H
#define TEMA_LAB_2C___LINIIDINAMIC_H
#include <condition_variable>
#include <mutex>
#include <vector>

using namespace std;

class my_barrier
{
public:
    explicit my_barrier(int count) : thread_count(count), counter(0), generation(0)
    {
    }

    void wait()
    {
        std::unique_lock lk(m);
        int local_gen = generation;

        ++counter;

        if (counter >= thread_count)
        {
            ++generation;
            counter = 0;
            cv.notify_all();
        }
        else
        {
            cv.wait(lk, [&] { return local_gen != generation; });
        }
    }

private:
    std::mutex m;
    std::condition_variable cv;
    int counter;
    int generation;
    int thread_count;
};

class LiniiDinamic
{
    int N, M, n, p;
    int **matrix = nullptr, **convMatrix = nullptr;

    void calculateRow(const vector<int>& prev, const vector<int>& current,
                      const vector<int>& next, vector<int>& result) const;

public:
    LiniiDinamic(int N, int M, int n, int p);
    ~LiniiDinamic();
    void allocate();
    void deallocate() const;
    void loadData(const vector<vector<int>>& mat, const vector<vector<int>>& conv) const;
    void run();
    void writeToFile(const char* path) const;

    void worker(int t, int startRow, int endRow, my_barrier& barrier);
};

#endif //TEMA_LAB_2C___LINIIDINAMIC_H