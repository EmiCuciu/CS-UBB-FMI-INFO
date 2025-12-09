#include <mpi/mpi.h>
#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>

using namespace std;


void read_from_file(const string& filename, vector<char>& data)
{
    ifstream in(filename);
    if (!in.is_open())
    {
        cerr << "Eroare: Nu pot deschide fisierul " << filename << endl;
        MPI_Abort(MPI_COMM_WORLD, 1);
    }

    int n;



    in.close();

}

void write_to_file(const string& filename, const vector<int>& data)
{
    ofstream out(filename);
    if (!out.is_open())
    {
        cerr << "Eroare la scriere in " << filename << endl;
        return;
    }

    out << data.size() << "\n";
    // ATENTIE: Daca ai inversat la citire, scrie invers aici!
    // for (int i = data.size() - 1; i >= 0; --i) out << data[i] << " ";

    // Scriere normala:
    for (int x : data)
    {
        out << x << " ";
    }
    out << endl;
    out.close();
}


int main(int argc, char** argv)
{
    MPI_Init(&argc, &argv);
    int rank, p;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &p);

    // Variabile comune
    int chunk_size = 0;
    vector<int> full_data; // Doar pe Rank 0
    vector<int> final_results; // Doar pe Rank 0

    // --- 1. PREGATIRE DATE (MASTER) ---
    if (rank == 0)
    {
        read_from_file("input.txt", full_data); //

        int n = full_data.size();

        // PADDING: Facem N divizibil cu P
        if (n % p != 0)
        {
            int needed = p - (n % p);
            full_data.resize(n + needed, 0); // completam cu 0 (element neutru)
        }

        chunk_size = full_data.size() / p;
        final_results.resize(full_data.size());

        cout << "Master: Date citite. Chunk size: " << chunk_size << endl;
    }

    // --- 2. COMUNICARE MARIME (BCAST) ---
    MPI_Bcast(&chunk_size, 1, MPI_INT, 0, MPI_COMM_WORLD);

    // --- 3. ALOCARE LOCALA ---
    vector<int> local_data(chunk_size);
    vector<int> local_res(chunk_size); // Sau int local_res daca e reducere

    // --- 4. DISTRIBUIRE DATE (SCATTER) ---
    MPI_Scatter(full_data.data(), chunk_size, MPI_INT,
                local_data.data(), chunk_size, MPI_INT,
                0, MPI_COMM_WORLD);

    // --- 5. PRELUCRARE (YOUR CODE HERE) ---
    for (int i = 0; i < chunk_size; i++)
    {
        // === LOGICA EXAMENULUI AICI ===
        local_res[i] = local_data[i] * 2; // Exemplu: dublare
        // ==============================
    }

    // --- 6. COLECTARE (GATHER) ---
    MPI_Gather(local_res.data(), chunk_size, MPI_INT,
               final_results.data(), chunk_size, MPI_INT,
               0, MPI_COMM_WORLD);

    // --- 7. FINALIZARE (MASTER) ---
    if (rank == 0)
    {
        // Optional: Elimina padding-ul adaugat
        // int n_real = ...;
        // final_results.resize(n_real);

        write_to_file("output.txt", final_results);
        cout << "Gata! Rezultat scris." << endl;
    }

    MPI_Finalize();
    return 0;
}
