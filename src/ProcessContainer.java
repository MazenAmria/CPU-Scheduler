public class ProcessContainer {
    private Process process;
    private long remainingTime;
    private long age;

    public ProcessContainer(Process process, long remainingTime, long age) {
        this.process = process;
        this.remainingTime = remainingTime;
        this.age = age;
    }

    public Process getProcess() {
        return process;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public long getPriority(){
        return this.process.getProcessID() - this.getAge();
    }
    public long getProcessID() {
        return this.process.getProcessID();
    }

    public long getArrivalTime() {
        return this.process.getArrivalTime();
    }


    public long getTaskDuration() {
        return this.process.getTaskDuration();
    }
}
