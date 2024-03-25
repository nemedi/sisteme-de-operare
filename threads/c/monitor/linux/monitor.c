#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define BUFFER_SIZE 10

typedef struct {
    int buffer[BUFFER_SIZE];
    int count;
    pthread_mutex_t mutex;
    pthread_cond_t cond_prod;
    pthread_cond_t cond_cons;
} Monitor;

void monitor_init(Monitor *mon) {
    mon->count = 0;
    pthread_mutex_init(&mon->mutex, NULL);
    pthread_cond_init(&mon->cond_prod, NULL);
    pthread_cond_init(&mon->cond_cons, NULL);
}

void monitor_produce(Monitor *mon, int item) {
    pthread_mutex_lock(&mon->mutex);
    while (mon->count == BUFFER_SIZE) {
        pthread_cond_wait(&mon->cond_prod, &mon->mutex);
    }
    mon->buffer[mon->count++] = item;
    printf("Produced: %d\n", item);
    pthread_cond_signal(&mon->cond_cons);
    pthread_mutex_unlock(&mon->mutex);
}

int monitor_consume(Monitor *mon) {
    int item;
    pthread_mutex_lock(&mon->mutex);
    while (mon->count == 0) {
        pthread_cond_wait(&mon->cond_cons, &mon->mutex);
    }
    item = mon->buffer[--mon->count];
    printf("Consumed: %d\n", item);
    pthread_cond_signal(&mon->cond_prod);
    pthread_mutex_unlock(&mon->mutex);
    return item;
}

void monitor_destroy(Monitor *mon) {
    pthread_mutex_destroy(&mon->mutex);
    pthread_cond_destroy(&mon->cond_prod);
    pthread_cond_destroy(&mon->cond_cons);
}

void *producer(void *arg) {
    Monitor *mon = (Monitor *)arg;
    int item = 0;
    while (1) {
        monitor_produce(mon, item++);
    }
    return NULL;
}

void *consumer(void *arg) {
    Monitor *mon = (Monitor *)arg;
    while (1) {
        monitor_consume(mon);
    }
    return NULL;
}

int main() {
    Monitor mon;
    monitor_init(&mon);
    pthread_t prod_thread, cons_thread;
    pthread_create(&prod_thread, NULL, producer, &mon);
    pthread_create(&cons_thread, NULL, consumer, &mon);
    pthread_join(prod_thread, NULL);
    pthread_join(cons_thread, NULL);
    monitor_destroy(&mon);
    return 0;
}
