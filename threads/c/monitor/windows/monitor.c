#include <stdio.h>
#include <windows.h>

#define BUFFER_SIZE 10

typedef struct {
    int buffer[BUFFER_SIZE];
    int count;
    HANDLE mutex;
    HANDLE cond_prod;
    HANDLE cond_cons;
} Monitor;

void monitor_init(Monitor *mon) {
    mon->count = 0;
    mon->mutex = CreateMutex(NULL, FALSE, NULL);
    mon->cond_prod = CreateEvent(NULL, FALSE, FALSE, NULL);
    mon->cond_cons = CreateEvent(NULL, FALSE, FALSE, NULL);
}

void monitor_produce(Monitor *mon, int item) {
    WaitForSingleObject(mon->mutex, INFINITE);
    while (mon->count == BUFFER_SIZE) {
        WaitForSingleObject(mon->cond_prod, INFINITE);
    }
    mon->buffer[mon->count++] = item;
    printf("Produced: %d\n", item);
    SetEvent(mon->cond_cons);
    ReleaseMutex(mon->mutex);
}

int monitor_consume(Monitor *mon) {
    int item;
    WaitForSingleObject(mon->mutex, INFINITE);
    while (mon->count == 0) {
        WaitForSingleObject(mon->cond_cons, INFINITE);
    }
    item = mon->buffer[--mon->count];
    printf("Consumed: %d\n", item);
    SetEvent(mon->cond_prod);
    ReleaseMutex(mon->mutex);
    return item;
}

void monitor_destroy(Monitor *mon) {
    CloseHandle(mon->mutex);
    CloseHandle(mon->cond_prod);
    CloseHandle(mon->cond_cons);
}

void producer(void *arg) {
    Monitor *mon = (Monitor *)arg;
    int item = 0;
    while (1) {
        monitor_produce(mon, item++);
    }
}

void consumer(void *arg) {
    Monitor *mon = (Monitor *)arg;
    while (1) {
        monitor_consume(mon);
    }
}

int main() {
    Monitor mon;
    monitor_init(&mon);
    HANDLE prod_thread = CreateThread(NULL, 0, (LPTHREAD_START_ROUTINE)producer, &mon, 0, NULL);
    HANDLE cons_thread = CreateThread(NULL, 0, (LPTHREAD_START_ROUTINE)consumer, &mon, 0, NULL);
    WaitForSingleObject(prod_thread, INFINITE);
    WaitForSingleObject(cons_thread, INFINITE);
    monitor_destroy(&mon);
    return 0;
}
