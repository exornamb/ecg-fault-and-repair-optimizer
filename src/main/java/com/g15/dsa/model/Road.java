package com.g15.dsa.model;

/**
 * Represents a road or feeder line connecting two ECG locations,
 * forming an edge in the service network graph.
 */
public class Road {

    private String roadId;
    private String fromLocationId;
    private String toLocationId;
    private double distanceKm;
    private int travelTimeMin;
    private String roadType;
    private boolean bidirectional;

    public Road() {}

    public Road(String roadId, String fromLocationId, String toLocationId,
                double distanceKm, int travelTimeMin, String roadType, boolean bidirectional) {
        this.roadId = roadId;
        this.fromLocationId = fromLocationId;
        this.toLocationId = toLocationId;
        this.distanceKm = distanceKm;
        this.travelTimeMin = travelTimeMin;
        this.roadType = roadType;
        this.bidirectional = bidirectional;
    }

    public String getRoadId() { return roadId; }
    public void setRoadId(String roadId) { this.roadId = roadId; }

    public String getFromLocationId() { return fromLocationId; }
    public void setFromLocationId(String fromLocationId) { this.fromLocationId = fromLocationId; }

    public String getToLocationId() { return toLocationId; }
    public void setToLocationId(String toLocationId) { this.toLocationId = toLocationId; }

    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }

    public int getTravelTimeMin() { return travelTimeMin; }
    public void setTravelTimeMin(int travelTimeMin) { this.travelTimeMin = travelTimeMin; }

    public String getRoadType() { return roadType; }
    public void setRoadType(String roadType) { this.roadType = roadType; }

    public boolean isBidirectional() { return bidirectional; }
    public void setBidirectional(boolean bidirectional) { this.bidirectional = bidirectional; }

    @Override
    public String toString() {
        return "Road{" + roadId + ": " + fromLocationId + " -> " + toLocationId +
               " (" + distanceKm + "km, " + travelTimeMin + "min)}";
    }
}
