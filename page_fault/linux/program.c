#include <stdio.h>
#include <signal.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/mman.h>

void sigsegv_handler(int signum) {
    printf("Page fault occurred!\n");
    exit(EXIT_FAILURE);
}

int main() {
    struct sigaction sa;
    sa.sa_handler = sigsegv_handler;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = 0;
    if (sigaction(SIGSEGV, &sa, NULL) == -1) {
        perror("sigaction");
        return EXIT_FAILURE;
    }

    char *ptr = NULL;
    *ptr = 'a';

    return EXIT_SUCCESS;
}
