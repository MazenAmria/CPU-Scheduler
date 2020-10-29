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
}
