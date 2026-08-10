package controller;

import dao.FaultDAO;
import dao.LocationDAO;
import dao.ResourceDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import model.Fault;
import service.FaultService;

public class EditFaultController {

    private Fault fault;

    private FaultManagementController controller;

    // =========================
    // FORM FIELDS
    // =========================

    @FXML
    private ComboBox<String> locationBox;

    @FXML
    private ComboBox<String> categoryBox;

    @FXML
    private ComboBox<String> urgencyBox;

    @FXML
    private ComboBox<String> crewBox;

    @FXML
    private ComboBox<String> statusBox;

    // =========================
    // INITIALIZE
    // =========================

    @FXML
    public void initialize() {

        // Locations
        LocationDAO locationDAO =
                new LocationDAO();

        locationBox.setItems(
                locationDAO.getAllLocations()
        );

        // Categories
        categoryBox.getItems().addAll(
                "Transformer Failure",
                "Meter Fault",
                "Cable Burst",
                "Fallen Conductor"
        );

        // Priority / Urgency
        urgencyBox.getItems().addAll(
                "Critical",
                "High",
                "Medium",
                "Low",
                "Very Low"
        );

        // Crews
        ResourceDAO resourceDAO =
                new ResourceDAO();

        crewBox.setItems(
                resourceDAO.getAllCrews()
        );

        // Status
        statusBox.getItems().addAll(
                "OPEN",
                "ASSIGNED",
                "RESOLVED"
        );
    }

    // =========================
    // RECEIVE CONTROLLER
    // =========================

    public void setController(
            FaultManagementController controller) {

        this.controller = controller;
    }

    // =========================
    // LOAD FAULT
    // =========================

    public void setFault(Fault fault) {

        this.fault = fault;

        locationBox.setValue(
                fault.getArea());

        categoryBox.setValue(
                fault.getCategory());

        urgencyBox.setValue(
                fault.getPriorityText());

        crewBox.setValue(
                fault.getCrew());

        statusBox.setValue(
                fault.getStatus());
    }

    // =========================
    // SAVE
    // =========================

    @FXML
    private void saveChanges() {

        if (!validateForm()) {
            return;
        }

        fault.setArea(
                locationBox.getValue());

        fault.setCategory(
                categoryBox.getValue());

        fault.setUrgency(
                convertUrgency(
                        urgencyBox.getValue()));

        fault.setCrew(
                crewBox.getValue());

        fault.setStatus(
                statusBox.getValue());

        if ("RESOLVED".equalsIgnoreCase(fault.getStatus())) {

            ResourceDAO resourceDAO =
                    new ResourceDAO();

            resourceDAO.setCrewAvailable(
                    fault.getCrew()
            );
        }

        FaultDAO dao =
                new FaultDAO();


        dao.updateFault(
                fault);

        FaultService.refresh();


        if (controller != null) {

            controller.refreshTable();
        }

        closeWindow();
    }

    // =========================
    // CONVERT URGENCY
    // =========================

    private int convertUrgency(
            String priority) {

        switch (priority) {

            case "Critical":
                return 5;

            case "High":
                return 4;

            case "Medium":
                return 3;

            case "Low":
                return 2;

            default:
                return 1;
        }
    }

    // =========================
    // VALIDATION
    // =========================

    private boolean validateForm() {

        if (locationBox.getValue() == null) {

            showWarning(
                    "Location Required",
                    "Please select a location.");

            return false;
        }

        if (categoryBox.getValue() == null) {

            showWarning(
                    "Category Required",
                    "Please select a fault category.");

            return false;
        }

        if (urgencyBox.getValue() == null) {

            showWarning(
                    "Priority Required",
                    "Please select a priority.");

            return false;
        }

        if (crewBox.getValue() == null) {

            showWarning(
                    "Crew Required",
                    "Please select a crew.");

            return false;
        }

        if (statusBox.getValue() == null) {

            showWarning(
                    "Status Required",
                    "Please select a status.");

            return false;
        }

        return true;
    }

    // =========================
    // WARNING
    // =========================

    private void showWarning(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.WARNING);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }

    // =========================
    // CLOSE
    // =========================

    @FXML
    private void closeWindow() {

        Stage stage =
                (Stage) locationBox
                        .getScene()
                        .getWindow();

        stage.close();
    }
}