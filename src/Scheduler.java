import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

abstract public class Scheduler implements Runnable {
    protected ArrayList<Process> processes;
    protected ArrayList<Process> readyQueue;
    protected ArrayList<Record> processesLog;
    protected ArrayList<Quantum> cpuLog;
    protected long currentTime;

    public Scheduler(ArrayList<Process> processes) {
        this.processes = new ArrayList<>(processes);
        // Set the tiebreaking
        Collections.sort(this.processes, Comparator.comparingLong(Process::getProcessID));
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
