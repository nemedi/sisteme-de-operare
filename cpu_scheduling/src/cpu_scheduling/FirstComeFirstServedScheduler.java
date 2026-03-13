package cpu_scheduling;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static cpu_scheduling.Utilities.printGanttChart;
import static cpu_scheduling.Utilities.printMetrics;

public class FirstComeFirstServedScheduler {
	
    public static List<Job> schedule(List<Job> jobs) {
        jobs.sort(Comparator.comparingDouble(j -> j.arrivalTime));
        int currentTime = 0;
        for (Job job : jobs) {
            if (currentTime < job.arrivalTime) {
                currentTime = (int) job.arrivalTime;
            }
            job.startTime = currentTime;
            job.completionTime = currentTime + job.burstTime;
            job.turnaroundTime = job.completionTime - job.arrivalTime;
            job.waitingTime = job.turnaroundTime - job.burstTime;
            currentTime = job.completionTime;
        }
        return jobs;
    }

    public static void demonstrateConvoyEffect() {
        System.out.println("\n============================================================");
        System.out.println("DEMO CONVOY EFFECT");
        System.out.println("============================================================");
        System.out.println("\n--- Scenario 1: LONGEST job first ---");
        List<Job> jobs1 = new ArrayList<>();
        jobs1.add(new Job("P1", 0, 24));
        jobs1.add(new Job("P2", 0, 3));
        jobs1.add(new Job("P3", 0, 3));
        List<Job> results1 = schedule(jobs1);
        printMetrics(results1);
        System.out.println("\n--- Scenario 2: SHORTEST job first ---");
        List<Job> jobs2 = new ArrayList<>();
        jobs2.add(new Job("P2", 0, 3));
        jobs2.add(new Job("P3", 1, 3));
        jobs2.add(new Job("P1", 2, 24));
        List<Job> results2 = schedule(jobs2);
        printMetrics(results2);
        System.out.println("\nComments: Same jobs, different order → totally different waiting time!");
    }

    public static void main(String[] args) {
        List<Job> jobs = new ArrayList<>();
        jobs.add(new Job("J1", 0, 24));
        jobs.add(new Job("J2", 0, 3));
        jobs.add(new Job("J3", 0, 3));
        List<Job> results = schedule(jobs);
        printGanttChart(results);
        printMetrics(results);
        demonstrateConvoyEffect();
    }
}