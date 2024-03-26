#include <windows.h>
#include <stdio.h>

HANDLE mutex;

DWORD WINAPI threadFunction1(LPVOID lpParam)
{
    WaitForSingleObject(mutex, INFINITE);
    printf("Thread 1 is in the critical section\n");
    ReleaseMutex(mutex);
    return 0;
}

DWORD WINAPI threadFunction2(LPVOID lpParam)
{
    WaitForSingleObject(mutex, INFINITE);
    printf("Thread 2 is in the critical section\n");
    ReleaseMutex(mutex);
    return 0;
}

int main()
{
    mutex = CreateMutex(NULL, FALSE, NULL);
    if (mutex == NULL)
    {
        printf("Mutex creation failed\n");
        return 1;
    }
    HANDLE thread1, thread2;
    thread1 = CreateThread(NULL, 0, threadFunction1, NULL, 0, NULL);
    thread2 = CreateThread(NULL, 0, threadFunction2, NULL, 0, NULL);
    WaitForSingleObject(thread1, INFINITE);
    WaitForSingleObject(thread2, INFINITE);
    CloseHandle(thread1);
    CloseHandle(thread2);
    CloseHandle(mutex);
    return 0;
}