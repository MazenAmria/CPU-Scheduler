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
    private final double IOPercentage;

    public MultiprogrammedWithUniformIOPercentage(ArrayList<Process> processes, double IOPercentage) throws InputMismatchException {
        super(processes);
        if (Double.compare(IOPercentage, 100.0) == 0)
            throw new InputMismatchException("IO Percentage should be less than 100.0 or processes will never be processed or ended!");
        this.IOPercentage = IOPercentage / 100.0;
    }

    private double share(int numberOfProcesses) {
        if (numberOfProcesses == 0) return 0;
        return (1 - Math.pow(IOPercentage, numberOfProcesses));
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
