#include <windows.h>
#include <stdio.h>

int main() {
    HANDLE hEvent = CreateEvent(NULL, FALSE, FALSE, "MyEvent");
    if (hEvent == NULL) {
        fprintf(stderr, "Error creating event (%d)\n", GetLastError());
        return 1;
    }
    printf("Waiting for signal...\n");
    WaitForSingleObject(hEvent, INFINITE);
    printf("Signal received!\n");
    CloseHandle(hEvent);
    return 0;
}
