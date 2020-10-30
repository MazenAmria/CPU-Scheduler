package schedulers.components;

public class ProcessContainer {
    private Process process;
    private double remainingTime;
    private long age;

    public ProcessContainer(Process process, double remainingTime, long age) {
        this.process = process;
        this.remainingTime = remainingTime;
        this.age = age;
    }

    public Process getProcess() {
        return process;
    }

    public double getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(double remainingTime) {
        this.remainingTime = remainingTime;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public long getPriority() {
        return this.process.getProcessID() - this.getAge();
    }

    public long getProcessID() {
        return this.process.getProcessID();
    }

    public double getArrivalTime() {
        return this.process.getArrivalTime();
    }

    public double getTaskDuration() {
        return this.process.getTaskDuration();
    }
}
