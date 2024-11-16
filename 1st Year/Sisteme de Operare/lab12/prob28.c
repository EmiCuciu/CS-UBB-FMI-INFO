#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>
#include <limits.h>

int theN;
int* theSir;

pthread_mutex_t theMutex;

int getRandom(int min, int max)
{
    return rand() % (max + 1) + min;
}

char isSorted()
{
    for (int i = 0;i < theN - 1;i++)
    {
        if (theSir[i + 1] < theSir[i])
        {
            return 0;
        }
    }
    return 1;
}
void* thePrinter(void* f)
{
    while (1)
    {
        int shouldStop = 0;
        pthread_mutex_lock(&theMutex);
        if (isSorted())
        {
            shouldStop = 1;
            for (int i = 0;i < theN;i++)
            {
                printf("%d ", theSir[i]);
            }
            printf("\n");
        }
        pthread_mutex_unlock(&theMutex);
        if (shouldStop == 1) break;
    }
    return NULL;
}
void* theFunctie(void* f)
{
    while (1)
    {
        int shouldStop = 1;
        pthread_mutex_lock(&theMutex);
        if (!isSorted())
        {
            shouldStop = 0;

            int i = getRandom(0, theN - 1);
            int j;
            do
            {
                j = getRandom(0, theN - 1);
            } while (j == i);

            int aux;
            if (i < j && theSir[i] > theSir[j])
            {
                aux = theSir[i];
                theSir[i] = theSir[j];
                theSir[j] = aux;
            }
            else if (i > j && theSir[i] < theSir[j])
            {
                aux = theSir[i];
                theSir[i] = theSir[j];
                theSir[j] = aux;
            }
        }
        pthread_mutex_unlock(&theMutex);
        if (shouldStop == 1) break;
    }
    return NULL;
}

int main(int argc, char** argv)
{
    //	srand(time(NULL));
    srand(4);
    if (argc != 2)
    {
        printf("Trebuie argument\n");
        return 1;
    }
    int n = atoi(argv[1]);
    pthread_mutex_init(&theMutex, NULL);
    theN = n;
    theSir = malloc(sizeof(int) * n);
    for (int i = 0; i < n;i++)
    {
        theSir[i] = getRandom(0, 10000);
    }
    printf("%d\n", isSorted());
    pthread_t theThreads[n + 1];
    for (int i = 0;i < n;i++)
    {
        pthread_create(&theThreads[i], NULL, &theFunctie, (void*)i);
    }

    pthread_t thePrinterThread;
    pthread_create(&thePrinterThread, NULL, &thePrinter, NULL);
    pthread_join(thePrinterThread, NULL);
}