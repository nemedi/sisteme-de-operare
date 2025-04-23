#include <windows.h>
#include <stdio.h>

DWORD WINAPI ThreadFunction(LPVOID lpParam) {
    MessageBoxA(NULL, "Hello from the new thread!", "Win32 API Call", MB_OK | MB_ICONINFORMATION);
    return 0;
}

void PrintThreadKernelTime(HANDLE hThread) {
    FILETIME creationTime, exitTime, kernelTime, userTime;
    if (GetThreadTimes(hThread, &creationTime, &exitTime, &kernelTime, &userTime)) {
        ULONGLONG kTime = ((ULONGLONG)kernelTime.dwHighDateTime << 32) | kernelTime.dwLowDateTime;
        ULONGLONG uTime = ((ULONGLONG)userTime.dwHighDateTime << 32) | userTime.dwLowDateTime;
        printf("Kernel Time: %llu ticks\n", kTime);
        printf("User Time:   %llu ticks\n", uTime);
    } else {
        printf("Failed to get thread times: %lu\n", GetLastError());
    }
}

int main() {
    HANDLE hThread; //= GetCurrentThread();;
    DWORD threadId;
    hThread = CreateThread(
        NULL,           // default security attributes
        0,              // default stack size
        ThreadFunction, // thread function
        NULL,           // parameter to thread function
        0,              // default creation flags
        &threadId       // receive thread identifier
    );
    if (hThread == NULL) {
        printf("CreateThread failed with error: %lu\n", GetLastError());
        return 1;
    }
    printf("Thread created with ID: %lu\n", threadId);
    WaitForSingleObject(hThread, INFINITE);        
    PrintThreadKernelTime(hThread);
    CloseHandle(hThread);
    return 0;
}
