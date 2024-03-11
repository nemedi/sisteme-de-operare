#include <stdio.h>
#include <windows.h>

int main() {
    HANDLE hFileMapping = OpenFileMapping(FILE_MAP_READ, FALSE, "MySharedMemory");
    if (hFileMapping == NULL) {
        perror("Error opening shared memory");
        return 1;
    }
    char* sharedMemory = (char*)MapViewOfFile(hFileMapping, FILE_MAP_READ, 0, 0, 0);
    if (sharedMemory == NULL) {
        perror("Error mapping view of file");
        CloseHandle(hFileMapping);
        return 1;
    }
    while (1) {
        printf("Received from shared memory: %s\n", sharedMemory);
        if (strcmp(sharedMemory, "quit") == 0) {
            break;
        }
        Sleep(1000);
    }
    UnmapViewOfFile(sharedMemory);
    CloseHandle(hFileMapping);
    return 0;
}
