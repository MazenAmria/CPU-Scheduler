import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NonPreemptiveExplicitPriorityScheduler extends Scheduler {
    private final long ageFactor;

    public NonPreemptiveExplicitPriorityScheduler(ArrayList<Process> processes, long ageFactor) {
        super(processes);
        this.ageFactor = ageFactor;
    }

    Record findRecordByPID(ArrayList<Record> records, long PID){
        for(Record record : records){
            if(record.getProcessID() == PID) return record;
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
        for(ProcessContainer process : this.processes){
            process.setAge(0);
            process.setRemainingTime(process.getTaskDuration());
        }
        // While there are processes to execute
        while(finished < this.processes.size()){
            System.out.println("NPEP");
            // Add new arrival processes to ready queue (depending on their burst)
            while(this.processes.size() > cursor && this.processes.get(cursor).getArrivalTime() <= this.currentTime){
                // Insert
                this.readyQueue.add(this.processes.get(cursor));
                this.processesLog.add(new Record(
                        this.processes.get(cursor).getProcessID(),
                        Long.MAX_VALUE,
                        Long.MIN_VALUE,
                        this.processes.get(cursor).getTaskDuration(),
                        this.processes.get(cursor).getArrivalTime()
                ));
                cursor++;
            }
            // Sort
            Collections.sort(this.readyQueue, Comparator.comparingLong(ProcessContainer::getPriority));
            if(!readyQueue.isEmpty()) {
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
                // Update Ages
                for (ProcessContainer process : this.readyQueue) {
                    process.setAge(process.getAge() + this.ageFactor * this.readyQueue.get(0).getTaskDuration());
                }
                // Skip the execution time
                this.currentTime += this.readyQueue.get(0).getTaskDuration();
                // Terminate the process
                this.readyQueue.remove(0);
                finished++;
            }else{
                this.currentTime++;
            }
        }
    }

}
