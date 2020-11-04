package schedulers.algorithms;

import schedulers.Scheduler;
import schedulers.components.Process;
import schedulers.components.ProcessContainer;
import schedulers.components.Quantum;
import schedulers.components.Record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;

public class MultiprogrammedWithUniformIOPercentage extends Scheduler {
    private final double[] shares;

    public MultiprogrammedWithUniformIOPercentage(ArrayList<Process> processes, int IOPercentage) {
        super(processes);
        switch (IOPercentage) {
            case 20 -> this.shares = new double[]{0.8, 0.96, 0.992, 0.9984, 0.99968, 0.999936};
            case 50 -> this.shares = new double[]{0.5, 0.75, 0.875, 0.9375, 0.96875, 0.984375};
            case 80 -> this.shares = new double[]{0.2, 0.36, 0.448, 0.5904, 0.67232, 0.737856};
            case 90 -> this.shares = new double[]{0.1, 0.19, 0.271, 0.3439, 0.40951, 0.468559};
            default -> throw new InputMismatchException();
        }
    }

    private double share(int numberOfProcesses) {
        return (this.shares[numberOfProcesses - 1]) / numberOfProcesses;
    }

    @Override
    public void run() {
        // Sort The processes by arrival time
        Collections.sort(this.processes, Comparator.comparing(ProcessContainer::getProcess));
        // Clearing the Ages and Remaining Times for All Processes
        for (ProcessContainer process : this.processes) {
            process.setAge(0);
            process.setRemainingTime(process.getTaskDuration());
        }
        int cursor = 0;
        int finished = 0;
        while (finished < this.processes.size()) {
            if (Double.compare(this.currentTime, Double.NaN) == 0) System.exit(0);
            // Add new arrival processes to ready queue (depending on their burst)
            while (this.processes.size() > cursor && Double.compare(this.processes.get(cursor).getArrivalTime(), this.currentTime) <= 0) {
                // Insert
                this.readyQueue.add(this.processes.get(cursor));
                cursor++;
            }
            Collections.sort(this.readyQueue, Comparator.comparingDouble(ProcessContainer::getRemainingTime));
            if (!this.readyQueue.isEmpty()) {
                if (this.processes.size() > cursor
                        && Double.compare(this.processes.get(cursor).getArrivalTime(), this.currentTime + (this.readyQueue.get(0).getRemainingTime() / share(this.readyQueue.size()))) <= 0) {
                    for (ProcessContainer process : readyQueue) {
                        process.setRemainingTime(process.getRemainingTime() - ((this.processes.get(cursor).getArrivalTime() - this.currentTime) * share(this.readyQueue.size())));
                    }
                    this.currentTime = this.processes.get(cursor).getArrivalTime();
                } else {
                    double interval = this.readyQueue.get(0).getRemainingTime();
                    this.currentTime += (interval / share(this.readyQueue.size()));
                    for (ProcessContainer process : readyQueue) {
                        process.setRemainingTime(process.getRemainingTime() - interval);
                    }
                    while (!this.readyQueue.isEmpty() && Double.compare(this.readyQueue.get(0).getRemainingTime(), 0) == 0) {
                        this.processesLog.add(new Record(
                                this.readyQueue.get(0).getProcessID(),
                                this.readyQueue.get(0).getArrivalTime(),
                                this.currentTime,
                                this.readyQueue.get(0).getTaskDuration(),
                                this.readyQueue.get(0).getArrivalTime()
                        ));
                        this.cpuLog.add(new Quantum(
                                this.readyQueue.get(0).getProcessID(),
                                this.readyQueue.get(0).getArrivalTime(),
                                this.currentTime
                        ));
                        this.readyQueue.remove(0);
                        finished++;
                    }
                }
            } else {
                this.currentTime = Math.floor(this.currentTime + 1);
            }
        }
    }
}
