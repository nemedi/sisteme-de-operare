#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>

#define PIPE_NAME "my_pipe"

int main() {
    int fd;
    char buffer[256];
    mkfifo(PIPE_NAME, 0666);
    fd = open(PIPE_NAME, O_WRONLY);
    if (fd == -1) {
        perror("Error opening named pipe");
        exit(EXIT_FAILURE);
    }
    printf("Enter a message (type 'quit' to exit): ");
    while (1) {
        fgets(buffer, sizeof(buffer), stdin);
        size_t len = strlen(buffer);
        if (len > 0 && buffer[len - 1] == '\n') {
            buffer[len - 1] = '\0';
        }
        write(fd, buffer, strlen(buffer));
        if (strcmp(buffer, "quit") == 0) {
            break;
        }
    }
    close(fd);
    return EXIT_SUCCESS;
}
