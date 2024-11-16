#include <pthread.h>
#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>

/*
void* tramvai(void *arg){
    int *f = arg;
    printf("%d\n", *f);
}
*/

/*
int main(){

    int numar = 5;
    pthread_t t;
    printf("Before create: %d\n",numar);
    pthread_create (&t, NULL, &tramvai, &numar);
    for(int i = 0; i < 1000000000; i++){}; // wait for thread to finish
    printf("After create: %d\n",numar);
    pthread_join(t, NULL);
}
*/

/*
int main(){
    int numar = 5;
    int n1 = 5;
    int n2 = 6;
    int n3 = 7;
    pthread_t t[10];
    pthread_create (&t[0], NULL, &tramvai, &numar);
    pthread_create (&t[1], NULL, &tramvai, &n1);
    pthread_create (&t[2], NULL, &tramvai, &n2);
    pthread_create (&t[3], NULL, &tramvai, &n3);
    getchar();
}
*/

pthread_mutex_t theMutex;


void *tramvai(void *arg){
    pthread_mutex_lock(&theMutex);
    sleep(5);
    printf("%d\n", *(int*)arg);
    pthread_mutex_unlock(&theMutex);
}

int main(){
    pthread_mutex_init(&theMutex, NULL);

    int n1 = 5;
    int n2 = 6;
    int n3 = 7;

    pthread_t t[10];
    pthread_create (&t[0], NULL, &tramvai, &n1);
    pthread_create (&t[1], NULL, &tramvai, &n2);
    pthread_create (&t[2], NULL, &tramvai, &n3);

    sleep(1);
    pthread_mutex_lock(&theMutex);
    printf("Hello\n");
    pthread_mutex_unlock(&theMutex);

}