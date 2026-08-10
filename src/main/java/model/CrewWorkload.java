package model;

public class CrewWorkload {

    private final String crewName;

    private final int assignedFaults;

    private final String availability;

    private final int capacity;

    public CrewWorkload(
            String crewName,
            int assignedFaults,
            String availability,
            int capacity) {

        this.crewName = crewName;
        this.assignedFaults = assignedFaults;
        this.availability = availability;
        this.capacity = capacity;
    }

    public String getCrewName() {
        return crewName;
    }

    public int getAssignedFaults() {
        return assignedFaults;
    }

    public String getAvailability() {
        return availability;
    }

    public int getCapacity() {
        return capacity;
    }
}
