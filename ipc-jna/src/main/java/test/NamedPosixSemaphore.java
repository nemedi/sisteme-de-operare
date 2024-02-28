package test;

import com.sun.jna.Pointer;

public class NamedPosixSemaphore {
    private Pointer semaphore;

    public NamedPosixSemaphore(String name, int initialValue) {
        // The value 0666 specifies read/write permissions for owner, group, and others.
        int oflag = 0; // Flags for semaphore creation.
        int mode = 0666; // Permissions for the semaphore.
        semaphore = Pointer.createConstant(0);

        semaphore = PosixSemaphore.INSTANCE.sem_open(name, oflag, mode, initialValue);
        if (semaphore == Pointer.NULL) {
            throw new RuntimeException("Failed to create semaphore");
        }
    }

    public void waitSemaphore() {
        PosixSemaphore.INSTANCE.sem_wait(semaphore);
    }

    public void postSemaphore() {
        PosixSemaphore.INSTANCE.sem_post(semaphore);
    }

    public void closeSemaphore() {
        PosixSemaphore.INSTANCE.sem_close(semaphore);
    }

    public void unlinkSemaphore(String name) {
        PosixSemaphore.INSTANCE.sem_unlink(name);
    }
}
