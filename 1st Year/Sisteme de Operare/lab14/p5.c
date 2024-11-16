#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

// Structure for thread data
typedef struct {
    int *array;
    int start;
    int end;
    long long sum;
} ThreadData;

// Thread function
void* sum_array(void *arg) {
    ThreadData *data = (ThreadData*)arg;
    data->sum = 0;
    for(int i = data->start; i < data->end; i++) {
        data->sum += data->array[i];
    }
    return NULL;
}

int main(int argc, char *argv[]) {
    if(argc != 3) {
        printf("Usage: %s <num_elements> <num_threads>\n", argv[0]);
        return 1;
    }

    int num_elements = atoi(argv[1]);
    int num_threads = atoi(argv[2]);

    // Allocate and initialize array
    int *array = malloc(num_elements * sizeof(int));
    for(int i = 0; i < num_elements; i++) {
        array[i] = rand() % 100;
    }

    // Allocate thread data and create threads
    ThreadData *thread_data = malloc(num_threads * sizeof(ThreadData));
    pthread_t *threads = malloc(num_threads * sizeof(pthread_t));
    int elements_per_thread = num_elements / num_threads;
    for(int i = 0; i < num_threads; i++) {
        thread_data[i].array = array;
        thread_data[i].start = i * elements_per_thread;
        thread_data[i].end = (i == num_threads - 1) ? num_elements : (i + 1) * elements_per_thread;
        pthread_create(&threads[i], NULL, sum_array, &thread_data[i]);
    }

    // Wait for threads to finish and sum results
    long long total_sum = 0;
    for(int i = 0; i < num_threads; i++) {
        pthread_join(threads[i], NULL);
        total_sum += thread_data[i].sum;
    }

    printf("Total sum: %lld\n", total_sum);

    // Cleanup
    free(array);
    free(thread_data);
    free(threads);

    return 0;
}