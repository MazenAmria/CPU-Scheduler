import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ShortestJobFirstScheduler extends Scheduler {
    public ShortestJobFirstScheduler(ArrayList<Process> processes) {
        super(processes);
    }

    @Override
    public void schedule() {
        // Sort The processes by arrival time
        Collections.sort(this.processes);
        // Set a cursor to traverse the processes
        int cursor = 0;
        // Number of finished processes
        int finished = 0;
        // Clearing the Ages and Remaining Times for All Procesess
        for(Process process : this.processes){
            process.setAge(0);
            process.setRemainingTime(process.getTaskDuration());
        }
        // While there are processes to execute
        while(finished < this.processes.size()){
            // Add new arrival processes to ready queue (depending on their burst)
            while(this.processes.size() > cursor && this.processes.get(cursor).getArrivalTime() <= this.currentTime){
                // Find The position
                int index = Collections.binarySearch(
                        this.readyQueue,
                        this.processes.get(cursor),
                        Comparator.comparing(Process::getTaskDuration)
                );
                if(index < 0) index = -1 - index;
                // Insert
                this.readyQueue.add(
                        index,
                        this.processes.get(cursor)
                );
                cursor++;
            }
            if(!this.readyQueue.isEmpty()) {
                // Finish the process
                this.readyQueue.get(0).setRemainingTime(0);
                // Save process execution in the log
                this.processesLog.add(new Record(
                        this.readyQueue.get(0).getProcessID(),
                        currentTime,
                        currentTime + this.readyQueue.get(0).getTaskDuration(),
                        this.readyQueue.get(0).getTaskDuration(),
                        this.readyQueue.get(0).getArrivalTime()
                ));
                this.cpuLog.add(new Quantum(
                        this.readyQueue.get(0).getProcessID(),
                        currentTime,
                        currentTime + this.readyQueue.get(0).getTaskDuration()
                ));
                // Skip the execution time
                this.currentTime += this.readyQueue.get(0).getTaskDuration();
                // Terminate the process
                this.readyQueue.remove(0);
                finished++;
            }else{
                // If no processes in the queue, skip to the next quantum
                this.currentTime++;
            }
        }
    }
}
