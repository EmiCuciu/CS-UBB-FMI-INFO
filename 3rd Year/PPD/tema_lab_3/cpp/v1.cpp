#include <mpi/mpi.h>
#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>

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


int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int myId, numProcesses;
    MPI_Comm_rank(MPI_COMM_WORLD, &myId);
    MPI_Comm_size(MPI_COMM_WORLD, &numProcesses);

    const int MASTER = 0;

    if (myId == MASTER) {
        cout << "[MASTER] Am intrat in Task" << myId << endl << flush;

        // ifstream fin1("v1_data/test1/N_1.txt");
        // ifstream fin2("v1_data/test1/N_2.txt");
        // ofstream fout("v1_data/test1/N_3_paralel.txt");


        // ifstream fin1("v1_data/test2/N_1.txt");
        // ifstream fin2("v1_data/test2/N_2.txt");
        // ofstream fout("v1_data/test2/N_3_paralel.txt");


        ifstream fin1("v1_data/test3/N_1.txt");
        ifstream fin2("v1_data/test3/N_2.txt");
        ofstream fout("v1_data/test3/N_3_paralel.txt");

        if (!fin1 || !fin2) {
            cerr << "Nu am putut deschide fisierele de intrare!" << endl;
            MPI_Finalize();
            return 1;
        }

        int N1, N2;
        fin1 >> N1;
        fin2 >> N2;
        int N = max(N1, N2);

        // Read and reverse numbers (least significant digit first)
        vector<int> num1(N, 0), num2(N, 0);
        for (int i = 0; i < N1; ++i) fin1 >> num1[N1 - 1 - i];
        for (int i = 0; i < N2; ++i) fin2 >> num2[N2 - 1 - i];

        int segSize = N / (numProcesses - 1);
        int rest = N % (numProcesses - 1);

        int start = 0;
        int id_proces_curent = 1;

        while (start < N) {
            int len = segSize + (id_proces_curent <= rest ? 1 : 0);
            int end = start + len;

            vector<int> seg1(len), seg2(len);
            for (int i = 0; i < len; ++i) {
                seg1[i] = num1[start + i];
                seg2[i] = num2[start + i];
            }

            MPI_Send(&start, 1, MPI_INT, id_proces_curent, 0, MPI_COMM_WORLD);
            MPI_Send(&end, 1, MPI_INT, id_proces_curent, 0, MPI_COMM_WORLD);
            MPI_Send(seg1.data(), len, MPI_INT, id_proces_curent, 0, MPI_COMM_WORLD);
            MPI_Send(seg2.data(), len, MPI_INT, id_proces_curent, 0, MPI_COMM_WORLD);

            cout << "[MASTER] Trimitem segmentul [" << start << "," << end << "] catre Task " << id_proces_curent << endl << flush;

            start = end;
            id_proces_curent++;
            if (id_proces_curent == numProcesses) id_proces_curent = 1;
        }

        for (int id = 1; id < numProcesses; ++id) {
            cout << "[MASTER] Trimitem semnal de terminare in Task " << id << endl << flush;
            int dummy = 0;
            MPI_Send(&dummy, 1, MPI_INT, id, 0, MPI_COMM_WORLD);
            MPI_Send(&dummy, 1, MPI_INT, id, 0, MPI_COMM_WORLD);
        }

        // Collect results in order
        vector<int> N_3(N + 1, 0);
        int segments_received = 0;

        while (segments_received < N) {
            int seg_start, seg_end;
            MPI_Status status;

            MPI_Recv(&seg_start, 1, MPI_INT, MPI_ANY_SOURCE, 0, MPI_COMM_WORLD, &status);
            MPI_Recv(&seg_end, 1, MPI_INT, status.MPI_SOURCE, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            MPI_Status stat;
            int elements_available;
            MPI_Probe(status.MPI_SOURCE, 0, MPI_COMM_WORLD, &stat);
            MPI_Get_count(&stat, MPI_INT, &elements_available);

            vector<int> result(elements_available);
            MPI_Recv(result.data(), elements_available, MPI_INT, status.MPI_SOURCE, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            // Store results in correct position
            for (int i = 0; i < result.size(); ++i) {
                N_3[seg_start + i] = result[i];
            }

            segments_received += (seg_end - seg_start);
        }

        // Remove leading zeros
        int actual_size = N_3.size();
        while (actual_size > 1 && N_3[actual_size - 1] == 0) {
            actual_size--;
        }
        N_3.resize(actual_size);

        fout << actual_size << "\n";
        for (int i = actual_size - 1; i >= 0; --i) {
            fout << N_3[i];
            if (i > 0) fout << " ";
        }
        fout << "\n";

        fin1.close();
        fin2.close();
        fout.close();
        cout << "Inchidere program.\n" << flush;

    } else {
        int carry = 0;
        while (true) {
            cout << "[TASK " << myId << "] Am intrat in Task" << myId << endl << flush;
            MPI_Status status;
            int start, end;

            MPI_Recv(&start, 1, MPI_INT, MASTER, 0, MPI_COMM_WORLD, &status);
            MPI_Recv(&end, 1, MPI_INT, MASTER, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            if (start >= end) {
                cout << "[TASK " << myId << "] termina executia." << endl << flush;
                break;
            }

            int len = end - start;
            vector<int> seg1(len), seg2(len), result(len);

            MPI_Recv(seg1.data(), len, MPI_INT, MASTER, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            MPI_Recv(seg2.data(), len, MPI_INT, MASTER, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            static bool first_segment = true;
            if (first_segment && myId > 1) {
                MPI_Recv(&carry, 1, MPI_INT, myId - 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
                first_segment = false;
            }

            for (int i = 0; i < len; ++i) {
                int s = seg1[i] + seg2[i] + carry;
                result[i] = s % 10;
                carry = s / 10;
            }

            if (myId < numProcesses - 1) {
                MPI_Send(&carry, 1, MPI_INT, myId + 1, 0, MPI_COMM_WORLD);
            } else {
                if (carry > 0)
                    result.push_back(carry);
            }

            int result_len = result.size();
            MPI_Send(&start, 1, MPI_INT, MASTER, 0, MPI_COMM_WORLD);
            MPI_Send(&end, 1, MPI_INT, MASTER, 0, MPI_COMM_WORLD);
            MPI_Send(result.data(), result_len, MPI_INT, MASTER, 0, MPI_COMM_WORLD);
            cout << "[TASK " << myId << "] Trimite noile valori catre MASTER." << endl << flush;
        }
    }

    // compareFiles("v1_data/test1/N_3.txt", "v1_data/test1/N_3_paralel.txt");
    // compareFiles("v1_data/test2/N_3.txt", "v1_data/test2/N_3_paralel.txt");
    compareFiles("v1_data/test3/N_3.txt", "v1_data/test3/N_3_paralel.txt");

    MPI_Finalize();
    return 0;
}
