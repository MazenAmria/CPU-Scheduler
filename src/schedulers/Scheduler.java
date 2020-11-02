package schedulers;

import schedulers.components.*;
import schedulers.components.Process;
import schedulers.components.Record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

abstract public class Scheduler implements Runnable {
    protected ArrayList<ProcessContainer> processes;
    protected ArrayList<ProcessContainer> readyQueue;
    protected ArrayList<Record> processesLog;
    protected ArrayList<Quantum> cpuLog;
    protected double currentTime;
    public int numOfProcesses;
    public HashMap<Long, Integer> processesTable;

    public Scheduler(ArrayList<Process> processes) {
        this.processes = new ArrayList<>();
        this.numOfProcesses = processes.size();
        this.processesTable = new HashMap<>();
        int counter = 0;
        for (Process process : processes){
            this.processes.add(new ProcessContainer(
                    process,
                    process.getTaskDuration(),
                    0
            ));
            this.processesTable.put(process.getProcessID(), counter);
            counter++;
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

    public ArrayList<Visualisable> getCpuLogVis() {
        ArrayList<Visualisable> visualisables = new ArrayList<>();
        for (Quantum quantum : this.getCpuLog()) {
            visualisables.add(quantum);
        }
        return visualisables;
    }

    public ArrayList<Visualisable> getProcessesLogVis() {
        ArrayList<Visualisable> visualisables = new ArrayList<>();
        for (Record record : this.getProcessesLog()) {
            visualisables.add(record);
        }
        return visualisables;
    }
}
