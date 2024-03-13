#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <semaphore.h>

#define SHARED_MEM_NAME "/my_shared_memory"
#define SHARED_MEM_SIZE 1024

int main() {
    int shm_fd = shm_open(SHARED_MEM_NAME, O_CREAT | O_RDWR, 0666);
    if (shm_fd == -1) {
        perror("Error creating shared memory");
        exit(EXIT_FAILURE);
    }
    size_t size = SHARED_MEM_SIZE;
    if (ftruncate(shm_fd, size) == -1) {
        perror("Error setting the size of shared memory");
        exit(EXIT_FAILURE);
    }
    char *shared_memory = mmap(NULL, size, PROT_READ | PROT_WRITE, MAP_SHARED, shm_fd, 0);
    if (shared_memory == MAP_FAILED) {
        perror("Error mapping shared memory");
        exit(EXIT_FAILURE);
    }
    printf("Enter text to write to shared memory (type 'quit' to exit):\n");
    while (1) {
        char input[256];
        fgets(input, sizeof(input), stdin);
        input[strcspn(input, "\n")] = '\0';
        if (strcmp(input, "quit") == 0) {
            break;
        }
        snprintf(shared_memory, size, "%s", input);
    }
    munmap(shared_memory, size);
    close(shm_fd);
    shm_unlink(shared_memory);
    return 0;
}
