package cpu_scheduling;

import java.util.*;

import static cpu_scheduling.Utilities.printGanttChart;
import static cpu_scheduling.Utilities.printMetrics;

public class PriorityRoundRobinScheduler {

    public static List<Job> schedule(List<Job> jobs, int quantum) {
        int time = 0;
        int completed = 0;
        int n = jobs.size();
        List<Job> ready = new ArrayList<>();
        while (completed < n) {
            for (Job job : jobs) {
                if (job.arrivalTime <= time && !ready.contains(job) && job.remainingTime > 0) {
                    ready.add(job);
                }
            }
            if (ready.isEmpty()) {
                time++;
                continue;
            }
            ready.sort((a,b) -> b.priority - a.priority);
            Job current = ready.get(0);
            int execute = Math.min(quantum, current.remainingTime);
            time += execute;
            current.remainingTime -= execute;
            if (current.remainingTime == 0) {
                current.completionTime = time;
                current.turnaroundTime = current.completionTime - current.arrivalTime;
                current.waitingTime = current.turnaroundTime - current.burstTime;
                ready.remove(current);
                completed++;
            } else {
                ready.remove(current);
                ready.add(current);
            }
        }
        return jobs;
    }

    public static void main(String[] args) {
        int quantum = 4;
        List<Job> jobs = new ArrayList<>();
        jobs.add(new Job("J1", 0, 4, 5));
        jobs.add(new Job("J2", 0, 5, 8));
        jobs.add(new Job("J3", 2, 9, 10));
        jobs.add(new Job("J4", 2, 3, 7));
        jobs.add(new Job("J5", 3, 10, 4));
        jobs.add(new Job("J6", 17, 12, 9));
        List<Job> results = schedule(jobs, quantum);
        printGanttChart(results);
        printMetrics(results, true);
    }
}