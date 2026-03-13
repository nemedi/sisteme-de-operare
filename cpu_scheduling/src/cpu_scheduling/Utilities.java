package cpu_scheduling;

import java.util.List;

public class Utilities {
	
    public static void printGanttChart(List<Job> jobs) {
        System.out.println("\nGantt Chart:");
        System.out.println("┌" + "─".repeat(50) + "┐");
        StringBuilder timeline = new StringBuilder();
        StringBuilder labels = new StringBuilder();
        int current = 0;
        for (Job job : jobs) {
            int width = Math.max(3, job.burstTime / 2);
            timeline.append("│");
            timeline.append(String.format("%" + width + "s", job.pid));
            labels.append(String.format("%-" + (width + 1) + "d", current));
            current = job.completionTime;
        }
        timeline.append("│");
        labels.append(current);
        System.out.println(timeline);
        System.out.println("└" + "─".repeat(50) + "┘");
        System.out.println(labels);
    }

    public static void printMetrics(List<Job> jobs, boolean includePriority) {
        if (includePriority) {
        	System.out.println("\n=======================================================");
            System.out.printf("%-4s %-7s %-7s %-8s %-10s %-7s %-10s\n",
                    "PID","Arr","Burst","Prio","Complete","Wait","Turn");
        } else {
        	System.out.println("\n===============================================");

        	System.out.printf("%-5s %-7s %-10s %-7s %-10s\n",
                    "PID","Burst","Complete","Wait","Turn");
        }
        double totalWait = 0;
        double totalTurn = 0;
        for (Job job : jobs) {
        	if (includePriority) {
                System.out.printf("%-4s %-7d %-7d %-8d %-10d %-7d %-10d\n",
                        job.pid,
                        job.arrivalTime,
                        job.burstTime,
                        job.priority,
                        job.completionTime,
                        job.waitingTime,
                        job.turnaroundTime);
        	} else {
                System.out.printf("%-5s %-7d %-10d %-7d %-10d\n",
                        job.pid,
                        job.burstTime,
                        job.completionTime,
                        job.waitingTime,
                        job.turnaroundTime);
        	}
            totalWait += job.waitingTime;
            totalTurn += job.turnaroundTime;
        }
        if (includePriority) {
        	System.out.println("\n=======================================================");
        } else {
        	System.out.println("===============================================");
        }
        System.out.printf("Average Waiting Time: %.2f\n", totalWait / jobs.size());
        System.out.printf("Average Turnaround Time: %.2f\n", totalTurn / jobs.size());
    }
    
    public static void printMetrics(List<Job> jobs) {
    	printMetrics(jobs, false);
    }
}
