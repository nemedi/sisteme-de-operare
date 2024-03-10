package jna;

import com.sun.jna.Pointer;

public class SharedMemory {
	
    private int id;
    private Pointer pointer;

    public SharedMemory(int key, int size, int shmflg) {
        id = MyPosixLibrary.INSTANCE.shmget(key, size, shmflg);
        if (id == -1) {
            throw new RuntimeException("Failed to create shared memory segment");
        }
        pointer = MyPosixLibrary.INSTANCE.shmat(id, null, 0);
        if (pointer == Pointer.NULL) {
            throw new RuntimeException("Failed to attach shared memory segment");
        }
    }

    public Pointer getPointer() {
        return pointer;
    }

    public void detach() {
        if (pointer != Pointer.NULL) {
        	MyPosixLibrary.INSTANCE.shmdt(pointer);
        }
    }

    public void removeSegment() {
        if (id != -1) {
        	MyPosixLibrary.INSTANCE.shmctl(id, MyPosixLibrary.IPC_RMID, null);
        }
    }
}
