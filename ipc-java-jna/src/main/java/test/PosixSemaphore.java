package test;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface PosixSemaphore extends Library {
    PosixSemaphore INSTANCE = Native.load("c", PosixSemaphore.class);

    Pointer sem_open(String name, int oflag, int mode, int value);
    int sem_close(Pointer sem);
    int sem_unlink(String name);
    int sem_wait(Pointer sem);
    int sem_post(Pointer sem);
}
