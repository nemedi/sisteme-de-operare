#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <semaphore.h>

#define SHARED_MEM_NAME "/my_shared_memory"
#define SHARED_MEM_SIZE 1024

int main() {
    int shm_fd = shm_open(SHARED_MEM_NAME, O_RDWR, 0666);
    if (shm_fd == -1) {
        perror("Error opening shared memory");
        exit(EXIT_FAILURE);
    }
    size_t size = SHARED_MEM_SIZE;
    char *shared_memory = mmap(NULL, size, PROT_READ | PROT_WRITE, MAP_SHARED, shm_fd, 0);
    if (shared_memory == MAP_FAILED) {
        perror("Error mapping shared memory");
        exit(EXIT_FAILURE);
    }
    printf("Reading from shared memory:\n");
    while (1) {
        printf("Received: %s\n", shared_memory);
        if (strcmp(shared_memory, "quit") == 0) {
            break;
        }
        sleep(1);
    }
    munmap(shared_memory, size);
    close(shm_fd);
    return 0;
}
