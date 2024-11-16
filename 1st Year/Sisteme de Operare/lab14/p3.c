#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <time.h>

int *numbers;
pthread_mutex_t *locks;
pthread_barrier_t barrier;
int n;

void *thread_func(void *arg) {
    int id = *(int*)arg;
    numbers[id] = (rand() % 11) + 10;

    pthread_barrier_wait(&barrier);

    for (;;) {
        pthread_mutex_lock(&locks[id]);

        if (numbers[id] <= 0) {
            pthread_mutex_unlock(&locks[id]);
            break;
        }

        for (int i = 0; i < n; i++) {
            if (i != id) {
                pthread_mutex_lock(&locks[i]);
                if (numbers[i] > 0) numbers[i]--;
                pthread_mutex_unlock(&locks[i]);
            }
        }

        int all_zero = 1;
        for (int i = 0; i < n; i++) {
            if (i != id && numbers[i] > 0) {
                all_zero = 0;
                break;
            }
        }

        pthread_mutex_unlock(&locks[id]);

        if (all_zero) break;
    }

    return NULL;
}

int main() {
    srand(time(NULL));

    printf("Enter a number: ");
    scanf("%d", &n);

    numbers = malloc(n * sizeof(int));
    locks = malloc(n * sizeof(pthread_mutex_t));
    pthread_t *threads = malloc(n * sizeof(pthread_t));
    int *ids = malloc(n * sizeof(int));

    pthread_barrier_init(&barrier, NULL, n);

    for (int i = 0; i < n; i++) {
        pthread_mutex_init(&locks[i], NULL);
        ids[i] = i;
        pthread_create(&threads[i], NULL, thread_func, &ids[i]);
    }

    for (int i = 0; i < n; i++) {
        pthread_join(threads[i], NULL);
    }

    for (int i = 0; i < n; i++) {
        printf("%d ", numbers[i]);
    }
    printf("\n");

    free(numbers);
    free(locks);
    free(threads);
    free(ids);

    return 0;
}