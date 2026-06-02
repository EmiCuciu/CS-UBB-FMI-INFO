#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>   
#include <math.h>

int main() {
    srand(time(NULL));
    int fd[2];
    pipe(fd);

    if (fork() == 0) {
        // Procesul B
        close(fd[1]);
        int numB = rand() % 901 + 100; // Numar aleator intre 100 si 1000
        printf("Process B has generated %d\n", numB);

        int numA;
        while (1) {
            read(fd[0], &numA, sizeof(numA));
            int diff = abs(numB - numA);
            printf("B received %d; difference: %d\n", numA, diff);
            if (diff < 50)
                break;
        }
        exit(0);
    } else {
        // Procesul A
        close(fd[0]);
        int count = 0;
        int numA,numB;
        do {
            numA = rand() % 1001 + 50; // Numar aleator intre 50 si 1050
            write(fd[1], &numA, sizeof(numA));
            count++;
        } while (abs(numA - numB) >= 50);
        printf("Process A has generated %d numbers\n", count);
    }

    return 0;
}