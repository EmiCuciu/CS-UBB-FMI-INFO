#include <pthread.h>
#include <stdio.h>

pthread_cond_t theVariabila;
pthread_mutex_t theMutex;

void* producator(void *f){
    printf("Hello from producator\n");
    getchar();
    pthread_mutex_lock(&theMutex);
    pthread_cond_signal(&theVariabila);
    pthread_mutex_unlock(&theMutex);
}

int main(){
    pthread_t thread;

    pthread_mutex_init(&theMutex, NULL);
    pthread_create(&thread, NULL, producator, NULL);

    pthread_mutex_lock(&theMutex);
    printf("Customer waiting for profuct\n");
    pthread_cond_wait(&theVariabila, &theMutex);
    printf("Customer awaken!\n");   
    pthread_mutex_unlock(&theMutex);

    pthread_join(thread, NULL);
    return 0;
}