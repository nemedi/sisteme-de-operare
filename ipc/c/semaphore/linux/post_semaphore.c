
#include <semaphore.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[]) {
    if (argc == 2)
    {
        char* name = argv[1];
        sem_t* semaphore = sem_open(name, O_CREAT, 0644, 0);
        if (semaphore == SEM_FAILED) {
            perror("sem_open");
            return 1;
        }
        if (sem_post(semaphore) == -1) {
            perror("sem_post");
            exit(EXIT_FAILURE);
        }
        if (sem_close(semaphore) == -1) {
            perror("sem_close");
            return 1;
        }
        printf("Semaphore was successfully released.\n");
        return 0;
    }
    else 
    {
        return -1;
    }    
}
