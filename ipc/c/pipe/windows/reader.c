#include <windows.h>
#include <stdio.h>

int main() {
    HANDLE hPipe;
    char buffer[1024];
    DWORD bytesRead;
    hPipe = CreateNamedPipe("\\\\.\\pipe\\MyPipe", PIPE_ACCESS_DUPLEX,
                            PIPE_TYPE_BYTE | PIPE_READMODE_BYTE | PIPE_WAIT,
                            1, 1024, 1024, 0, NULL);
    if (hPipe == INVALID_HANDLE_VALUE) {
        fprintf(stderr, "Error creating named pipe (%d)\n", GetLastError());
        return 1;
    }
    printf("Waiting to receive data from writer...\n");
    ConnectNamedPipe(hPipe, NULL);
    if (ReadFile(hPipe, buffer, sizeof(buffer), &bytesRead, NULL)) {
        buffer[bytesRead] = '\0';
        printf("Received data from writer: %s\n", buffer);
    } else {
        fprintf(stderr, "Error reading from pipe (%d)\n", GetLastError());
    }
    DisconnectNamedPipe(hPipe);
    CloseHandle(hPipe);
    return 0;
}
