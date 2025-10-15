#include <chrono>
#include <iostream>
#include <random>
#include <thread>
#include <vector>

using namespace std;

const int SIZE = 10000000;

int x[SIZE], y[SIZE], z[SIZE];

int generateRandomNumber(int upperBoundary)
{
    return rand() % upperBoundary + 1;
}

void genVect(std::pmr::vector<int>& copy)
{
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> dist(1, 100);

    for (int i = 0; i < SIZE; ++i)
        copy.emplace_back(dist(gen));
}

void printArray(vector<int> a, int n)
{
    for (int i = 0; i < n; i++)
        cout << a[i] << " ";
    cout << "\n";
}

int operatorAdunare(int a, int b)
{
    return a * a * a * a * a + b * b * b * b * b;
}


void task(vector<int>& a, vector<int>& b, vector<int>& c, int start, int end)
{
    for (int i = start; i < end; i++)
    {
        c[i] = operatorAdunare(a[i], b[i]);
    }
}

void task_static(int start, int end)
{
    for (int i = start; i < end; i++)
    {
        z[i] = operatorAdunare(x[i], y[i]);
    }
}


int main()
{
    // secvential
    vector<int> a(SIZE), b(SIZE), c(SIZE);

    for (int i = 0; i < SIZE; i++)
    {
        a[i] = generateRandomNumber(10000);
        b[i] = generateRandomNumber(10000);
    }

    auto t_start = std::chrono::high_resolution_clock::now();


    for (int i = 0; i < SIZE; i++)
    {
        c[i] = operatorAdunare(a[i], b[i]);
    }

    auto t_end = std::chrono::high_resolution_clock::now();

    if (SIZE <= 10)
    {
        printArray(a, SIZE);
        printArray(b, SIZE);
        printArray(c, SIZE);
    }


    double elapsed_time_ms = std::chrono::duration<double, std::milli>(t_end - t_start).count();

    cout << elapsed_time_ms << "ms \n";

    // paralel
    cout << "Paralel \n";
    vector<int> c_paralel(SIZE);

    int p = 4;
    int start, end, rest;
    start = 0;
    end = SIZE / p;
    rest = SIZE % p;

    vector<thread> threads(p);


    t_start = std::chrono::high_resolution_clock::now();
    for (int i = 0; i < p; i++)
    {
        if (rest)
        {
            end++;
            rest--;
        }

        threads[i] = thread(task, ref(a), ref(b), ref(c_paralel), start, end);
        // threads[i] = thread(task_static, start, end);
        start = end;
        end = end + SIZE / p;
    }

    for (int i = 0; i < p; i++)
    {
        threads[i].join();
    }

    if (SIZE <= 10)
    {
        printArray(c_paralel, SIZE);
    }

    t_end = std::chrono::high_resolution_clock::now();

    elapsed_time_ms = std::chrono::duration<double, std::milli>(t_end - t_start).count();

    cout << elapsed_time_ms << "ms\n";


    // static

    return 0;
}
