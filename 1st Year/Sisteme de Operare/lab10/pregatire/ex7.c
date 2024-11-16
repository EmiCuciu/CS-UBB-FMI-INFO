#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <time.h>

int contains7(int num) {
    while (num) {
        if (num % 10 == 7)
            return 1;
        num /= 10;
    }
    return 0;
}

int main(int argc, char *argv[]) {
    if (argc != 2) {
        printf("Usage: %s <number_of_processes>\n", argv[0]);
        return 1;
    }

    int N = atoi(argv[1]);
    int fd[N][2];
    srand(time(NULL));

    for (int i = 0; i < N; i++) {
        pipe(fd[i]);
    }

    for (int i = 0; i < N; i++) {
        if (fork() == 0) {
            close(fd[i][1]);
            if (i != 0)
                close(fd[i - 1][0]);

            int num;
            while (1) {
                read(fd[i][0], &num, sizeof(num));
                if (num % 7 == 0 || contains7(num)) {
                    if ((rand() % 3) == 0) {
                        printf("boltz\n");
                        exit(0);
                    }
                } else {
                    printf("%d\n", num);
                    num++;
                    write(fd[(i + 1) % N][1], &num, sizeof(num));
                }
            }
        }
    }

    int start = 1;
    write(fd[0][1], &start, sizeof(start));

    for (int i = 0; i < N; i++) {
        wait(NULL);
    }

    return 0;
}