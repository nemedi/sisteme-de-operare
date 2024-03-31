import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class DeadlockDetection {
    private int numberOfProcesses;
    private int numberOfResources;
    private int[][] allocation;
    private int[][] request;
    private boolean[] marked;
    private boolean[] visited;
    private List<Integer> deadlockProcesses;

    public DeadlockDetection(int numnerofProcesses, int numberOfResources, int[][] allocation, int[][] request) {
        this.numberOfProcesses = numnerofProcesses;
        this.numberOfResources = numberOfResources;
        this.allocation = allocation;
        this.request = request;
        marked = new boolean[numnerofProcesses];
        visited = new boolean[numnerofProcesses];
        deadlockProcesses = new ArrayList<>();
    }

    public boolean isDeadlock() {
        for (int i = 0; i < numberOfProcesses; i++) {
            if (!marked[i]) {
                Arrays.fill(visited, false);
                if (isCycle(i)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCycle(int process) {
        if (visited[process]) {
            return true;
        }
        if (marked[process]) {
            return false;
        }
        visited[process] = true;
        for (int i = 0; i < numberOfResources; i++) {
            if (request[process][i] > 0 && allocation[process][i] == 0) {
                if (isCycle(i)) {
                    return true;
                }
            }
        }
        marked[process] = true;
        return false;
    }

    public List<Integer> getDeadlockProcesses() {
        if (isDeadlock()) {
            for (int i = 0; i < numberOfProcesses; i++) {
                if (marked[i]) {
                    deadlockProcesses.add(i);
                }
            }
        }
        return deadlockProcesses;
    }

}
