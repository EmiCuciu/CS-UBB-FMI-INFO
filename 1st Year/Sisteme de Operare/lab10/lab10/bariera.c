#include <pthread.h>
#include <stdio.h>

#define NUM_THREADS 3

pthread_mutex_t theMutex;
pthread_cond_t theVariabila;
int threaduriWaiting;

void *thread(void *f)
{
    sleep(((int)f+1)*2);
    pthread_mutex_lock(&theMutex);
    /// ==== ^^^^  workload dinamic
    printf("Threaduri waiting: %d\n", threaduriWaiting);

    if (threaduriWaiting < NUM_THREADS - 1)
    {
        threaduriWaiting++;
        printf("Thread %d is waiting\n", f);
        pthread_cond_wait(&theVariabila, &theMutex);
        printf("Thread %d is resumed\n", f);
    }
    else
    {
        threaduriWaiting = 0;
        pthread_cond_broadcast(&theVariabila);
        printf("Thread %d signalled\n", f);
    }

    pthread_mutex_unlock(&theMutex);
}

int main()
{
    threaduriWaiting = 0;
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