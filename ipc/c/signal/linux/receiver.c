#include <stdio.h>
#include <signal.h>
#include <unistd.h>

void signalHandler(int signal) {
    if (signal == SIGUSR1) {
        printf("Received SIGUSR1 signal.\n");
    } else {
        printf("Received an unexpected signal.\n");
    }
}

int main() {
    if (signal(SIGUSR1, signalHandler) == SIG_ERR) {
        perror("Error registering signal handler");
        return 1;
    }
    printf("Waiting for SIGUSR1 signal. Use another terminal to send the signal.\n");
    while (1) {
        sleep(1);
    }
    return 0;
}
