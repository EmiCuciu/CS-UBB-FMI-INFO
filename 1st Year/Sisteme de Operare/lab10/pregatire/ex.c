#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

int main(int argc, char *argv[]) {
    if (argc != 2) {
        printf("Usage: %s <number_of_children>\n", argv[0]);
        return 1;
    }

    int n = atoi(argv[1]);
    int pids[n];

    for (int i = 0; i < n; i++) {
        pids[i] = fork();

        if (pids[i] < 0) {
            perror("fork");
            return 1;
        }

        if (pids[i] == 0) {
            printf("Child process PID: %d, parent PID: %d\n", getpid(), getppid());
            exit(0);
        }
    }

    printf("Parent process PID: %d, child PIDs: ", getpid());
    for (int i = 0; i < n; i++) {
        printf("%d ", pids[i]);
    }
    printf("\n");

    for (int i = 0; i < n; i++) {
        wait(NULL);
    }

    return 0;
}