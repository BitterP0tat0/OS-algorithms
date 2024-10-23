package Homework;

public class Test {
    public static void main(String[] args) {

        Algorithms algorithms = new Algorithms();
        algorithms.generateProcesses(20);
        algorithms.sortArrivalTime();

        algorithms.printProcesses();
        algorithms.calculateMetricsFCFS();
        algorithms.calculateMetricsSJF();
        algorithms.calculateMetricsNonPreemptivePriority();
        algorithms.calculateMetricsMultilevelQueue();
        algorithms.calculateMetricsRR(50);

    }
}