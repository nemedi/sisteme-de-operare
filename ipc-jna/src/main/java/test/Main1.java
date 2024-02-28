package test;

public class Main1 {
    public static void main(String[] args) {
        String semaphoreName = "my_posix_semaphore";
        int initialValue = 1;

        NamedPosixSemaphore semaphore = new NamedPosixSemaphore(semaphoreName, initialValue);

        try {
            // Perform some work

            // Signal that work is done
            semaphore.postSemaphore();
        } finally {
            semaphore.closeSemaphore();
        }
    }
}
