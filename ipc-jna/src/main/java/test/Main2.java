package test;

public class Main2 {
    public static void main(String[] args) {
        String semaphoreName = "my_posix_semaphore";
        int initialValue = 1;

        NamedPosixSemaphore semaphore = new NamedPosixSemaphore(semaphoreName, initialValue);

        try {
            // Wait for the semaphore to be signaled
            semaphore.waitSemaphore();

            // Continue with synchronized work
        } finally {
            semaphore.closeSemaphore();
        }
    }
}
