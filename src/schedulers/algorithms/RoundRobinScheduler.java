package schedulers.algorithms;

import schedulers.Scheduler;
import schedulers.components.Process;
import schedulers.components.ProcessContainer;
import schedulers.components.Quantum;
import schedulers.components.Record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RoundRobinScheduler extends Scheduler {
    private final double timeQuantum;
    private ProcessContainer runningProcess;

    public RoundRobinScheduler(ArrayList<Process> processes, double timeQuantum) {
        super(processes);
        this.timeQuantum = timeQuantum;
    }

    Record findRecordByPID(ArrayList<Record> records, long PID) {
        for (Record record : records) {
            if (record.getProcessID() == PID) return record;
        }
        return null;
    }

    @Override
    public void run() {
        // Sort The processes by arrival time
        Collections.sort(this.processes, Comparator.comparing(ProcessContainer::getProcess));
        // Set a cursor to traverse the processes
        int cursor = 0;
        // Number of finished processes
        int finished = 0;
        // Clearing the Ages and Remaining Times for All Processes
        for (ProcessContainer process : this.processes) {
            process.setAge(0);
            process.setRemainingTime(process.getTaskDuration());
        }
        // While there are processes to execute
        while (finished < this.processes.size()) {
            // Add new arrival processes to ready queue
            while (this.processes.size() > cursor && this.processes.get(cursor).getArrivalTime() <= this.currentTime) {
                // Insert
                this.readyQueue.add(this.processes.get(cursor));
                this.processesLog.add(new Record(
                        this.processes.get(cursor).getProcessID(),
                        Double.MAX_VALUE,
                        Double.MIN_VALUE,
                        this.processes.get(cursor).getTaskDuration(),
                        this.processes.get(cursor).getArrivalTime()
                ));
                cursor++;
            }
            // Return the running process to the queue
            if (this.runningProcess != null && Double.compare(this.runningProcess.getRemainingTime(), 0) > 0) {
                this.readyQueue.add(runningProcess);
            }
            // Choose the next process to run
            if (!this.readyQueue.isEmpty()) {
                this.runningProcess = this.readyQueue.get(0);
                Record runningRecord = findRecordByPID(this.processesLog, this.runningProcess.getProcessID());
                runningRecord.setStartTime(Double.min(this.currentTime, runningRecord.getStartTime()));
                this.readyQueue.remove(0);
                if (Double.compare(this.runningProcess.getRemainingTime(), this.timeQuantum) <= 0) {
                    this.cpuLog.add(new Quantum(
                            this.runningProcess.getProcessID(),
                            this.currentTime,
                            this.currentTime + this.runningProcess.getRemainingTime()
                    ));
                    finished++;
                    this.currentTime += this.runningProcess.getRemainingTime();
                    this.runningProcess.setRemainingTime(0);
                    runningRecord.setFinishTime(this.currentTime);
                } else {
                    this.runningProcess.setRemainingTime(this.runningProcess.getRemainingTime() - this.timeQuantum);
                    this.cpuLog.add(new Quantum(
                            this.runningProcess.getProcessID(),
                            this.currentTime,
                            this.currentTime + this.timeQuantum
                    ));
                    this.currentTime += this.timeQuantum;
                }
            } else {
                this.currentTime = Math.floor(this.currentTime + 1);
            }
        }
    }
}
