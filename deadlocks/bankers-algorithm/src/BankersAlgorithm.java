public class BankersAlgorithm {
    private int[][] maximum; // maximum needs of processes
    private int[][] allocation; // resources allocated to processes
    private int[] available; // available resources
    private int[][] need; // resources needed by processes
    private int numberOfProcesses; // number of processes
    private int numberOfResources; // number of resources

    public BankersAlgorithm(int[][] maximum, int[][] allocation, int[] available) {
        this.maximum = maximum;
        this.allocation = allocation;
        this.available = available;
        this.numberOfProcesses = maximum.length;
        this.numberOfResources = available.length;
        this.need = new int[numberOfProcesses][numberOfResources];
        for (int i = 0; i < numberOfProcesses; i++) {
            for (int j = 0; j < numberOfResources; j++) {
                need[i][j] = maximum[i][j] - allocation[i][j];
            }
        }
    }

    public boolean isSafeState() {
        boolean[] finish = new boolean[numberOfProcesses];
        int[] work = new int[numberOfResources];
        System.arraycopy(available, 0, work, 0, numberOfResources);
        int count = 0;
        while (count < numberOfProcesses) {
            boolean found = false;
            for (int i = 0; i < numberOfProcesses; i++) {
                if (!finish[i]) {
                    int j;
                    for (j = 0; j < numberOfResources; j++) {
                        if (need[i][j] > work[j])
                            break;
                    }
                    if (j == numberOfResources) {
                        for (int k = 0; k < numberOfResources; k++) {
                            work[k] += allocation[i][k];
                        }
                        finish[i] = true;
                        found = true;
                        count++;
                    }
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }
}
