#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <time.h>

typedef struct {
    int *curr, index, team;
    pthread_mutex_t *m;
    pthread_cond_t *c;
    pthread_barrier_t *b;
} data;

void *runner(void *arg) {
    data d = *((data *) arg);
    pthread_barrier_wait(d.b);
    pthread_mutex_lock(d.m);
    while(*d.curr != d.index) pthread_cond_wait(d.c, d.m);
    usleep((random() % 101 + 100) * 1000);
    (*d.curr)++;
    if(d.index == 3)
        printf("Team %d finished!\n", d.team);
    else {
        pthread_cond_broadcast(d.c);
    }
    pthread_mutex_unlock(d.m);
    return NULL;
}

int main(int argc, char *argv[]) {
    int n;
    printf("n = ");
    scanf("%d", &n);
    srandom(time(NULL));
    int *teams = malloc(sizeof(int) * n);
    int **currents = malloc(sizeof(int*) * n);
    data **args = malloc(sizeof(data*) * n);
    pthread_t **th = malloc(sizeof(pthread_t*) * n);
    pthread_mutex_t **mutexes = malloc(sizeof(pthread_mutex_t*) * n);
    pthread_cond_t **conds = malloc(sizeof(pthread_cond_t*) * n);
    int i, j;
    for(i = 0; i < n; i++) {
        args[i] = malloc(sizeof(data) * 4);
        th[i] = malloc(sizeof(pthread_t) * 4);
        mutexes[i] = malloc(sizeof(pthread_mutex_t));
        pthread_mutex_init(mutexes[i], NULL);
        conds[i] = malloc(sizeof(pthread_cond_t));
        pthread_cond_init(conds[i], NULL);
        teams[i] = i;
        currents[i] = malloc(sizeof(int));
        *currents[i] = 0;
    }
    pthread_barrier_t *b = malloc(sizeof(pthread_barrier_t));
    pthread_barrier_init(b, NULL, 4 * n);

    for(i = 0; i < 4; i++) {
        for(j = 0; j < n; j++) {
            args[j][i].b = b;
            args[j][i].m = mutexes[j];
            args[j][i].c = conds[j];
            args[j][i].curr = currents[j];
            args[j][i].index = i;
            args[j][i].team = teams[j];
            pthread_create(&th[j][i], NULL, runner, &args[j][i]);
        }
    }

    for(i = 0; i < 4; i++) {
        for(j = 0; j < n; j++) {
            pthread_join(th[j][i], NULL);
        }
    }

    pthread_barrier_destroy(b);
    free(b);
    for(i = 0; i < n; i++) {
        pthread_mutex_destroy(mutexes[i]);
        pthread_cond_destroy(conds[i]);
        free(mutexes[i]);
        free(conds[i]);
        free(currents[i]);
        free(th[i]);
        free(args[i]);
    }
    free(teams);
    free(currents);
    free(mutexes);
    free(conds);
    free(th);
    free(args);
    return 0;
}