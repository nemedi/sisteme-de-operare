#include <stdio.h>
#include <windows.h>

#define MAX_COMMAND_LENGTH 100

int main() {
    HANDLE semaphore;
    semaphore = CreateSemaphore(NULL, 1, 1, "MyNamedSemaphore");
    if (semaphore == NULL) {
        fprintf(stderr, "Semaphore creation failed (%d)\n", GetLastError());
        return 1;
    }
    WaitForSingleObject(semaphore, INFINITE);
    char command[MAX_COMMAND_LENGTH];
    while (1) {
        printf("Enter a command (type 'quit' to exit): ");
        fgets(command, MAX_COMMAND_LENGTH, stdin);
        size_t len = strlen(command);
        if (len > 0 && command[len - 1] == '\n') {
            command[len - 1] = '\0';
        }
        printf("Command: %s\n", command);
        if (strcmp(command, "quit") == 0) {
            break;
        }
    }
    ReleaseSemaphore(semaphore, 1, NULL);
    CloseHandle(semaphore);
    return 0;
}
