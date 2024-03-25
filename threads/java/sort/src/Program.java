import java.util.Arrays;

public class Program {
    public static void main(String[] args) {
        int[] array = new int[100];
        int numThreads = 4;
        int chunkSize = array.length / numThreads;

        // Initialize array with random values
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) (Math.random() * 1000);
        }

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;
            int end = (i == numThreads - 1) ? array.length : (i + 1) * chunkSize;
            threads[i] = new Thread(new SortTask(array, start, end));
            threads[i].start();
        }

        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mergeChunks(array, chunkSize);

        // Print sorted array
        System.out.println("Sorted Array:");
        for (int item : array) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    private static void mergeChunks(int[] array, int chunkSize) {
        int[] temp = new int[array.length];
        int chunkIndex = 0;
        for (int i = 0; i < array.length; i += chunkSize) {
            System.arraycopy(array, i, temp, chunkIndex, chunkSize);
            chunkIndex += chunkSize;
        }

        Arrays.sort(temp);
        System.arraycopy(temp, 0, array, 0, array.length);
    }
}
