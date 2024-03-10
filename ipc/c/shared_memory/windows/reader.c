#include <windows.h>
#include <stdio.h>

int main() {
    HANDLE hMapFile;
    LPCTSTR pBuf;

    // Open the existing file mapping object
    hMapFile = OpenFileMapping(FILE_MAP_ALL_ACCESS, FALSE, L"MySharedMemory");

    if (hMapFile == NULL) {
        fprintf(stderr, "Could not open file mapping object (%d)\n", GetLastError());
        return 1;
    }

    // Map the shared memory into the address space of the calling process
    pBuf = (LPTSTR)MapViewOfFile(hMapFile, FILE_MAP_ALL_ACCESS, 0, 0, 256);

    if (pBuf == NULL) {
        fprintf(stderr, "Could not map view of file (%d)\n", GetLastError());
        CloseHandle(hMapFile);
        return 1;
    }

    // Read data from shared memory
    printf("Data received from writer: %s\n", pBuf);

    // Unmap the shared memory
    UnmapViewOfFile(pBuf);
    CloseHandle(hMapFile);

    return 0;
}
