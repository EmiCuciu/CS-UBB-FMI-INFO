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
        out << number[i];
        if (i > 0) out << " ";
    }
    out << "\n";
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

    int rank, numProcesses;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &numProcesses);

    const int MASTER = 0;
    vector<int> N_1, N_2, N_3;

    if (rank == MASTER)
    {
        //? Test1
        // read_number("v1_data/test1/N_1.txt", N_1);
        // read_number("v1_data/test1/N_2.txt", N_2);

        //? Test2
        read_number("v1_data/test2/N_1.txt", N_1);
        read_number("v1_data/test2/N_2.txt", N_2);

        //? Test3
        // read_number("v1_data/test3/N_1.txt", N_1);
        // read_number("v1_data/test3/N_2.txt", N_2);

        int n1 = N_1.size();
        int n2 = N_2.size();
        int N = max(n1, n2);

        N_1.resize(N, 0);
        N_2.resize(N, 0);
        N_3.resize(N + 1, 0);

        int segSize = N / (numProcesses - 1);
        int rest = N % (numProcesses - 1);

        int start = 0;
        int id_proces_curent = 1;

        while (start < N)
        {
            int len = segSize + (id_proces_curent <= rest ? 1 : 0);
            int end = start + len;

            vector<int> seg1(len), seg2(len);
            for (int i = 0; i < len; ++i)
            {
                seg1[i] = N_1[start + i];
                seg2[i] = N_2[start + i];
            }

            MPI_Send(&start, 1, MPI_INT, id_proces_curent, 0, MPI_COMM_WORLD);
            MPI_Send(&end, 1, MPI_INT, id_proces_curent, 0, MPI_COMM_WORLD);
            MPI_Send(seg1.data(), len, MPI_INT, id_proces_curent, 0, MPI_COMM_WORLD);
            MPI_Send(seg2.data(), len, MPI_INT, id_proces_curent, 0, MPI_COMM_WORLD);


            start = end;
            id_proces_curent++;
            if (id_proces_curent == numProcesses) id_proces_curent = 1;
        }

        for (int id = 1; id < numProcesses; ++id)
        {
            int dummy = 0;
            MPI_Send(&dummy, 1, MPI_INT, id, 0, MPI_COMM_WORLD);
            MPI_Send(&dummy, 1, MPI_INT, id, 0, MPI_COMM_WORLD);
        }

        int segments_received = 0;
        while (segments_received < N)
        {
            int seg_start, seg_end;
            MPI_Status status;

            MPI_Recv(&seg_start, 1, MPI_INT, MPI_ANY_SOURCE, 0, MPI_COMM_WORLD, &status);
            MPI_Recv(&seg_end, 1, MPI_INT, status.MPI_SOURCE, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            MPI_Status stat;
            int elements_available;
            MPI_Probe(status.MPI_SOURCE, 0, MPI_COMM_WORLD, &stat);
            MPI_Get_count(&stat, MPI_INT, &elements_available);

            vector<int> result(elements_available);
            MPI_Recv(result.data(), elements_available, MPI_INT, status.MPI_SOURCE, 0, MPI_COMM_WORLD,
                     MPI_STATUS_IGNORE);

            for (int i = 0; i < result.size(); ++i)
            {
                N_3[seg_start + i] = result[i];
            }

            segments_received += (seg_end - seg_start);
        }

        while (N_3.size() > 1 && N_3.back() == 0)
        {
            N_3.pop_back();
        }

        //? Test1
        // write_number("v1_data/test1/N_3_paralel.txt", N_3);
        // compareFiles("v1_data/test1/N_3.txt", "v1_data/test1/N_3_paralel.txt");

        //? Test2
        write_number("v1_data/test2/N_3_paralel.txt", N_3);
        compareFiles("v1_data/test2/N_3.txt", "v1_data/test2/N_3_paralel.txt");

        // ? Test3
        // write_number("v1_data/test3/N_3_paralel.txt", N_3);
        // compareFiles("v1_data/test3/N_3.txt", "v1_data/test3/N_3_paralel.txt");
    }
    else
    {
        int carry = 0;
        while (true)
        {
            int start, end;
            MPI_Recv(&start, 1, MPI_INT, MASTER, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            MPI_Recv(&end, 1, MPI_INT, MASTER, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            if (start >= end)
            {
                break;
            }

            int len = end - start;
            vector<int> seg1(len), seg2(len), result(len);

            MPI_Recv(seg1.data(), len, MPI_INT, MASTER, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            MPI_Recv(seg2.data(), len, MPI_INT, MASTER, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            static bool first_segment = true;
            if (first_segment && rank > 1)
            {
                MPI_Recv(&carry, 1, MPI_INT, rank - 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
                first_segment = false;
            }

            for (int i = 0; i < len; ++i)
            {
                int sum = seg1[i] + seg2[i] + carry;
                result[i] = sum % 10;
                carry = sum / 10;
            }

            if (rank < numProcesses - 1)
            {
                MPI_Send(&carry, 1, MPI_INT, rank + 1, 0, MPI_COMM_WORLD);
            }
            else
            {
                if (carry > 0)
                    result.push_back(carry);
            }

            int result_len = result.size();
            MPI_Send(&start, 1, MPI_INT, MASTER, 0, MPI_COMM_WORLD);
            MPI_Send(&end, 1, MPI_INT, MASTER, 0, MPI_COMM_WORLD);
            MPI_Send(result.data(), result_len, MPI_INT, MASTER, 0, MPI_COMM_WORLD);
        }
    }

    MPI_Finalize();
    return 0;
}
