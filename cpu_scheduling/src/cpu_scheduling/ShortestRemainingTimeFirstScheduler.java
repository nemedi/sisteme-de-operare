package cpu_scheduling;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static cpu_scheduling.Utilities.printGanttChart;
import static cpu_scheduling.Utilities.printMetrics;

public class ShortestRemainingTimeFirstScheduler {

    public static List<Job> schedule(List<Job> jobs) {
        int n = jobs.size();
        int completed = 0;
        int currentTime = 0;
        while (completed < n) {
            Job shortest = null;
            for (Job job : jobs) {
                if (job.arrivalTime <= currentTime && job.remainingTime > 0) {
                    if (shortest == null ||
                        job.remainingTime < shortest.remainingTime) {
                        shortest = job;
                    }
                }
            }
            if (shortest == null) {
                currentTime++;
                continue;
            }
            if (shortest.startTime == -1) {
                shortest.startTime = currentTime;
            }
            shortest.remainingTime--;
            currentTime++;
            if (shortest.remainingTime == 0) {
                shortest.completionTime = currentTime;
                shortest.turnaroundTime =
                        shortest.completionTime - shortest.arrivalTime;
                shortest.waitingTime =
                        shortest.turnaroundTime - shortest.burstTime;
                completed++;
            }
        }
        jobs.sort(Comparator.comparingInt(j -> j.completionTime));
        return jobs;
    }
    
    public static void main(String[] args) {
        List<Job> jobs = new ArrayList<>();
        jobs.add(new Job("J1", 0, 8));
        jobs.add(new Job("J2", 1, 4));
        jobs.add(new Job("J3", 2, 9));
        jobs.add(new Job("J4", 3, 5));
        List<Job> results = schedule(jobs);
        printGanttChart(results);
        printMetrics(results);
    }	
}
