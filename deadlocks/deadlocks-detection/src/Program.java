import java.util.Scanner;

public class Program {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of processes: ");
        int numberOfProcesses = scanner.nextInt();
        System.out.print("Enter the number of resources: ");
        int numberOfResources = scanner.nextInt();
        int[][] allocation = new int[numberOfProcesses][numberOfResources];
        int[][] request = new int[numberOfProcesses][numberOfResources];
        System.out.println("Enter the allocation matrix:");
        for (int i = 0; i < numberOfProcesses; i++) {
            for (int j = 0; j < numberOfResources; j++) {
                allocation[i][j] = scanner.nextInt();
            }
        }
        System.out.println("Enter the request matrix:");
        for (int i = 0; i < numberOfProcesses; i++) {
            for (int j = 0; j < numberOfResources; j++) {
                request[i][j] = scanner.nextInt();
            }
        }
        DeadlockDetection detector = new DeadlockDetection(numberOfProcesses, numberOfResources, allocation, request);
        if (detector.isDeadlock()) {
            System.out.println("Deadlock detected.");
            System.out.println("Processes involved in deadlock: " + detector.getDeadlockProcesses());
        } else {
            System.out.println("No deadlock detected.");
        }
        scanner.close();
    }
}
