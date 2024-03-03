#include <semaphore.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[]) {
    if (argc == 2)
    {
        char* name = argv[1];
        if (sem_unlink(name) == -1) {
            perror("sem_unlink");
            exit(EXIT_FAILURE);
        }
        printf("Semaphore was successfully removed.\n");
        return 0;
    }
    else 
    {
        return -1;
    }    
}
