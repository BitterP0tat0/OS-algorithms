package Homework;

public class Parameters {
    private String processId;
    private int arrivalTime;
    private int burstTime;
    private int completionTime;
    private int turnaroundTime;
    private int waitingTime;
    private int priority;

    public Parameters(String processId, int arrivalTime, int burstTime) {

        this.processId = processId;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;

    }

    public String getProcessId() {
        return processId;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    // For function FCFS
    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
        this.turnaroundTime = completionTime - arrivalTime;
        this.waitingTime = turnaroundTime - burstTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    @Override
    public String toString() {
        return " [process " + processId + ", arrivalTime=" + arrivalTime + ", burstTime=" + burstTime + "]";
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

}