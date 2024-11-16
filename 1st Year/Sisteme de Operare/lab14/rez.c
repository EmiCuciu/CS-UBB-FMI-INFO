#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>

typedef struct{
        int **matrix;
        int n;
        int idx;
        int k;
        pthread_mutex_t *m;
        int *alive_threads;
        sem_t *barrier;
        int *printed;
}ThreadData;

void *thread_func(void*arg){
        ThreadData *data = (ThreadData *)arg;
        int row = data->idx / data->n;
        int col = data->idx % data->n;
        int sum=0, count = 0;
        for(int i = row - data->k;i<=row+data->k;i++){
                for(int j=col-data->k;j<=col+data->k;j++){
                        if(i>=0 && i<data->n &&j>=0&&j<data->n){
                                sum += data->matrix[i][j];
                                count++;
                        }
                }
        }
        if(count == 0){
                pthread_exit(NULL);
        }
        int avg = sum/count;
        sem_wait(data->barrier);
        for(int i=row-data->k/2;i<row+data->k/2;i++){
                for(int j=col-data->k/2;j<=col+data->k/2;j++){
                        if(i>=0 && i< data->n && j>=0 && j<data->n && !(i==row && j == col)){
                        pthread_mutex_lock(data->m);
                        data->matrix[i][j]-=avg;
                        if(data->matrix[i][j]<0 && !data->printed[data->idx]){
                                printf("Thread %d: valoarea a scazut sub 0\n",data->idx);
                                data->printed[data->idx] = 1;
                                (*data->alive_threads)--;
                                if(*data->alive_threads == 1){
                                        printf("Ola in spaniola\n",data->idx);
                                }
                        }
                        pthread_mutex_unlock(data->m);
                }
        }
        }
        sem_post(data->barrier);
        return NULL;
}


int main(){
        int n,k;
        scanf("%d",&n);
        int **matrix = malloc(n*sizeof(int *));
        for(int i=0;i<n;i++){
                matrix[i] = malloc(n*sizeof(int));
                for(int j=0;j<n;j++){
                        scanf("%d", &matrix[i][j]);
                }
        }
        scanf("%d",&k);
        pthread_t *threads = malloc(n*n*sizeof(pthread_t));
        pthread_mutex_t m = PTHREAD_MUTEX_INITIALIZER;
        sem_t barrier;
        sem_init(&barrier,0,1);
        int alive_threads = n*n;
        int *printed = calloc(n*n,sizeof(int));
        for(int i=0;i<n*n;i++){
                ThreadData *data = malloc(sizeof(ThreadData));
                data->matrix = matrix;
                data->n = n;
                data->idx = i;
                data->k = k;
                data->m = &m;
                data->alive_threads = &alive_threads;
                data->barrier = &barrier;
                data->printed = printed;
                pthread_create(&threads[i],NULL,thread_func,data);
        }
        for(int i=0;i<n*n;i++){
                pthread_join(threads[i],NULL);
        }
        free(printed);
        free(threads);
        for(int i=0;i<n;i++){
                free(matrix[i]);
        }
        free(matrix);
        sem_destroy(&barrier);
        return 0;
}
