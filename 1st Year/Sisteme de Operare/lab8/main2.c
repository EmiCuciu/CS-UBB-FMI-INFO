#include <pthread.h>
#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>


void *t(void *a){
    return (void*)5;
}

int main(){
    void *rezultat;
    pthread_t pt;
    pthread_create(&pt, NULL, &t, NULL);
    pthread_join(pt, &rezultat);

    printf("%d\n", rezultat);
}

/*
#include <stdio.h>
#include <pthread.h>

void *t(void *a){
    static int val = 5;
    return &val;
}

int main(){
    void *rezultat;
    pthread_t pt;
    pthread_create(&pt, NULL, &t, NULL);
    pthread_join(pt, &rezultat);

    printf("%d\n", *(int*)rezultat);
    return 0;
}
*/