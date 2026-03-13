package cpu_scheduling;

import static cpu_scheduling.Utilities.printGanttChart;
import static cpu_scheduling.Utilities.printMetrics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class MultiLevelQueueRoundRobinScheduler {

	public static List<Job> schedule(
	        List<Job> jobs,
	        int[][] queues,
	        int[] quantums) {
	    int nQueues = queues.length;
	    List<Queue<Job>> queueList = new ArrayList<>();
	    Set<Job> added = new HashSet<>();
	    for (int i = 0; i < nQueues; i++) {
	        queueList.add(new LinkedList<>());
	    }
	    int time = 0;
	    int completed = 0;
	    while (completed < jobs.size()) {
	        for (Job job : jobs) {
	            if (job.arrivalTime <= time && !added.contains(job)) {
	                for (int i = 0; i < queues.length; i++) {
	                    int minimum = queues[i][0];
	                    int maximum = queues[i][1];
	                    if (job.priority >= minimum && job.priority <= maximum) {
	                        queueList.get(i).add(job);
	                        added.add(job);
	                        break;
	                    }
	                }
	            }
	        }
	        Job current = null;
	        int queueIndex = -1;
	        for (int i = 0; i < nQueues; i++) {
	            if (!queueList.get(i).isEmpty()) {
	                current = queueList.get(i).poll();
	                queueIndex = i;
	                break;
	            }
	        }
	        if (current == null) {
	            time++;
	            continue;
	        }
	        int quantum = quantums[queueIndex];
	        int execute = Math.min(quantum, current.remainingTime);
	        time += execute;
	        current.remainingTime -= execute;
	        if (current.remainingTime == 0) {
	            current.completionTime = time;
	            current.turnaroundTime =
	                    current.completionTime - current.arrivalTime;
	            current.waitingTime =
	                    current.turnaroundTime - current.burstTime;
	            completed++;

	        } else {
	            queueList.get(queueIndex).add(current);
	        }
	    }
	    return jobs;
	}
    public static void main(String[] args) {
        List<Job> jobs = new ArrayList<>();
        jobs.add(new Job("J1", 0, 4, 5));
        jobs.add(new Job("J2", 0, 5, 8));
        jobs.add(new Job("J3", 2, 9, 10));
        jobs.add(new Job("J4", 2, 3, 7));
        jobs.add(new Job("J5", 3, 10, 4));
        jobs.add(new Job("J6", 17, 12, 9));
        int[][] queues = {
            {8,10},
            {5,7},
            {0,4}
        };
        int[] quantums = {2, 4, 6};
        List<Job> results = schedule(jobs, queues, quantums);
        printGanttChart(results);
        printMetrics(results);
    }
}