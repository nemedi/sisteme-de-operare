#include <semaphore.h>
#include <fcntl.h>

sem_t* Java_ipc_SemaphoreTool_openSemaphore(const char *name, int shared, unsigned int capacity) {
    return sem_open(name, O_CREAT, shared, capacity);
}

void Java_ipc_SemaphoreTool_aquireSemaphore(sem_t* semaphore) {
    sem_wait(semaphore);
}

void Java_ipc_SemaphoreTool_releaseSemaphore(sem_t* semaphore) {
    sem_post(semaphore);
}

void Java_ipc_SemaphoreTool_cloneSemaphore(sem_t* semaphore) {
	sem_close(semaphore);
}

int Java_ipc_SemaphoreTool_unlinkSemaphore(const char* name) {
	return sem_unlink(name);
}
