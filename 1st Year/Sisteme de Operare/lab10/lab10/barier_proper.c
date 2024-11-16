#include <pthread.h>   
#include <stdio.h>

#define NUM_THREADS 3

pthread_barrier_t theBarrier;

void *thread(void *f)
{
    sleep(((int)f+1)*2);

    printf("Thread %d is waiting\n", f);
    pthread_barrier_wait(&theBarrier);
    printf("Thread %d is resumed\n", f);
}

int main()
{
    pthread_barrier_init(&theBarrier, NULL, NUM_THREADS);

    pthread_t threads[NUM_THREADS];
    for (int i = 0; i < NUM_THREADS; i++)
    {
        pthread_create(&threads[i], NULL, &thread, i);
    }

    for (int i = 0; i < NUM_THREADS; i++)
    {
        pthread_join(threads[i], NULL);
    }
}