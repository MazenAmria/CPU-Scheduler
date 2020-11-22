package schedulers.components;

public class ProcessContainer {
    private final Process process;
    private double remainingTime;
    private double age;

    public ProcessContainer(Process process, double remainingTime, double age) {
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

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public void incrementAge(double ageFactor) {
        this.age += ageFactor;
    }

    public double getPriority() {
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
