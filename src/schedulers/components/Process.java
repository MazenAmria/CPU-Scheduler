package schedulers.components;

public class Process implements Comparable<Process> {
    private long processID;
    private double arrivalTime;
    private double taskDuration; //Burst
    private long repeat;
    private long arrivingInterval;
    private long deadLine;

    public Process(long processID, double arrivalTime, double taskDuration, long repeat, long arrivingInterval, long deadLine) {
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

    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getTaskDuration() {
        return taskDuration;
    }

    public void setTaskDuration(double taskDuration) {
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
        return "Schedulers.Components.Process{" +
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
        return Double.compare(this.arrivalTime, o.getArrivalTime());
    }
}
