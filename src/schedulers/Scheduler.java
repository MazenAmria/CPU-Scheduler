package schedulers;

import schedulers.components.Process;
import schedulers.components.ProcessContainer;
import schedulers.components.Quantum;
import schedulers.components.Record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

abstract public class Scheduler implements Runnable {
    protected ArrayList<ProcessContainer> processes;
    protected ArrayList<ProcessContainer> readyQueue;
    protected ArrayList<Record> processesLog;
    protected ArrayList<Quantum> cpuLog;
    protected double currentTime;

    public Scheduler(ArrayList<Process> processes) {
        this.processes = new ArrayList<>();
        for (Process process : processes){
            this.processes.add(new ProcessContainer(
                    process,
                    process.getTaskDuration(),
                    0
            ));
        }
        // Set the tiebreaking
        Collections.sort(this.processes, Comparator.comparingLong(ProcessContainer::getProcessID));
        this.readyQueue = new ArrayList<>();
        this.processesLog = new ArrayList<>();
        this.cpuLog = new ArrayList<>();
        this.currentTime = 0;
    }

    public ArrayList<Record> getProcessesLog() {
        return processesLog;
    }

    public ArrayList<Quantum> getCpuLog() {
        return cpuLog;
    }
}
