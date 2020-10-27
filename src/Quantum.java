public class Quantum {
    private long processID;
    private long startTime;
    private long finishTime;

    public Quantum(long processID, long startTime, long finishTime) {
        this.processID = processID;
        this.startTime = startTime;
        this.finishTime = finishTime;
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

    @Override
    public String toString() {
        return "Quantum{" +
                "processID=" + processID +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                '}';
    }
}
