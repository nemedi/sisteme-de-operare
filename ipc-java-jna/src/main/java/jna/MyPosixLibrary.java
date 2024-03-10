package jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;

public interface MyPosixLibrary extends Library {
	
    int IPC_CREAT = 0001000;
    int IPC_EXCL = 0002000;
    int IPC_RMID = 0;
	
    MyPosixLibrary INSTANCE = Native.load(Platform.isWindows()
    		? "msvcrt" : "c", MyPosixLibrary.class);

    int mkfifo(String pathname, int mode);
    
    int sem_init(Pointer semaphore, int shared, int capacity);
    int sem_destroy(Pointer semaphore);
    int sem_wait(Pointer semaphore);
    int sem_post(Pointer semaphore);
    
    int shmget(int key, int size, int shmflg);
    Pointer shmat(int shmid, Pointer shmaddr, int shmflg);
    int shmdt(Pointer shmaddr);
    int shmctl(int shmid, int cmd, Pointer buf);
    
    int socket(int domain, int type, int protocol);
    int bind(int sockfd, SockAddr socketAddress, int addressLength);
    int listen(int sockfd, int backlog);
    int accept(int sockfd, SockAddr socketAddress, int[] addressLength);
    int connect(int sockfd, SockAddr socketAddress, int addressLength);
    int close(int fd);
}