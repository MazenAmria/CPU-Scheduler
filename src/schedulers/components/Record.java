package schedulers.components;

public class Record {
    private long processID;
    private double startTime;
    private double finishTime;
    private double arrivalTime;
    private double taskDuration;
    private double turnAround;
    private double weightedTurnAround;
    private double waitTime;

    public Record(long processID, double startTime, double finishTime, double taskDuration, double arrivalTime) {
        this.processID = processID;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.taskDuration = taskDuration;
        this.arrivalTime = arrivalTime;
        this.turnAround = finishTime - arrivalTime;
        this.weightedTurnAround = this.turnAround / taskDuration;
        this.waitTime = this.turnAround - taskDuration;
    }

    public Record(long processID, double startTime, double finishTime, double turnAround, double weightedTurnAround, double waitTime) {
        this.processID = processID;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.turnAround = turnAround;
        this.weightedTurnAround = weightedTurnAround;
        this.waitTime = waitTime;
    }

    public long getProcessID() {
        return processID;
    }

    public void setProcessID(long processID) {
        this.processID = processID;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(double finishTime) {
        this.finishTime = finishTime;
        this.turnAround = finishTime - this.arrivalTime;
        this.weightedTurnAround = this.turnAround / (double) this.taskDuration;
        this.waitTime = this.turnAround - this.taskDuration;
    }

    public double getTurnAround() {
        return turnAround;
    }

    public void setTurnAround(double turnAround) {
        this.turnAround = turnAround;
    }

    public double getWeightedTurnAround() {
        return weightedTurnAround;
    }

    public void setWeightedTurnAround(double weightedTurnAround) {
        this.weightedTurnAround = weightedTurnAround;
    }

    public double getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(double waitTime) {
        this.waitTime = waitTime;
    }

    @Override
    public String toString() {
        return "Schedulers.Components.Record{" +
                "processID=" + processID +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                ", turnAround=" + turnAround +
                ", weightedTurnAround=" + weightedTurnAround +
                ", waitTime=" + waitTime +
                '}';
    }
}
