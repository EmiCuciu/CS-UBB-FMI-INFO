#include <iostream>
#include <mpi.h>

const int N = 10;

void print(int* a, int n)
{
    for (int i = 0; i < n; ++i)
    {
        std::cout << a[i] << ' ';
    }
    std::cout << std::endl;
}

int main(int argc, char** argv)
{
    // int a[N];
    // int b[N];
    // int c[N];
    //
    // MPI_Status status;
    //
    //
    // MPI_Init(&argc, &argv);
    //
    // int rank, size;
    // MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    // MPI_Comm_size(MPI_COMM_WORLD, &size);
    //
    //
    // if (rank == 0)
    // {
    //     for (int i = 0; i < N; i++)
    //     {
    //         a[i] = rand() % 10;
    //         b[i] = rand() % 10;
    //     }
    //
    //     int start = 0;
    //     int cat = N / (size - 1);
    //     int rest = N % (size - 1);
    //     int end = cat;
    //
    //     for (int i = 1; i < size; i++)
    //     {
    //         if (rest > 0)
    //         {
    //             end++;
    //             rest--;
    //         }
    //
    //         MPI_Send(&start, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
    //
    //         MPI_Send(&end, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
    //
    //         MPI_Send(&a[start], end - start, MPI_INT, i, 0, MPI_COMM_WORLD);
    //
    //         MPI_Send(&b[start], end - start, MPI_INT, i, 0, MPI_COMM_WORLD);
    //
    //         start = end;
    //         end = start + cat;
    //     }
    //
    //
    //     for (int i = 1; i < size; i++)
    //     {
    //         MPI_Recv(&start, 1, MPI_INT, i, 0, MPI_COMM_WORLD, &status);
    //
    //         MPI_Recv(&end, 1, MPI_INT, i, 0, MPI_COMM_WORLD, &status);
    //
    //         MPI_Recv(&c[start], end - start, MPI_INT, i, 0, MPI_COMM_WORLD, &status);
    //     }
    //
    //     print(a,N);
    //     print(b,N);
    //     print(c,N);
    // }
    // else
    // {
    //     int start, end;
    //
    //     MPI_Recv(&start, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
    //
    //     MPI_Recv(&end, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
    //
    //     MPI_Recv(&a[start], end - start, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
    //
    //     MPI_Recv(&b[start], end - start, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
    //
    //     for (int i = start; i < end; i++)
    //     {
    //         c[i] = a[i] + b[i];
    //     }
    //
    //     MPI_Send(&start, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
    //
    //     MPI_Send(&end, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
    //
    //     MPI_Send(&c[start], end - start, MPI_INT, 0, 0, MPI_COMM_WORLD);
    // }
    //
    //
    // MPI_Finalize();


    int namelen, myid, numprocs;
    MPI_Init(&argc, &argv); // aici initializam mpi -> se creaza un nou communicator, rank process si size
    MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
    MPI_Comm_rank(MPI_COMM_WORLD, &myid);

    int a[N];
    int b[N];
    int c[N];


    MPI_Status status;
    //master are rank 0 by default
    if (myid == 0)
    {
        for (int i = 0; i < N; i++)
        {
            a[i] = rand() % 10;
            b[i] = rand() % 10;
        }
    }

    //aloc vectori auxiliari
    int sizeChunk = N / numprocs;

    int* auxA = new int[sizeChunk];
    int* auxB = new int[sizeChunk];
    int* auxC = new int[sizeChunk];


    MPI_Scatter(a, sizeChunk, MPI_INT, auxA, sizeChunk, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Scatter(b, sizeChunk, MPI_INT, auxB, sizeChunk, MPI_INT, 0, MPI_COMM_WORLD);

    for (int i = 0; i < sizeChunk; i++)
    {
        auxC[i] = auxA[i] + auxB[i];
    }

    MPI_Gather(auxC, sizeChunk, MPI_INT, c, sizeChunk, MPI_INT, 0, MPI_COMM_WORLD);

    if (myid == 0)
    {
        print(a, N);
        print(b, N);
        print(c, N);
    }

    MPI_Finalize();

    /**
     * ! pentru -n egal cu ceva ce da cu rest trebuie facut cu scatterV , gatterV
     **/

    return 0;
}
