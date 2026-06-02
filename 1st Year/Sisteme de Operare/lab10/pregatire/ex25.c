#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <math.h>

int main() {
    srand(time(NULL));
    int fd1[2], fd2[2], fd3[2];
    pipe(fd1);
    pipe(fd2);
    pipe(fd3);

    if (fork() == 0) {
        // Procesul B
        close(fd1[0]);
        int num;
        while (1) {
            num = rand() % 200 + 1; // Numar aleator intre 1 si 200
            printf("Process B generated %d\n", num);
            write(fd1[1], &num, sizeof(num));
        }
        exit(0);
    }

    if (fork() == 0) {
        // Procesul C
        close(fd2[0]);
        int num;
        while (1) {
            num = rand() % 200 + 1; // Numar aleator intre 1 si 200
            printf("Process C generated %d\n", num);
            write(fd2[1], &num, sizeof(num));
        }
        exit(0);
    }

    if (fork() == 0) {
        // Procesul D
        close(fd1[1]);
        close(fd2[1]);
        close(fd3[1]);
        int numA, numB, numC;
        read(fd3[0], &numA, sizeof(numA));
        printf("Process D received %d from A\n", numA);
        while (1) {
            read(fd1[0], &numB, sizeof(numB));
            read(fd2[0], &numC, sizeof(numC));
            printf("Process D received %d from B and %d from C\n", numB, numC);
            int diff = abs(numB - numC);
            printf("Difference calculated by D is %d\n", diff);
            if (diff <= numA)
                break;
        }
        printf("Process D is stopping\n");
        exit(0);
    }

    // Procesul A
    close(fd1[1]);
    close(fd2[1]);
    close(fd3[0]);
    int num = rand() % 11 + 10; // Numar aleator intre 10 si 20
    printf("Process A generated %d\n", num);
    write(fd3[1], &num, sizeof(num));

    return 0;
}