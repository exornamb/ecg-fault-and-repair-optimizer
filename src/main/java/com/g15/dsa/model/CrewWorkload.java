package com.g15.dsa.model;

/**
 * Summary of a crew's active workload for analytics and load balancing.
 */
public class CrewWorkload {

    private String crewName;
    private String crewType;
    private int totalAssigned;
    private int pending;
    private int inProgress;
    private int completed;

    public CrewWorkload() {}

    public CrewWorkload(String crewName, String crewType, int totalAssigned, int pending, int inProgress, int completed) {
        this.crewName = crewName;
        this.crewType = crewType;
        this.totalAssigned = totalAssigned;
        this.pending = pending;
        this.inProgress = inProgress;
        this.completed = completed;
    }

    public String getCrewName() { return crewName; }
    public void setCrewName(String crewName) { this.crewName = crewName; }

    public String getCrewType() { return crewType; }
    public void setCrewType(String crewType) { this.crewType = crewType; }

    public int getTotalAssigned() { return totalAssigned; }
    public void setTotalAssigned(int totalAssigned) { this.totalAssigned = totalAssigned; }

    public int getPending() { return pending; }
    public void setPending(int pending) { this.pending = pending; }

    public int getInProgress() { return inProgress; }
    public void setInProgress(int inProgress) { this.inProgress = inProgress; }

    public int getCompleted() { return completed; }
    public void setCompleted(int completed) { this.completed = completed; }

    public double getCompletionRate() {
        if (totalAssigned == 0) return 0.0;
        return (double) completed / totalAssigned * 100.0;
    }

    @Override
    public String toString() {
        return "CrewWorkload{" + crewName + ": assigned=" + totalAssigned +
               ", pending=" + pending + ", inProgress=" + inProgress +
               ", completed=" + completed + " (" + String.format("%.1f", getCompletionRate()) + "%)}";
    }
}
