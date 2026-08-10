package model;

public class Fault {

    private int id;
    private String faultId;
    private String area;
    private String category;
    private int urgency;
    private String crew;
    private String status;

    public Fault(
            int id,
            String faultId,
            String area,
            String category,
            int urgency,
            String crew,
            String status) {

        this.id = id;
        this.faultId = faultId;
        this.area = area;
        this.category = category;
        this.urgency = urgency;
        this.crew = crew;
        this.status = status;
    }

    // =========================
    // GETTERS
    // =========================

    public int getId() {
        return id;
    }

    public String getFaultId() {
        return faultId;
    }

    public String getArea() {
        return area;
    }

    public String getCategory() {
        return category;
    }

    public int getUrgency() {
        return urgency;
    }

    public String getCrew() {
        return crew;
    }

    public String getStatus() {
        return status;
    }

    // =========================
    // SETTERS
    // =========================

    public void setArea(String area) {
        this.area = area;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }

    public void setCrew(String crew) {
        this.crew = crew;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // =========================
    // DISPLAY PRIORITY
    // =========================

    public String getPriorityText() {

        switch (urgency) {

            case 5:
                return "Critical";

            case 4:
                return "High";

            case 3:
                return "Medium";

            case 2:
                return "Low";

            default:
                return "Very Low";
        }
    }
}