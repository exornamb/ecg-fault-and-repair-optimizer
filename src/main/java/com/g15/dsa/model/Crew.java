package com.g15.dsa.model;

/**
 * Represents an ECG repair crew / emergency response unit assigned to faults.
 */
public class Crew {

    private String crewId;
    private String name;
    private String type;
    private String availability;
    private int capacity;
    private String baseLocationId;
    private int activeJobs;

    public Crew() {}

    public Crew(String crewId, String name, String type, String availability, int capacity, String baseLocationId) {
        this.crewId = crewId;
        this.name = name;
        this.type = type;
        this.availability = availability;
        this.capacity = capacity;
        this.baseLocationId = baseLocationId;
        this.activeJobs = 0;
    }

    public Crew(String name, String type, String availability, int capacity) {
        this("", name, type, availability, capacity, "");
    }

    public String getCrewId() { return crewId; }
    public void setCrewId(String crewId) { this.crewId = crewId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getBaseLocationId() { return baseLocationId; }
    public void setBaseLocationId(String baseLocationId) { this.baseLocationId = baseLocationId; }

    public int getActiveJobs() { return activeJobs; }
    public void setActiveJobs(int activeJobs) { this.activeJobs = activeJobs; }

    public boolean isAvailable() {
        return "Available".equalsIgnoreCase(availability) && activeJobs < capacity;
    }

    @Override
    public String toString() {
        return "Crew{" + name + " [" + type + ", " + availability + ", jobs=" + activeJobs + "/" + capacity + "]}";
    }
}
