#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <mqueue.h>

#define QUEUE_NAME "/my_message_queue"
#define MAX_MSG_SIZE 256

int main() {
    struct mq_attr attr;
    attr.mq_flags = 0;
    attr.mq_maxmsg = 10;
    attr.mq_msgsize = MAX_MSG_SIZE;
    attr.mq_curmsgs = 0;
    mqd_t mq = mq_open(QUEUE_NAME, O_CREAT | O_WRONLY, 0666, &attr);
    if (mq == (mqd_t)-1) {
        perror("Error opening message queue");
        exit(EXIT_FAILURE);
    }
    printf("Enter text to write to the message queue (type 'exit' to quit):\n");
    while (1) {
        char input[MAX_MSG_SIZE];
        fgets(input, sizeof(input), stdin);
        input[strcspn(input, "\n")] = '\0';
        if (strcmp(input, "exit") == 0) {
            break;
        }
        if (mq_send(mq, input, strlen(input) + 1, 0) == -1) {
            perror("Error sending message to queue");
            break;
        }
    }
    mq_close(mq);
    mq_unlink(QUEUE_NAME);
    return 0;
}