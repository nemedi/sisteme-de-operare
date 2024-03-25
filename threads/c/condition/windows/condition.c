#include <stdio.h>
#include <windows.h>

#define BUFFER_SIZE 10

int buffer[BUFFER_SIZE];
int count = 0;
HANDLE mutex;
HANDLE cond_prod;
HANDLE cond_cons;

DWORD WINAPI producer(LPVOID lpParam) {
    int item = 0;
    while (1) {
        WaitForSingleObject(mutex, INFINITE);
        while (count == BUFFER_SIZE) {
            WaitForSingleObject(cond_prod, INFINITE);
        }
        buffer[count++] = item++;
        printf("Produced: %d\n", item - 1);
        SetEvent(cond_cons);
        ReleaseMutex(mutex);
    }
    return 0;
}

DWORD WINAPI consumer(LPVOID lpParam) {
    while (1) {
        WaitForSingleObject(mutex, INFINITE);
        while (count == 0) {
            WaitForSingleObject(cond_cons, INFINITE);
        }
        int item = buffer[--count];
        printf("Consumed: %d\n", item);
        SetEvent(cond_prod);
        ReleaseMutex(mutex);
    }
    return 0;
}

int main() {
    mutex = CreateMutex(NULL, FALSE, NULL);
    cond_prod = CreateEvent(NULL, FALSE, FALSE, NULL);
    cond_cons = CreateEvent(NULL, FALSE, FALSE, NULL);
    HANDLE prod_thread = CreateThread(NULL, 0, producer, NULL, 0, NULL);
    HANDLE cons_thread = CreateThread(NULL, 0, consumer, NULL, 0, NULL);
    WaitForSingleObject(prod_thread, INFINITE);
    WaitForSingleObject(cons_thread, INFINITE);
    CloseHandle(mutex);
    CloseHandle(cond_prod);
    CloseHandle(cond_cons);
    return 0;
}
