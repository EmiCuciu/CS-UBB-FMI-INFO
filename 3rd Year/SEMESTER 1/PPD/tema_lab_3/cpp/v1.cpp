#include <mpi/mpi.h>
#include <algorithm>
#include <chrono>
#include <fstream>
#include <iostream>
#include <vector>

using namespace std;

bool compareFiles(const string& file1, const string& file2)
{
    ifstream f1(file1), f2(file2);
    if (!f1.is_open() || !f2.is_open())
    {
        cerr << "Eroare la deschiderea fisierelor \n";
        return false;
    }

    string line1, line2;
    vector<string> lines1, lines2;

    while (getline(f1, line1))
    {
        line1.erase(line1.find_last_not_of(" \t\r\n") + 1);
        if (!line1.empty()) lines1.push_back(line1);
    }

    while (getline(f2, line2))
    {
        line2.erase(line2.find_last_not_of(" \t\r\n") + 1);
        if (!line2.empty()) lines2.push_back(line2);
    }

    if (lines1.size() != lines2.size())
    {
        cerr << "Fisierele au numere diferite de linii substantiale\n";
        return false;
    }

    for (size_t i = 0; i < lines1.size(); i++)
    {
        if (lines1[i] != lines2[i])
        {
            cerr << "Diferenta la linia " << (i + 1) << "\n";
            return false;
        }
    }

    cout << "Sunt identice" << endl;
    return true;
}

int main(int argc, char** argv)
{
    MPI_Init(&argc, &argv);
    int rank, p;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &p);

    auto start = chrono::high_resolution_clock::now();

    if (rank == 0)
    {
        //? Test1
        // ifstream in1("v1_data/test1/N_1.txt");
        // ifstream in2("v1_data/test1/N_2.txt");

        //? Test2
        // ifstream in1("v1_data/test2/N_1.txt");
        // ifstream in2("v1_data/test2/N_2.txt");

        //? Test3
        ifstream in1("v1_data/test3/N_1.txt");
        ifstream in2("v1_data/test3/N_2.txt");

        int n1, n2;
        in1 >> n1;
        in2 >> n2;

        int N_max = max(n1, n2);

        int chunk_size = N_max / (p - 1);
        int rest = N_max % (p - 1);

        int id_proces_curent = 1;
        int cifre_citite = 0;

        while (cifre_citite < N_max && id_proces_curent < p)
        {
            int current_chunk = chunk_size;
            if (id_proces_curent <= rest)
            {
                current_chunk++;
            }

            vector<int> chunk_N_1(current_chunk);
            vector<int> chunk_N_2(current_chunk);

            for (int i = 0; i < current_chunk; i++)
            {
                if (cifre_citite + i < n1)
                {
                    in1 >> chunk_N_1[i];
                }
                else
                {
                    chunk_N_1[i] = 0; // Padding
                }

                if (cifre_citite + i < n2)
                {
                    in2 >> chunk_N_2[i];
                }
                else
                {
                    chunk_N_2[i] = 0; // Padding
                }
            }

            reverse(chunk_N_1.begin(), chunk_N_1.end());
            reverse(chunk_N_2.begin(), chunk_N_2.end());

            MPI_Send(&current_chunk, 1, MPI_INT, id_proces_curent, 0, MPI_COMM_WORLD);

            MPI_Send(chunk_N_1.data(), current_chunk, MPI_INT, id_proces_curent, 1, MPI_COMM_WORLD);

            MPI_Send(chunk_N_2.data(), current_chunk, MPI_INT, id_proces_curent, 2, MPI_COMM_WORLD);

            cifre_citite += current_chunk;
            id_proces_curent++;
        }

        in1.close();
        in2.close();

        // ofstream out("v1_data/test1/N_3_paralel.txt");
        // ofstream out("v1_data/test2/N_3_paralel.txt");
        ofstream out("v1_data/test3/N_3_paralel.txt");

        vector<int> all_results;

        for (int i = 1; i < p; i++)
        {
            int result_size;
            MPI_Recv(&result_size, 1, MPI_INT, i, 3, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            vector<int> partial_result(result_size);
            MPI_Recv(partial_result.data(), result_size, MPI_INT, i, 4, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            all_results.insert(all_results.end(), partial_result.begin(), partial_result.end());
        }

        while (all_results.size() > 1 && all_results.back() == 0)
        {
            all_results.pop_back();
        }

        out << all_results.size() << "\n";

        for (int i = all_results.size() - 1; i >= 0; --i)
        {
            out << all_results[i] << " ";
        }

        out.close();

        // compareFiles("v1_data/test1/N_3.txt", "v1_data/test1/N_3_paralel.txt");
        // compareFiles("v1_data/test2/N_3.txt", "v1_data/test2/N_3_paralel.txt");
        compareFiles("v1_data/test3/N_3.txt", "v1_data/test3/N_3_paralel.txt");

        auto end = chrono::high_resolution_clock::now();
        auto duration = chrono::duration_cast<chrono::microseconds>(end - start).count();
        cout << duration << endl;
    }
    else
    {
        int chunk_size;
        MPI_Recv(&chunk_size, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        vector<int> local_N_1(chunk_size);
        vector<int> local_N_2(chunk_size);

        MPI_Recv(local_N_1.data(), chunk_size, MPI_INT, 0, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        MPI_Recv(local_N_2.data(), chunk_size, MPI_INT, 0, 2, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        int carry = 0;
        if (rank > 1)
        {
            MPI_Recv(&carry, 1, MPI_INT, rank - 1, 5, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        }

        vector<int> local_result(chunk_size);
        for (int i = 0; i < chunk_size; i++)
        {
            int sum = local_N_1[i] + local_N_2[i] + carry;
            local_result[i] = sum % 10;
            carry = sum / 10;
        }


        if (rank < p - 1)
        {
            MPI_Send(&carry, 1, MPI_INT, rank + 1, 5, MPI_COMM_WORLD);
        }

        if (rank == p - 1 && carry > 0)
        {
            local_result.push_back(carry);
        }

        int result_size = local_result.size();
        MPI_Send(&result_size, 1, MPI_INT, 0, 3, MPI_COMM_WORLD);
        MPI_Send(local_result.data(), result_size, MPI_INT, 0, 4, MPI_COMM_WORLD);
    }

    MPI_Finalize();
    return 0;
}