#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define NUM_THREADS 5

pthread_mutex_t mutex;

void *thread_function(void *arg)
{
    pthread_mutex_lock(&mutex);
    printf("Thread %ld is accessing the shared resource\n", (long)arg);
    pthread_mutex_unlock(&mutex);
    pthread_exit(NULL);
}

int main()
{
    pthread_t threads[NUM_THREADS];
    int i;
    if (pthread_mutex_init(&mutex, NULL) != 0)
    {
        fprintf(stderr, "Mutex initialization failed\n");
        return 1;
    }
    for (i = 0; i < NUM_THREADS; ++i)
    {
        if (pthread_create(&threads[i], NULL, thread_function, (void *)(long)i) != 0)
        {
            fprintf(stderr, "Thread creation failed\n");
            return 1;
        }
    }
    for (i = 0; i < NUM_THREADS; ++i) 
    {
        if (pthread_join(threads[i], NULL) != 0)
        {
            fprintf(stderr, "Thread join failed\n");
            return 1;
        }
    }
    pthread_mutex_destroy(&mutex);
    return 0;
}
