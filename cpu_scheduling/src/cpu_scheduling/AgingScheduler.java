package cpu_scheduling;

import java.util.ArrayList;
import java.util.List;

import static cpu_scheduling.Utilities.printGanttChart;
import static cpu_scheduling.Utilities.printMetrics;

public class AgingScheduler {

    public static List<Job> schedule(List<Job> jobs, int ageingStep) {
        List<Job> ready = new ArrayList<>();
        int time = 0;
        int completed = 0;
        while (completed < jobs.size()) {
            for (Job job : jobs) {
                if (job.arrivalTime == time) {
                    ready.add(job);
                }
            }
            if (ready.isEmpty()) {
                time++;
                continue;
            }
            for (Job job : ready) {
                if (job.remainingTime > 0 && job.startTime == -1) {
                    job.priority += ageingStep;
                }
            }
            ready.sort((a, b) -> b.priority - a.priority);
            Job current = ready.get(0);
            if (current.startTime == -1) {
                current.startTime = time;
            }
            current.remainingTime--;
            time++;
            if (current.remainingTime == 0) {
                current.completionTime = time;
                current.turnaroundTime =
                        current.completionTime - current.arrivalTime;
                current.waitingTime =
                        current.turnaroundTime - current.burstTime;
                ready.remove(current);
                completed++;
            }
        }
        return jobs;
    }

    public static void main(String[] args) {
        List<Job> processes = new ArrayList<>();
        processes.add(new Job("J1", 0, 8, 1));
        processes.add(new Job("J2", 1, 4, 5));
        processes.add(new Job("J3", 2, 9, 2));
        processes.add(new Job("J4", 3, 5, 3));
        int ageingStep = 1;
        List<Job> results = schedule(processes, ageingStep);
        printGanttChart(results);
        printMetrics(results);
    }
}