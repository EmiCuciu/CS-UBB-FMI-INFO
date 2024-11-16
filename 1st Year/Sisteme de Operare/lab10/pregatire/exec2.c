#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/time.h>
#include <sys/wait.h>

int main(int argc, char **argv)
{
    struct timeval t0, t1;
    double duration;
    int status;

    if (argc < 2)
    {
        fprintf(stderr, "No command provived.\n");
        exit(1);
    }

    gettimeofday(&t0, NULL);
    if (fork() == 0)
    {
        execvp(argv[1], argv + 1);
        exit(1);
    }

    wait(&status);
    gettimeofday(&t1, NULL);

    duration = ((t1.tv_sec - t0.tv_sec)*1000.0 + (t1.tv_usec - t0.tv_usec)/1000.0)/1000.0;
    printf("Duration: %lf seconds\n", duration);

    return WEXITSTATUS(status);
}