#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/wait.h>

int main() {
    int fd[2];
    pipe(fd);

    int sum = 1;
    printf("Sunt procesul 1\n");
    for (int id=2; id<=15; id++) {
        if (fork() == 0) {
            printf("Sunt procesul %d! Parintii mei sunt: ", id);
            for (int j=id/2; j>0; j/=2) {
                printf("%d,", j);
            }
            printf("\b!\n");
            write(fd[1], &id, sizeof(id));
            exit(0);
        } else {
            wait(NULL);
        }
    }

    for (int i=0; i<14; i++) {
        int child_id;
        read(fd[0], &child_id, sizeof(child_id));
        sum += child_id;
    }

    printf("Suma ID-urilor este: %d\n", sum);

    return 0;
}