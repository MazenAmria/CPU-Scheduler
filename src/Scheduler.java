import java.util.ArrayList;

abstract public class Scheduler {
    private ArrayList<Process> processes;
    private ArrayList<Record> processesLog;
    private long currentTime;

    public Scheduler(ArrayList<Process> processes) {
        this.processes = processes;
        this.currentTime = 0;
    }

    abstract public void schedule();
}
