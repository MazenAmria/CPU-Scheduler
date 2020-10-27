public class Record {
    private long processID;
    private long startTime;
    private long finishTime;
    private long turnAround;
    private double weightedTurnAround;
    private long waitTime;

    public Record(long processID, long startTime, long finishTime, long taskDuration, long arrivalTime) {
        this.processID = processID;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.turnAround = finishTime - arrivalTime;
        this.weightedTurnAround = this.turnAround / (double) taskDuration;
        this.waitTime = this.turnAround - taskDuration;
    }

    public Record(long processID, long startTime, long finishTime, long turnAround, double weightedTurnAround, long waitTime) {
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public long getTurnAround() {
        return turnAround;
    }

    public void setTurnAround(long turnAround) {
        this.turnAround = turnAround;
    }

    public double getWeightedTurnAround() {
        return weightedTurnAround;
    }

    public void setWeightedTurnAround(double weightedTurnAround) {
        this.weightedTurnAround = weightedTurnAround;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }
}
