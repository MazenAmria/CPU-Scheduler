package schedulers.components;

public class Quantum implements Visualisable {
    private long processID;
    private double startTime;
    private double finishTime;

    public Quantum(long processID, double startTime, double finishTime) {
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
    }

    @Override
    public String toString() {
        return "Schedulers.Components.Quantum{" +
                "processID=" + processID +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                '}';
    }
}
