#include <stdio.h>
#include <windows.h>

int main() {
    HANDLE hFileMapping = CreateFileMapping(INVALID_HANDLE_VALUE, NULL, PAGE_READWRITE, 0, 256, "MySharedMemory");
    if (hFileMapping == NULL) {
        perror("Error creating shared memory");
        return 1;
    }
    char* sharedMemory = (char*)MapViewOfFile(hFileMapping, FILE_MAP_WRITE, 0, 0, 0);
    if (sharedMemory == NULL) {
        perror("Error mapping view of file");
        CloseHandle(hFileMapping);
        return 1;
    }
    while (1) {
        printf("Enter a message (type 'quit' to exit): ");
        fgets(sharedMemory, 256, stdin);
        size_t len = strlen(sharedMemory);
        if (len > 0 && sharedMemory[len - 1] == '\n') {
            sharedMemory[len - 1] = '\0';
        }
        if (strcmp(sharedMemory, "quit") == 0) {
            break;
        }
    }
    UnmapViewOfFile(sharedMemory);
    CloseHandle(hFileMapping);
    return 0;
}
