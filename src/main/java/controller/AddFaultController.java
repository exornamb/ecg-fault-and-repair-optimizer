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

public class AddFaultController {

    private FaultManagementController controller;

    // Null = Add Mode
    // Not null = Edit Mode
    private Fault editingFault;

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
                resourceDAO.getAvailableCrews()
        );

        // Status
        statusBox.getItems().addAll(
                "OPEN",
                "ASSIGNED",
                "RESOLVED"
        );
    }

    // =========================
    // SET PARENT CONTROLLER
    // =========================

    public void setController(
            FaultManagementController controller) {

        this.controller = controller;
    }

    // =========================
    // EDIT MODE
    // =========================

    public void setFaultForEditing(
            Fault fault) {

        editingFault = fault;

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
    private void saveFault() {

        if (!validateForm()) {
            return;
        }

        FaultDAO dao =
                new FaultDAO();

        int urgency =
                convertUrgency(
                        urgencyBox.getValue());

        if (editingFault == null) {

            Fault newFault =
                    new Fault(

                            0,

                            "",

                            locationBox.getValue(),

                            categoryBox.getValue(),

                            urgency,

                            crewBox.getValue(),

                            statusBox.getValue()
                    );

            dao.insertFault(
                    newFault);

        } else {

            editingFault.setArea(
                    locationBox.getValue());

            editingFault.setCategory(
                    categoryBox.getValue());

            editingFault.setUrgency(
                    urgency);

            editingFault.setCrew(
                    crewBox.getValue());

            editingFault.setStatus(
                    statusBox.getValue());

            dao.updateFault(
                    editingFault);
        }

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
    // CLOSE WINDOW
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