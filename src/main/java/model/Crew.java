package model;

public class Crew {

    private final String name;
    private final String type;
    private final String availability;
    private final int capacity;

    public Crew(String name,
                String type,
                String availability,
                int capacity) {

        this.name = name;
        this.type = type;
        this.availability = availability;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getAvailability() {
        return availability;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return name;
    }
}
