#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

typedef struct {
    int team;
    int number;
    pthread_cond_t *cond;
    pthread_mutex_t *mutex;
    int *next;
} ThreadData;

void *run(void *arg) {
    ThreadData *data = (ThreadData *)arg;
    usleep((rand() % 101 + 100) * 1000);
    pthread_mutex_lock(data->mutex);
    if (*data->next == data->number) {
        (*data->next)++;
        pthread_cond_broadcast(data->cond);
    }
    while (*data->next != data->number + 1) {
        pthread_cond_wait(data->cond, data->mutex);
    }
    pthread_mutex_unlock(data->mutex);
    if (data->number == 3) {
        printf("Team %d finished\n", data->team);
    }
    return NULL;
}

int main() {
    int n;
    scanf("%d", &n);
    pthread_t *threads = malloc(sizeof(pthread_t) * n * 4);
    ThreadData *data = malloc(sizeof(ThreadData) * n * 4);
    pthread_cond_t *conds = malloc(sizeof(pthread_cond_t) * n);
    pthread_mutex_t *mutexes = malloc(sizeof(pthread_mutex_t) * n);
    int *nexts = malloc(sizeof(int) * n);
    for (int i = 0; i < n; i++) {
        pthread_cond_init(&conds[i], NULL);
        pthread_mutex_init(&mutexes[i], NULL);
        nexts[i] = 0;
        for (int j = 0; j < 4; j++) {
            data[i * 4 + j].team = i;
            data[i * 4 + j].number = j;
            data[i * 4 + j].cond = &conds[i];
            data[i * 4 + j].mutex = &mutexes[i];
            data[i * 4 + j].next = &nexts[i];
            pthread_create(&threads[i * 4 + j], NULL, run, &data[i * 4 + j]);
        }
    }
    for (int i = 0; i < n * 4; i++) {
        pthread_join(threads[i], NULL);
    }
    free(threads);
    free(data);
    free(conds);
    free(mutexes);
    free(nexts);
    return 0;
}   