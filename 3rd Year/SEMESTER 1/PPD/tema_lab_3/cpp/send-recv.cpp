#include <mpi/mpi.h>
#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <algorithm>

using namespace std;

void g(string& s, int n, int& countA, int& countB)
{
    int len = s.length();

    if (len < n)
    {
        reverse(s.begin(), s.end());
        s.push_back('#');
        countA++;
    }
    else
    {
        s = s.substr(0, n);
        s.insert(0, "!");
        countB++;
    }
}

int main(int argc, char** argv)
{
    MPI_Init(&argc, &argv);

    int rank, p;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &p);

    int n = 0;
    int N = 0;
    vector<string> strings;

    if (rank == 0)
    {
        ifstream in("date.txt");
        if (!in.is_open())
        {
            cerr << "Eroare la deschiderea date.txt" << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        in >> N;

        strings.resize(N);
        for (int i = 0; i < N; i++)
        {
            getline(in, strings[i]);
        }

        in >> n;
        in.close();
    }

    MPI_Bcast(&n, 1, MPI_INT, 0, MPI_COMM_WORLD);

    MPI_Bcast(&N, 1, MPI_INT, 0, MPI_COMM_WORLD);

    int cat = N / p;
    int rest = N % p;

    int my_chunk = cat + (rank < rest ? 1 : 0);

    vector<string> my_strings(my_chunk);

    if (rank == 0)
    {
        int current_idx = 0;

        for (int r = 0; r < p; r++)
        {
            int count_for_proc = cat + (r < rest ? 1 : 0);

            if (r != 0)
            {
                for (int i = 0; i < count_for_proc; i++)
                {
                    string s = strings[current_idx + i];
                    int len = s.length();

                    MPI_Send(&len, 1, MPI_INT, r, 0, MPI_COMM_WORLD);
                    MPI_Send(s.c_str(), len, MPI_CHAR, r, 1, MPI_COMM_WORLD);
                }
            }
            else
            {
                for (int i = 0; i < count_for_proc; i++)
                {
                    my_strings[i] = strings[current_idx + i];
                }
            }
            current_idx += count_for_proc;
        }
    }
    else
    {
        for (int i = 0; i < my_chunk; i++)
        {
            int len;
            MPI_Recv(&len, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            char* buffer = new char[len + 1];
            MPI_Recv(buffer, len, MPI_CHAR, 0, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            my_strings[i] = string(buffer);
            delete[] buffer;
        }
    }


    cout << "Ruleaza\n";
    ofstream out("result.txt");
    out << "Ruleaza\n";

    MPI_Finalize();
    return 0;
}
