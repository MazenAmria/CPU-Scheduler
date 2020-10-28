public class Process implements Comparable<Process> {
    private long processID;
    private long arrivalTime;
    private long taskDuration; //Burst
    private long repeat;
    private long arrivingInterval;
    private long deadLine;

    public Process(long processID, long arrivalTime, long taskDuration, long repeat, long arrivingInterval, long deadLine) {
        this.processID = processID;
        this.arrivalTime = arrivalTime;
        this.taskDuration = taskDuration;
        this.repeat = repeat;
        this.arrivingInterval = arrivingInterval;
        this.deadLine = deadLine;
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

    @Override
    public String toString() {
        return "Process{" +
                "processID=" + processID +
                ", arrivalTime=" + arrivalTime +
                ", taskDuration=" + taskDuration +
                ", repeat=" + repeat +
                ", arrivingInterval=" + arrivingInterval +
                ", deadLine=" + deadLine +
                '}';
    }

    @Override
    public int compareTo(Process o) {
        return Long.compare(this.arrivalTime, o.getArrivalTime());
    }
}
