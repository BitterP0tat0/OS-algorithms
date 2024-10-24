package Homework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.LinkedList;
import java.util.Queue;

public class Algorithms {
    List<Parameters> processes = new ArrayList<>();
    Random random = new Random();

    public void generateProcesses(int numProcesses) {
        // add a hashmap for not having same times
        Set<Integer> arrivalTimes = new HashSet<>();
        int priority;
        for (int i = 1; i <= numProcesses; i++) {
            String processId;
            int arrivalTime;
            // for only arrival time
            do {
                arrivalTime = random.nextInt(20); // get nums between 20
                priority = random.nextInt(3);
            } while (arrivalTimes.contains(arrivalTime)); // if already exist the number

            arrivalTimes.add(arrivalTime); // add
            int burstTime = 1 + random.nextInt(10); // generate burst time
            processId = "p" + (arrivalTime + 1);// make sure the id is from smaller to biger
            Parameters process = new Parameters(processId, arrivalTime, burstTime, priority);
            processes.add(process);
        }
    }

    // For FCFS
    public void calculateMetricsFCFS() {
        int totalTurnaroundTime = 0;
        int totalWaitingTime = 0;

        int completedProcesses = 0;
        int currentTime = 0;

        for (Parameters process : processes) {
            // calculate arrive time
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();
            }

            // completion time
            currentTime += process.getBurstTime();
            process.setCompletionTime(currentTime);

            // total time
            totalTurnaroundTime += process.getTurnaroundTime();
            totalWaitingTime += process.getWaitingTime();

            completedProcesses++;
        }

        double averageTurnaroundTime = (double) totalTurnaroundTime / completedProcesses;
        double averageWaitingTime = (double) totalWaitingTime / completedProcesses;
        System.out.printf("FCFS Average Turnaround Time: %.2f ms%n", averageTurnaroundTime);
        System.out.printf("FCFS Average Waiting Time: %.2f ms%n", averageWaitingTime);

    }

    // For SJF
    public void calculateMetricsSJF() {
        int totalTurnaroundTime = 0;
        int totalWaitingTime = 0;

        int completedProcesses = 0;
        int currentTime = 0;

        // Create a list to hold processes that have arrived
        List<Parameters> readyQueue = new ArrayList<>();

        while (completedProcesses < processes.size()) {
            // Add processes that have arrived to the ready queue
            for (Parameters process : processes) {
                if (process.getArrivalTime() <= currentTime && !readyQueue.contains(process)) {
                    readyQueue.add(process);
                }
            }

            if (readyQueue.isEmpty()) {
                // If no processes are ready, move the current time forward
                currentTime++;
                continue;
            }

            Parameters currentProcess = readyQueue.get(0);

            // Completion time
            currentTime += currentProcess.getBurstTime();
            currentProcess.setCompletionTime(currentTime);

            // Calculate turnaround and waiting time
            totalTurnaroundTime += currentProcess.getTurnaroundTime();
            totalWaitingTime += currentProcess.getWaitingTime();

            completedProcesses++;
            readyQueue.remove(currentProcess); // Remove the completed process
        }

        double averageTurnaroundTime = (double) totalTurnaroundTime / completedProcesses;
        double averageWaitingTime = (double) totalWaitingTime / completedProcesses;

        System.out.printf("SJF Average Turnaround Time: %.2f ms%n", averageTurnaroundTime);
        System.out.printf("SJF Average Waiting Time: %.2f ms%n", averageWaitingTime);
    }

    // For RR

    public void calculateMetricsRR(int timeQuantum) {
        int totalTurnaroundTime = 0;
        int totalWaitingTime = 0;

        int completedProcesses = 0;
        int currentTime = 0;
        // Create a list for time
        int[] remainingBurstTime = new int[processes.size()];
        for (int i = 0; i < processes.size(); i++) {
            remainingBurstTime[i] = processes.get(i).getBurstTime();
        }

        boolean allProcessesCompleted = false;

        while (!allProcessesCompleted) {
            allProcessesCompleted = true;

            for (int i = 0; i < processes.size(); i++) {
                Parameters process = processes.get(i);

                if (remainingBurstTime[i] > 0) {
                    allProcessesCompleted = false;

                    if (currentTime < process.getArrivalTime()) {
                        currentTime = process.getArrivalTime();
                    }

                    if (remainingBurstTime[i] > timeQuantum) {
                        currentTime += timeQuantum;
                        remainingBurstTime[i] -= timeQuantum;
                    } else {
                        currentTime += remainingBurstTime[i];
                        remainingBurstTime[i] = 0;
                        process.setCompletionTime(currentTime);

                        totalTurnaroundTime += process.getTurnaroundTime();
                        totalWaitingTime += process.getWaitingTime();

                        completedProcesses++;
                    }
                }
            }
        }

        double averageTurnaroundTime = (double) totalTurnaroundTime / completedProcesses;
        double averageWaitingTime = (double) totalWaitingTime / completedProcesses;
        System.out.printf("RR Average Turnaround Time: %.2f ms%n", averageTurnaroundTime);
        System.out.printf("RR Average Waiting Time: %.2f ms%n", averageWaitingTime);
    }

    // calculateMetricsNonPreemptivePriority
    public void calculateMetricsNonPreemptivePriority() {

        int currentTime = 0;
        double totalTurnaroundTime = 0;
        double totalWaitingTime = 0;

        processes.sort(Comparator.comparingInt(Parameters::getPriority));

        for (Parameters process : processes) {
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();
            }

            process.setCompletionTime(currentTime + process.getBurstTime());

            int turnaroundTime = process.getCompletionTime() - process.getArrivalTime();
            int waitingTime = turnaroundTime - process.getBurstTime();

            process.setTurnaroundTime(turnaroundTime);
            process.setWaitingTime(waitingTime);

            currentTime += process.getBurstTime();
            totalTurnaroundTime += turnaroundTime;
            totalWaitingTime += waitingTime;
        }

        double averageTurnaroundTime = totalTurnaroundTime / processes.size();
        double averageWaitingTime = totalWaitingTime / processes.size();
        System.out.printf("Non-Preemptive Priority Average Turnaround Time: %.2f ms%n", averageTurnaroundTime);
        System.out.printf("Non-Preemptive Priority Average Waiting Time: %.2f ms%n", averageWaitingTime);
    }

    public void calculateMetricsMultilevelQueue() {
        Queue<Parameters> highPriorityQueue = new LinkedList<>();
        Queue<Parameters> mediumPriorityQueue = new LinkedList<>();
        Queue<Parameters> lowPriorityQueue = new LinkedList<>();

        // 将进程按优先级分配到不同的队列中
        for (Parameters process : processes) {
            if (process.getPriority() == 0) {
                highPriorityQueue.add(process); // superior
            } else if (process.getPriority() == 1) {
                mediumPriorityQueue.add(process); // mid
            } else {
                lowPriorityQueue.add(process); // low
            }
        }

        int currentTime = 0;
        int completedProcesses = 0;
        int timeQuantum = 1;
        while (!highPriorityQueue.isEmpty()) {
            Parameters process = highPriorityQueue.poll();
            if (process.getBurstTime() > timeQuantum) {
                currentTime += timeQuantum;
                process.setBurstTime(process.getBurstTime() - timeQuantum);
                highPriorityQueue.add(process);
            } else {
                currentTime += process.getBurstTime();
                process.setCompletionTime(currentTime);
                completedProcesses++;
            }
        }

        List<Parameters> sortedMediumQueue = new ArrayList<>(mediumPriorityQueue);
        sortedMediumQueue.sort(Comparator.comparingInt(Parameters::getBurstTime));
        for (Parameters process : sortedMediumQueue) {
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();
            }
            currentTime += process.getBurstTime();
            process.setCompletionTime(currentTime);
            completedProcesses++;
        }

        while (!lowPriorityQueue.isEmpty()) {
            Parameters process = lowPriorityQueue.poll();
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();
            }
            currentTime += process.getBurstTime();
            process.setCompletionTime(currentTime);
            completedProcesses++;
        }

        // 计算平均周转时间和平均等待时间
        double totalTurnaroundTime = 0;
        double totalWaitingTime = 0;

        for (Parameters process : processes) {
            totalTurnaroundTime += process.getTurnaroundTime();
            totalWaitingTime += process.getWaitingTime();
        }

        double averageTurnaroundTime = totalTurnaroundTime / completedProcesses;
        double averageWaitingTime = totalWaitingTime / completedProcesses;

        System.out.printf("calculateMetricsMultilevelQueue Average Turnaround Time: %.4f ms%n", averageTurnaroundTime);
        System.out.printf("calculateMetricsMultilevelQueue Average Waiting Time: %.4f ms%n", averageWaitingTime);
    }

    public void sortArrivalTime() {

        Collections.sort(processes, Comparator.comparingInt(Parameters::getArrivalTime));

    }

    public void printProcesses() {
        for (Parameters process : processes) {
            System.out.println(process);

        }

    }
}
