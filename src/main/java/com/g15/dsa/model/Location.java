package com.g15.dsa.model;

/**
 * Represents an ECG substation, transformer station, or customer service location
 * on the Accra / Legon power distribution network.
 */
public class Location {

    private String locationId;
    private String name;
    private String area;
    private String locationType;
    private double xCoord;
    private double yCoord;

    public Location() {}

    public Location(String locationId, String name, String area, String locationType, double xCoord, double yCoord) {
        this.locationId = locationId;
        this.name = name;
        this.area = area;
        this.locationType = locationType;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public String getLocationId() { return locationId; }
    public void setLocationId(String locationId) { this.locationId = locationId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getLocationType() { return locationType; }
    public void setLocationType(String locationType) { this.locationType = locationType; }

    public double getXCoord() { return xCoord; }
    public void setXCoord(double xCoord) { this.xCoord = xCoord; }

    public double getYCoord() { return yCoord; }
    public void setYCoord(double yCoord) { this.yCoord = yCoord; }

    @Override
    public String toString() {
        return "Location{" + locationId + ", " + name + " [" + area + "]" + "}";
    }
}
