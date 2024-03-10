#include <windows.h>
#include <stdio.h>

int main() {
    HANDLE hPipe;
    hPipe = CreateFile("\\\\.\\pipe\\MyPipe", GENERIC_READ | GENERIC_WRITE, 0,
                       NULL, OPEN_EXISTING, 0, NULL);
    if (hPipe == INVALID_HANDLE_VALUE) {
        fprintf(stderr, "Error connecting to named pipe (%d)\n", GetLastError());
        return 1;
    }
    const char* dataToSend = "Hello, Writer!";
    if (WriteFile(hPipe, dataToSend, strlen(dataToSend), NULL, NULL)) {
        printf("Data sent to reader.\n");
    } else {
        fprintf(stderr, "Error writing to pipe (%d)\n", GetLastError());
    }
    CloseHandle(hPipe);
    return 0;
}
