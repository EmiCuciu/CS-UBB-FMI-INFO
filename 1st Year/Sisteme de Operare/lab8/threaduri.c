#include <stdio.h>
#include <pthread.h>
int x = 100;
pthread_rwlock_t rwl;
void *shopper(void *a)
{
    long id = (long)a;
    if (id % 10 == 0)
    { // buyer
        pthread_rwlock_wrlock(&rwl);
        if (x > 0)
        {
            x--;
            printf("%ld: bought %d\n", id, x + 1);
        }
        else
        {
            printf("%ld: none left\n", id);
        }
        pthread_rwlock_unlock(&rwl);
    }
    else
    { // onlooker
        pthread_rwlock_rdlock(&rwl);
        printf("%ld: available %d\n", id, x);
        pthread_rwlock_unlock(&rwl);
    }
    return NULL;
}
int main(int argc, char **argv)
{
    int i;
    pthread_t t[200];
    pthread_rwlock_init(&rwl, NULL);
    for (i = 0; i < 200; i++)
    {
        pthread_create(&t[i],
                       NULL,
                       shopper,
                       (void *)(long)i);
    }
    for (i = 0; i < 200; i++)
    {
        pthread_join(t[i], NULL);
    }
    pthread_rwlock_destroy(&rwl);
    return 0;
}