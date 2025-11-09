#include <mpi/mpi.h>
#include <algorithm>
#include <fstream>
#include <iostream>
#include <vector>

using namespace std;

void read_number(const string& filename, vector<int>& number)
{
    ifstream in(filename);
    int n;
    in >> n;
    number.resize(n);
    for (int i = 0; i < n; i++)
    {
        in >> number[i];
    }
    in.close();

    reverse(number.begin(), number.end());
}

void write_number(const string& filename, const vector<int>& number)
{
    ofstream out(filename);
    out << number.size() << "\n";
    for (int i = number.size() - 1; i >= 0; --i)
    {
        out << number[i] << " ";
    }
    out.close();
}

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

    vector<int> N_1, N_2, N_3;

    int chunk_size = 0;

    if (rank == 0)
    {
        int N_padded = 0;

        //? Test1
        // read_number("v2_data/test1/N_1.txt", N_1);
        // read_number("v2_data/test1/N_2.txt", N_2);

        //? Test2
        read_number("v2_data/test2/N_1.txt", N_1);
        read_number("v2_data/test2/N_2.txt", N_2);

        //? Test3
        // read_number("v2_data/test3/N_1.txt", N_1);
        // read_number("v2_data/test3/N_2.txt", N_2);

        int n1 = N_1.size();
        int n2 = N_2.size();
        int N_max = max(n1, n2);

        N_padded = N_max;
        if (N_max % p != 0)
        {
            N_padded = N_max + (p - (N_max % p)); // ca sa avem multiplu de p, ne ajuta la Gather
        }
        chunk_size = N_padded / p;

        N_1.resize(N_padded, 0);
        N_2.resize(N_padded, 0);

        N_3.resize(N_padded);
    }


    MPI_Bcast(&chunk_size, 1, MPI_INT, 0, MPI_COMM_WORLD);

    vector<int> local_N_1(chunk_size);
    vector<int> local_N_2(chunk_size);
    vector<int> local_result(chunk_size);

    MPI_Scatter(N_1.data(), chunk_size, MPI_INT, local_N_1.data(), chunk_size, MPI_INT, 0, MPI_COMM_WORLD);

    MPI_Scatter(N_2.data(), chunk_size, MPI_INT, local_N_2.data(), chunk_size, MPI_INT, 0, MPI_COMM_WORLD);

    int carry = 0;
    if (rank > 0)
    {
        MPI_Recv(&carry, 1, MPI_INT, rank - 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        // primim carry de la procesul anterior
    }

    for (int i = 0; i < chunk_size; i++)
    {
        int sum = local_N_1[i] + local_N_2[i] + carry;
        local_result[i] = sum % 10;
        carry = sum / 10;
    }

    if (rank < p - 1)
    {
        MPI_Send(&carry, 1, MPI_INT, rank + 1, 0, MPI_COMM_WORLD);
    }
    else if (rank == p - 1)
    {
        MPI_Send(&carry, 1, MPI_INT, 0, 1, MPI_COMM_WORLD);
    }


    MPI_Gather(local_result.data(), chunk_size, MPI_INT, N_3.data(), chunk_size, MPI_INT, 0, MPI_COMM_WORLD);

    if (rank == 0)
    {
        int final_carry = 0;

        MPI_Recv(&final_carry, 1, MPI_INT, p - 1, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);


        if (final_carry > 0)
        {
            N_3.push_back(final_carry);
        }


        while (N_3.size() > 1 && N_3.back() == 0)
        {
            N_3.pop_back();
        }

        // write_number("v2_data/test1/N_3_paralel.txt", N_3);

        write_number("v2_data/test2/N_3_paralel.txt", N_3);

        // write_number("v2_data/test3/N_3_paralel.txt", N_3);

        // compareFiles("v2_data/test1/N_3.txt", "v2_data/test1/N_3_paralel.txt");
        compareFiles("v2_data/test2/N_3.txt", "v2_data/test2/N_3_paralel.txt");
        // compareFiles("v2_data/test3/N_3.txt", "v2_data/test3/N_3_paralel.txt");
    }

    MPI_Finalize();
    return 0;
}
