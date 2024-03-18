#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define NUM_THREADS 4
#define TASK_QUEUE_SIZE 10

typedef struct
{
    void (*function)(void *);
    void *argument;
} Task;

typedef struct
{
    Task *tasks;
    int capacity;
    int size;
    int front;
    int rear;
    pthread_mutex_t lock;
    pthread_cond_t not_empty;
    pthread_cond_t not_full;
} ThreadPool;

void task_execute(void *argument)
{
    int *task_id = (int *)argument;
    printf("Executing task %d\n", *task_id);
    free(argument);
}

ThreadPool* thread_pool_create(int capacity)
{
    ThreadPool *pool = (ThreadPool*)malloc(sizeof(ThreadPool));
    pool->capacity = capacity;
    pool->size = 0;
    pool->front = 0;
    pool->rear = -1;
    pool->tasks = (Task*)malloc(capacity * sizeof(Task));
    pthread_mutex_init(&pool->lock, NULL);
    pthread_cond_init(&pool->not_empty, NULL);
    pthread_cond_init(&pool->not_full, NULL);
    return pool;
}

void thread_pool_destroy(ThreadPool *pool)
{
    pthread_mutex_destroy(&pool->lock);
    pthread_cond_destroy(&pool->not_empty);
    pthread_cond_destroy(&pool->not_full);
    free(pool->tasks);
    free(pool);
}

void thread_pool_add_task(ThreadPool *pool, void (*function)(void *), void *argument)
{
    pthread_mutex_lock(&pool->lock);
    while (pool->size >= pool->capacity)
    {
        pthread_cond_wait(&pool->not_full, &pool->lock);
    }
    pool->rear = (pool->rear + 1) % pool->capacity;
    pool->tasks[pool->rear].function = function;
    pool->tasks[pool->rear].argument = argument;
    pool->size++;
    pthread_cond_signal(&pool->not_empty);
    pthread_mutex_unlock(&pool->lock);
}

void* thread_function(void *arg)
{
    ThreadPool *pool = (ThreadPool*)arg;
    while (1)
    {
        pthread_mutex_lock(&pool->lock);
        while (pool->size == 0)
        {
            pthread_cond_wait(&pool->not_empty, &pool->lock);
        }
        Task task = pool->tasks[pool->front];
        pool->front = (pool->front + 1) % pool->capacity;
        pool->size--;
        pthread_cond_signal(&pool->not_full);
        pthread_mutex_unlock(&pool->lock);
        task.function(task.argument);
    }
    return NULL;
}

int main()
{
    ThreadPool *pool = thread_pool_create(TASK_QUEUE_SIZE);
    pthread_t threads[NUM_THREADS];
    for (int i = 0; i < NUM_THREADS; i++)
    {
        pthread_create(&threads[i], NULL, thread_function, pool);
    }
    for (int i = 0; i < 20; i++)
    {
        int *task_id = (int *)malloc(sizeof(int));
        *task_id = i;
        thread_pool_add_task(pool, task_execute, task_id);
    }
    for (int i = 0; i < NUM_THREADS; i++) {
        pthread_join(threads[i], NULL);
    }
    thread_pool_destroy(pool);
    return 0;
}
