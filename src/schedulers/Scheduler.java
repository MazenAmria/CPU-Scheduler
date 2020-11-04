package schedulers;

import javafx.util.Pair;
import schedulers.components.Process;
import schedulers.components.Record;
import schedulers.components.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

abstract public class Scheduler implements Runnable {
    protected ArrayList<ProcessContainer> processes;
    protected ArrayList<ProcessContainer> readyQueue;
    protected ArrayList<Record> processesLog;
    protected ArrayList<Quantum> cpuLog;
    protected double currentTime;
    public int numOfProcesses;
    private final ArrayList<Pair<Long, Double>> processesTable;

    public Scheduler(ArrayList<Process> processes) {
        this.processes = new ArrayList<>();
        this.numOfProcesses = processes.size();
        this.processesTable = new ArrayList<>();
        for (Process process : processes) {
            this.processes.add(new ProcessContainer(
                    process,
                    process.getTaskDuration(),
                    0
            ));
            this.processesTable.add(new Pair<>(process.getProcessID(), process.getArrivalTime()));
        }
        Collections.sort(this.processesTable, Comparator.comparingDouble(Pair::getValue));
        // Set the tiebreaking
        Collections.sort(this.processes, Comparator.comparingLong(ProcessContainer::getProcessID));
        this.readyQueue = new ArrayList<>();
        this.processesLog = new ArrayList<>();
        this.cpuLog = new ArrayList<>();
        this.currentTime = 0;
    }

    public int getIndexOfProcess(long processID) {
        for (int i = 0; i < this.processesTable.size(); i++) {
            if (this.processesTable.get(i).getKey().compareTo(processID) == 0) return i;
        }
        return -1;
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
