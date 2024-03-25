import java.util.Arrays;

class SortTask implements Runnable {
    private int[] array;
    private int start;
    private int end;

    public SortTask(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        Arrays.sort(array, start, end);
    }
}


