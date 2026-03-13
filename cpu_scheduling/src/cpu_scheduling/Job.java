package cpu_scheduling;

public class Job {
    String pid;
    int arrivalTime;
    int burstTime;
    int remainingTime;
    int startTime = -1;
    int completionTime;
    int waitingTime;
    int turnaroundTime;
    int priority;
    
    public Job(String pid, int arrivalTime, int burstTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }
    
    public Job(String pid, int arrivalTime, int burstTime, int priority) {
        this(pid, arrivalTime, burstTime);
        this.priority = priority;
    }
}
