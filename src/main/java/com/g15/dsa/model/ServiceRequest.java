package com.g15.dsa.model;

/**
 * Represents a service request for power restoration or fault repair
 * submitted to the ECG Dumsor Response system.
 */
public class ServiceRequest implements Comparable<ServiceRequest> {

    private String requestId;
    private String locationId;
    private String faultType;
    private int urgency;
    private String status;
    private String assignedCrewId;
    private String reportedAt;
    private int estimatedMinutes;

    public ServiceRequest() {}

    public ServiceRequest(String requestId, String locationId, String faultType,
                          int urgency, String status, String reportedAt) {
        this.requestId = requestId;
        this.locationId = locationId;
        this.faultType = faultType;
        this.urgency = urgency;
        this.status = status;
        this.reportedAt = reportedAt;
    }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getLocationId() { return locationId; }
    public void setLocationId(String locationId) { this.locationId = locationId; }

    public String getFaultType() { return faultType; }
    public void setFaultType(String faultType) { this.faultType = faultType; }

    public int getUrgency() { return urgency; }
    public void setUrgency(int urgency) { this.urgency = urgency; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAssignedCrewId() { return assignedCrewId; }
    public void setAssignedCrewId(String assignedCrewId) { this.assignedCrewId = assignedCrewId; }

    public String getReportedAt() { return reportedAt; }
    public void setReportedAt(String reportedAt) { this.reportedAt = reportedAt; }

    public int getEstimatedMinutes() { return estimatedMinutes; }
    public void setEstimatedMinutes(int estimatedMinutes) { this.estimatedMinutes = estimatedMinutes; }

    /** Higher urgency = dispatched first. */
    @Override
    public int compareTo(ServiceRequest other) {
        return Integer.compare(other.urgency, this.urgency);
    }

    @Override
    public String toString() {
        return "ServiceRequest{" + requestId + " [urgency=" + urgency + "] " + locationId + " -> " + status + "}";
    }
}
