import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ShortestRemainingTimeFirstScheduler extends Scheduler{

	public ShortestRemainingTimeFirstScheduler(ArrayList<Process> processes) {
        super(processes);
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
            // Add new arrival processes to ready queue
            while(this.processes.size() > cursor && this.processes.get(cursor).getArrivalTime() <= this.currentTime){
                // Insert
                this.readyQueue.add(this.processes.get(cursor));
                this.processesLog.add(new Record(
                        this.processes.get(cursor).getProcessID(),
                        Double.MAX_VALUE,
                        Double.MIN_VALUE,
                        this.processes.get(cursor).getTaskDuration(),
                        this.processes.get(cursor).getArrivalTime()
                ));
                cursor++;
            }
            Collections.sort(this.readyQueue, Comparator.comparingDouble(ProcessContainer::getRemainingTime));
            if(!this.readyQueue.isEmpty()) {
            	this.readyQueue.get(0).setRemainingTime(this.readyQueue.get(0).getRemainingTime() - 1);
                Record record = findRecordByPID(this.processesLog, this.readyQueue.get(0).getProcessID());
                record.setStartTime(Double.min(currentTime, record.getStartTime()));
                record.setFinishTime(Double.max(currentTime, record.getFinishTime()));
                // Record the CPU progress
                if (this.cpuLog.isEmpty() || this.cpuLog.get(this.cpuLog.size() - 1).getProcessID() != this.readyQueue.get(0).getProcessID()) {
                    this.cpuLog.add(new Quantum(
                            this.readyQueue.get(0).getProcessID(),
                            this.currentTime,
                            this.currentTime + 1
                    ));
                } else {
                    this.cpuLog.get(this.cpuLog.size() - 1).setFinishTime(this.currentTime + 1);
                }
                if (Double.compare(this.readyQueue.get(0).getRemainingTime(), 0) == 0) {
                    // Terminate the process
                    this.readyQueue.remove(0);
                    finished++;
                }
                
                this.currentTime = Math.floor(this.currentTime + 1);
            }
            else {
            	this.currentTime = Math.floor(this.currentTime + 1);
            }  
        }
    }
	
}
