import java.util.ArrayList;
import java.util.Comparator;

public class ShortestJobFirstScheduler extends Scheduler {
    public ShortestJobFirstScheduler(ArrayList<Process> processes) {
        super(processes);
    }

    @Override
    public void schedule() {
        // Sort The processes by arrival time
        this.processes.sort(Comparator.comparingLong(Process::getArrivalTime));
        // Set a cursor to traverse the processes
        int cursor = 0;
        while(this.processes.size() > cursor){
            // Add new arrival processes to ready queue
            while(this.processes.size() > cursor && this.processes.get(cursor).getArrivalTime() <= this.currentTime){
                this.readyQueue.add(this.processes.get(cursor));
                cursor++;
            }
            if(!this.readyQueue.isEmpty()) {
                // Sorting the queue to get the shortest job
                this.readyQueue.sort(Comparator.comparingLong(Process::getTaskDuration));
                // Removing the shortest one and skipping the time quantum
                this.processesLog.add(new Record(
                        this.readyQueue.get(0).getProcessID(),
                        currentTime,
                        currentTime + this.readyQueue.get(0).getTaskDuration(),
                        this.readyQueue.get(0).getTaskDuration(),
                        this.readyQueue.get(0).getArrivalTime()
                ));
                this.readyQueue.get(0).setRemainingTime(0);
                this.currentTime += this.readyQueue.get(0).getTaskDuration();
                this.readyQueue.remove(0);
            }else{
                // If no processes in the queue, skip to the next quantum
                this.currentTime++;
            }
        }
    }
}
