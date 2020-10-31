import schedulers.*;
import schedulers.algorithms.*;
import schedulers.components.Process;
import schedulers.components.Quantum;

import java.util.ArrayList;

public class Driver {
    public static void main(String[] args){
        ArrayList<Process> processes = new ArrayList<>();
        processes.add(
                new Process(2, 1, 6, 0, 0, 0)
        );
        processes.add(
                new Process(1, 2, 10, 0, 0, 0)
        );
        processes.add(
                new Process(4, 7, 8, 0, 0, 0)
        );
        processes.add(
                new Process(103, 4, 8, 0, 0, 0)
        );
        processes.add(
                new Process(104, 3, 3, 0, 0, 0)
        );
        processes.add(
                new Process(3, 5, 2, 0, 0, 0)
        );
        Scheduler scheduler1 = new ShortestJobFirstScheduler(processes);
        Scheduler scheduler2 = new NonPreemptiveExplicitPriorityScheduler(processes, 10);
        Scheduler scheduler3 = new PreemptiveExplicitPriorityScheduler(processes, 10);
        Thread thread1 = new Thread(scheduler1);
        Thread thread2 = new Thread(scheduler2);
        Thread thread3 = new Thread(scheduler3);
        thread1.start();
        thread2.start();
        thread3.start();
        Scheduler scheduler4 = new ShortestJobFirstScheduler(processes);
        Scheduler scheduler5 = new NonPreemptiveExplicitPriorityScheduler(processes, 10);
        Scheduler scheduler6 = new PreemptiveExplicitPriorityScheduler(processes, 10);
        scheduler4.run();
        scheduler5.run();
        scheduler6.run();
        Scheduler scheduler7 = new MultiprogrammedWithUniformIOPercentage(processes, 80);
        Scheduler scheduler8 = new RoundRobinScheduler(processes, 2);
        Thread thread4 = new Thread(scheduler8);
        thread4.start();
        scheduler7.run();
        for (Quantum quantum : scheduler8.getCpuLog()) {
            System.out.println(quantum);
        }
    }
    
    public static double FindTimeQuantum(ArrayList<Process> processes) {
    	double timeQuantum = 0 ; 
    	//Sort processes based on the task duration parameter 
    	Collections.sort(processes, new Comparator<Process>() {
    	        @Override
    	        public int compare(Process  obj1 , Process  obj2) {
    	            return Double.compare(obj1.getTaskDuration(), obj2.getTaskDuration());
    	        }
    	    });
    	int numberOfProcessesToIncludeInTQ = (int)Math.ceil(0.8*(double)processes.size());
    	if(numberOfProcessesToIncludeInTQ == 0) return 0 ; 
    	timeQuantum = processes.get(numberOfProcessesToIncludeInTQ-1).getTaskDuration();
    	return timeQuantum ; 
    }
    
}
