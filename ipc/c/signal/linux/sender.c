#include <stdio.h>
#include <signal.h>
#include <unistd.h>
#include <fcntl.h>

int main() {
    pid_t targetPID;
    printf("Enter the PID of the program awaiting the signal: ");
    scanf("%d", &targetPID);
    if (kill(targetPID, SIGUSR1) == -1) {
        perror("Error sending signal");
        return 1;
    }
    printf("Sent SIGUSR1 signal to the target program.\n");
    return 0;
}
