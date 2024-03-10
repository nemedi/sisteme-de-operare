#include <windows.h>
#include <stdio.h>

int main() {
    HANDLE hMapFile;
    LPCTSTR pBuf;

    // Create a file mapping object
    hMapFile = CreateFileMapping(INVALID_HANDLE_VALUE, NULL, PAGE_READWRITE, 0, 256, L"MySharedMemory");

    if (hMapFile == NULL) {
        fprintf(stderr, "Could not create file mapping object (%d)\n", GetLastError());
        return 1;
    }

    // Map the shared memory into the address space of the calling process
    pBuf = (LPTSTR)MapViewOfFile(hMapFile, FILE_MAP_ALL_ACCESS, 0, 0, 256);

    if (pBuf == NULL) {
        fprintf(stderr, "Could not map view of file (%d)\n", GetLastError());
        CloseHandle(hMapFile);
        return 1;
    }

    // Write data to shared memory
    const char* dataToSend = "Hello, reader!";
    sprintf((char*)pBuf, "%s", dataToSend);
    printf("Data sent to reader: %s\n", dataToSend);

    // Unmap the shared memory
    UnmapViewOfFile(pBuf);
    CloseHandle(hMapFile);

    return 0;
}
