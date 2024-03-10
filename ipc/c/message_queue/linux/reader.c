#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <mqueue.h>

#define QUEUE_NAME "/my_message_queue"
#define MAX_MSG_SIZE 256

int main() {
    mqd_t mq = mq_open(QUEUE_NAME, O_RDONLY, 0666, NULL);
    if (mq == (mqd_t)-1) {
        perror("Error opening message queue");
        exit(EXIT_FAILURE);
    }
    printf("Reading from the message queue (type 'exit' to quit):\n");
    while (1) {
        char buffer[MAX_MSG_SIZE];
        ssize_t bytes_read = mq_receive(mq, buffer, MAX_MSG_SIZE, NULL);
        if (bytes_read == -1) {
            perror("Error receiving message from queue");
            break;
        }
        printf("Received: %s\n", buffer);
        if (strcmp(buffer, "exit") == 0) {
            break;
        }
    }
    mq_close(mq);
    return 0;
}