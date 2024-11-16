#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <pthread.h>

typedef struct {
    int id;
    int n;
    FILE *f;
    pthread_mutex_t *mtx;
    pthread_barrier_t *bar;
} param;

void *f(void *arg) {
    param p = *((param*) arg);
    int max = 100, size = 0;
    char *buf = malloc(sizeof(char) * max);
    while(!feof(p.f)) {
        pthread_barrier_wait(p.bar);
        pthread_mutex_lock(p.mtx);
        char w[21];
        int repeats = rand() % 3 + 1;
        for(int i = 0; i < repeats; i++) {
            if(feof(p.f)) {
                break;
            } else {
                memset(w, 0, sizeof(char) * 21);
                fscanf(p.f, "%s", w);
                int word_len = strlen(w) + 1;
                if(size + word_len + 1 > max) {
                    max += max;
                    buf = realloc(buf, sizeof(char) * max);
                }
                sprintf(buf + size, "%s ", w);
                size += word_len;
            }
        }
        pthread_mutex_unlock(p.mtx);
        pthread_barrier_wait(p.bar);
    }
    buf[size] = 0;
    return buf;
}

int main(int argc, char *argv[]) {
    if(argc != 3) {
        printf("Please provide the number of threads and a filename\n");
        exit(1);
    }
    FILE *file = fopen(argv[2], "r");
    if(NULL == file) {
        perror("Error opening file");
        exit(1);
    }
    int n = atoi(argv[1]);
    srand(getpid());
    pthread_t *th = malloc(sizeof(pthread_t) * n);
    pthread_mutex_t *mtx = malloc(sizeof(pthread_mutex_t));
    pthread_barrier_t *bar = malloc(sizeof(pthread_barrier_t));
    pthread_mutex_init(mtx, NULL);
    pthread_barrier_init(bar, NULL, n);
    param *args = malloc(sizeof(param) * n);
    for(int i = 0; i < n; i++) {
        args[i].id = i;
        args[i].n = n;
        args[i].f = file;
        args[i].mtx = mtx;
        args[i].bar = bar;
        if(0 != pthread_create(&th[i], NULL, f, &args[i])) {
            perror("Error creating thread");
        }
    }
    for(int i = 0; i < n; i++) {
        char *result;
        if(0 != pthread_join(th[i], (void**) &result)) {
            perror("Error joining thread");
        } else {
            printf("Thread %d:\n   %s\n", i, result);
            free(result);
        }
    }
    fclose(file);
    pthread_mutex_destroy(mtx);
    pthread_barrier_destroy(bar);
    free(mtx);
    free(bar);
    free(th);
    free(args);
    return 0;
}