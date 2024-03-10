#include <semaphore.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[]) {
    if (argc == 3)
    {
        char* name = argv[1];
        int capacity = atoi(argv[2]);
        sem_t* semaphore = sem_open(name, O_CREAT, 0644, capacity);
        if (semaphore == SEM_FAILED) {
            perror("sem_open");
            return 1;
        }
        if (sem_close(semaphore) == -1) {
            perror("sem_close");
            return 1;
        }
        printf("Semaphore was successfully created.\n");
        return 0;
    }
    else 
    {
        printf("Invalid arguments, use <name> <capacity>.\n");
        return -1;
    }
}
