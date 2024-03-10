#include <windows.h>
#include <stdio.h>

int main() {
    HANDLE hEvent = OpenEvent(EVENT_MODIFY_STATE, FALSE, L"MyEvent");
    if (hEvent == NULL) {
        fprintf(stderr, "Error opening event (%d)\n", GetLastError());
        return 1;
    }
    printf("Sending signal...\n");
    SetEvent(hEvent);
    CloseHandle(hEvent);
    return 0;
}