#include <windows.h>
#include <stdio.h>

HANDLE semaphore;

DWORD WINAPI threadFunction1(LPVOID lpParam) {
    WaitForSingleObject(semaphore, INFINITE);
    printf("Thread 1 is in the critical section\n");
    ReleaseSemaphore(semaphore, 1, NULL);
    return 0;
}
DWORD WINAPI threadFunction2(LPVOID lpParam) {
    WaitForSingleObject(semaphore, INFINITE);
    printf("Thread 2 is in the critical section\n");
    ReleaseSemaphore(semaphore, 1, NULL);
    return 0;
}

int main() {
    semaphore = CreateSemaphore(NULL, 0, 1, NULL);
    if (semaphore == NULL) {
        printf("Semaphore creation failed\n");
        return 1;
    }
    HANDLE thread1, thread2;
    thread1 = CreateThread(NULL, 0, threadFunction1, NULL, 0, NULL);
    thread2 = CreateThread(NULL, 0, threadFunction2, NULL, 0, NULL);
    WaitForSingleObject(thread1, INFINITE);
    WaitForSingleObject(thread2, INFINITE);
    CloseHandle(thread1);
    CloseHandle(thread2);
    CloseHandle(semaphore);
    return 0;
}
