package cpu_scheduling;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static cpu_scheduling.Utilities.printGanttChart;
import static cpu_scheduling.Utilities.printMetrics;

public class RoundRobinScheduler {

	public static List<Job> schedule(List<Job> jobs, int quantum) {
        Queue<Job> queue = new LinkedList<>(jobs);
        int currentTime = 0;
        while (!queue.isEmpty()) {
            Job job = queue.poll();
            int executeTime = Math.min(quantum, job.remainingTime);
            currentTime += executeTime;
            job.remainingTime -= executeTime;
            if (job.remainingTime > 0) {
                queue.add(job);
            } else {
                job.completionTime = currentTime;
                job.turnaroundTime = job.completionTime - job.arrivalTime;
                job.waitingTime = job.turnaroundTime - job.burstTime;
            }
        }
        return jobs;
    }

    public static void main(String[] args) {
        int quantum = 4;
        List<Job> jobs = new ArrayList<>();
        jobs.add(new Job("P1", 0, 10));
        jobs.add(new Job("P2", 0, 5));
        jobs.add(new Job("P3", 0, 6));
        jobs.add(new Job("P4", 0, 8));
        List<Job> results = schedule(jobs, quantum);
        printGanttChart(results);
        printMetrics(results);
    }
}