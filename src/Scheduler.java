import java.util.ArrayList;

abstract public class Scheduler {
    protected ArrayList<Process> processes;
    protected ArrayList<Process> readyQueue;
    protected ArrayList<Record> processesLog;
    protected ArrayList<Quantum> cpuLog;
    protected long currentTime;

    public Scheduler(ArrayList<Process> processes) {
        this.processes = processes;
        this.readyQueue = new ArrayList<>();
        this.processesLog = new ArrayList<>();
        this.cpuLog = new ArrayList<>();
        this.currentTime = 0;
    }

    abstract public void schedule();

    public ArrayList<Record> getProcessesLog() {
        return processesLog;
    }

    public ArrayList<Quantum> getCpuLog() {
        return cpuLog;
    }
}
