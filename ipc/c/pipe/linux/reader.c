#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>

#define PIPE_NAME "my_pipe"

int main() {
    int fd;
    char buffer[256];
    fd = open(PIPE_NAME, O_RDONLY);
    if (fd == -1) {
        perror("Error opening named pipe");
        exit(EXIT_FAILURE);
    }
    printf("Reader is waiting for messages...\n");
    while (1) {
        // Read from the named pipe
        ssize_t bytesRead = read(fd, buffer, sizeof(buffer) - 1);
        if (bytesRead == -1) {
            perror("Error reading from named pipe");
            break;
        }
        buffer[bytesRead] = '\0';
        printf("Received: %s\n", buffer);
        if (strcmp(buffer, "quit") == 0) {
            break;
        }
    }
    close(fd);
    return EXIT_SUCCESS;
}
