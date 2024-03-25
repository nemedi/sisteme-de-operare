#include <stdio.h>
#include <stdlib.h>
#include <semaphore.h>
#include <pthread.h>

#define NUM_THREADS 5

sem_t semaphore;

void *thread_function(void *arg) {
    sem_wait(&semaphore);
    printf("Thread %ld is accessing the shared resource\n", (long)arg);
    sem_post(&semaphore);
    pthread_exit(NULL);
}

int main() {
    pthread_t threads[NUM_THREADS];
    int i;
    if (sem_init(&semaphore, 0, 1) != 0) {
        fprintf(stderr, "Semaphore initialization failed\n");
        return 1;
    }
    for (i = 0; i < NUM_THREADS; ++i) {
        if (pthread_create(&threads[i], NULL, thread_function, (void *)(long)i) != 0) {
            fprintf(stderr, "Thread creation failed\n");
            return 1;
        }
    }
    for (i = 0; i < NUM_THREADS; ++i) {
        if (pthread_join(threads[i], NULL) != 0) {
            fprintf(stderr, "Thread join failed\n");
            return 1;
        }
    }
    sem_destroy(&semaphore);
    return 0;
}
