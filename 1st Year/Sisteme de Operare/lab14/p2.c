#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <pthread.h>
#include <time.h>

typedef struct {
    int id;
    int *flag;
    pthread_mutex_t *m;
    pthread_barrier_t *b;
} data;

void *f(void *arg) {
    data d = *((data*)arg);
    pthread_barrier_wait(d.b);
    int nr;
    while(1) {
        nr = random() % 111112;
        pthread_mutex_lock(d.m);
        if(*(d.flag) == 0) {
            printf("T[%d] -> %d\n", d.id, nr);
            if(nr % 1001 == 0) {
                *(d.flag) = 1;
                break;
            }
        } else {
            break;
        }
        pthread_mutex_unlock(d.m);
        usleep(100);
    }
    pthread_mutex_unlock(d.m);
    return NULL;
}

int main(int argc, char *argv[]) {
    if(argc < 2) {
        fprintf(stderr, "Provide an argument!\n");
        exit(1);
    }
    srandom(time(NULL));
    int n = atoi(argv[1]);
    int *flag = malloc(sizeof(int));
    *flag = 0;
    pthread_mutex_t *m = malloc(sizeof(pthread_mutex_t));
    pthread_barrier_t *b = malloc(sizeof(pthread_barrier_t));
    pthread_mutex_init(m, NULL);
    pthread_barrier_init(b, NULL, n);
    pthread_t *th = malloc(sizeof(pthread_t) * n);
    data *args = malloc(sizeof(data) * n);
    int i;
    for(i = 0; i < n; i++) {
        args[i].id = i;
        args[i].flag = flag;
        args[i].m = m;
        args[i].b = b;
        pthread_create(&th[i], NULL, f, &args[i]);
    }

    for(i = 0; i < n; i++) {
        pthread_join(th[i], NULL);
    }

    pthread_barrier_destroy(b);
    pthread_mutex_destroy(m);
    free(th);
    free(args);
    free(b);
    free(m);
    free(flag);
    return 0;
}