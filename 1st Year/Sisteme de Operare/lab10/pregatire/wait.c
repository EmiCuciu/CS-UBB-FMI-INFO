#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main(int argc, char** argv) {
    printf("parinte\n\n");
    int pid = fork();
    printf("parinte\n\n");
    int n;
    if (pid == 0) {
        n = 1;
    }
    else {
        n = 6;
    }

    if (pid != 0) {
        wait();
    }
    int i;
    for (i = n; i < n + 5; i++) {
        printf("%d ", i);
    }
    if (pid == 0) {
        printf("\n");
    }
    if (pid != 0) {
        printf("\n");
    }
    return 0;
}