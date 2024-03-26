#include <stdio.h>
#include <windows.h>

DWORD WINAPI ThreadFunction(LPVOID lpParam)
{
    char* threadName = (char*)lpParam;
    printf("Hello World from %s!\n", threadName);
    return 0;
}

int main()
{
    HANDLE hThread;
    DWORD dwThreadId;
    char* threadName = "MyThread";
    hThread = CreateThread(NULL, 0, ThreadFunction,
        threadName, 0, &dwThreadId);
    if (hThread == NULL)
    {
        fprintf(stderr, "Error creating thread\n");
        return 1;
    }
    WaitForSingleObject(hThread, INFINITE);
    CloseHandle(hThread);
    return 0;
}

