
#include <stdio.h>
#include <pthread.h>
#include <string.h>
#include <unistd.h>

void* thread_function(void* arg)
{
    char* thread_name = (char*)arg;
    printf("Hello World from %s!\n", thread_name);
    return NULL;
}

int main()
{
    pthread_t thread_id;
    char* thread_name = "MyThread";
    if (pthread_create(&thread_id, NULL, thread_function,
        (void*)thread_name) != 0)
    {
        perror("pthread_create");
        return 1;
    }
    if (pthread_join(thread_id, NULL) != 0)
    {
        perror("pthread_join");
        return 1;
    }
    return 0;
}