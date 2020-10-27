public class Process implements Comparable<Process> {
    public static long ageFactor;
    private long processID;
    private long arrivalTime;
    private long taskDuration; //Burst
    private long repeat;
    private long arrivingInterval;
    private long deadLine;
    private long remainingTime;
    private long age;

    public Process(long processID, long arrivalTime, long taskDuration, long repeat, long arrivingInterval, long deadLine) {
        this.processID = processID;
        this.arrivalTime = arrivalTime;
        this.taskDuration = taskDuration;
        this.repeat = repeat;
        this.arrivingInterval = arrivingInterval;
        this.deadLine = deadLine;
        this.remainingTime = taskDuration;
    }

    public long getProcessID() {
        return processID;
    }

    public void setProcessID(long processID) {
        this.processID = processID;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public long getTaskDuration() {
        return taskDuration;
    }

    public void setTaskDuration(long taskDuration) {
        this.taskDuration = taskDuration;
    }

    public long getRepeat() {
        return repeat;
    }

    public void setRepeat(long repeat) {
        this.repeat = repeat;
    }

    public long getArrivingInterval() {
        return arrivingInterval;
    }

    public void setArrivingInterval(long arrivingInterval) {
        this.arrivingInterval = arrivingInterval;
    }

    public long getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(long deadLine) {
        this.deadLine = deadLine;
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
        return this.getProcessID() - Process.ageFactor * this.getAge();
    }

    @Override
    public String toString() {
        return "Process{" +
                "processID=" + processID +
                ", arrivalTime=" + arrivalTime +
                ", taskDuration=" + taskDuration +
                ", repeat=" + repeat +
                ", arrivingInterval=" + arrivingInterval +
                ", deadLine=" + deadLine +
                ", remainingTime=" + remainingTime +
                ", age=" + age +
                '}';
    }

    @Override
    public int compareTo(Process o) {
        return Long.compare(this.arrivalTime, o.getArrivalTime());
    }
}
