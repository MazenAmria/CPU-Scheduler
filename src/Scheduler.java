import java.util.ArrayList;

abstract public class Scheduler {
    protected ArrayList<Process> processes;
    protected ArrayList<Process> readyQueue;
    protected ArrayList<Record> processesLog;
    protected long currentTime;

    public Scheduler(ArrayList<Process> processes) {
        this.processes = processes;
        this.currentTime = 0;
    }

    abstract public void schedule();
}
