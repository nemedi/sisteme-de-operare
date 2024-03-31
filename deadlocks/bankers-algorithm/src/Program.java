import java.util.Scanner;

public class Program {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of processes: ");
        int numberofProcesses = scanner.nextInt();
        System.out.print("Enter the number of resources: ");
        int numberOfResources = scanner.nextInt();
        int[][] maximum = new int[numberofProcesses][numberOfResources];
        int[][] allocation = new int[numberofProcesses][numberOfResources];
        int[] available = new int[numberOfResources];
        System.out.println("Enter the maximum needs of each process:");
        for (int i = 0; i < numberofProcesses; i++) {
            for (int j = 0; j < numberOfResources; j++) {
                maximum[i][j] = scanner.nextInt();
            }
        }
        System.out.println("Enter the allocated resources for each process:");
        for (int i = 0; i < numberofProcesses; i++) {
            for (int j = 0; j < numberOfResources; j++) {
                allocation[i][j] = scanner.nextInt();
            }
        }
        System.out.println("Enter the available resources:");
        for (int i = 0; i < numberOfResources; i++) {
            available[i] = scanner.nextInt();
        }
        BankersAlgorithm banker = new BankersAlgorithm(maximum, allocation, available);
        if (banker.isSafeState()) {
            System.out.println("Safe state");
        } else {
            System.out.println("Unsafe state");
        }
        scanner.close();
    }
}
