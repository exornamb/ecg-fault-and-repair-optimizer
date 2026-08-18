package com.g15.dsa.controller;

import com.g15.dsa.dao.FaultDAO;
import com.g15.dsa.dao.ResourceDAO;
import com.g15.dsa.model.Fault;
import com.g15.dsa.service.FaultService;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Optional;

public class FaultManagementController {

    // =========================
    // TABLE & COLUMNS
    // =========================

    @FXML
    private TableView<Fault> faultTable;

    @FXML
    private TableColumn<Fault, String> faultIdColumn;

    @FXML
    private TableColumn<Fault, String> areaColumn;

    @FXML
    private TableColumn<Fault, String> categoryColumn;

    @FXML
    private TableColumn<Fault, String> priorityColumn;

    @FXML
    private TableColumn<Fault, String> crewColumn;

    @FXML
    private TableColumn<Fault, String> statusColumn;

    // =========================
    // SEARCH & FILTERS
    // =========================

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> categoryFilter;

    @FXML
    private ComboBox<String> statusFilter;

    @FXML
    private ComboBox<String> priorityFilter;

    @FXML
    private Label totalFaultsCountLabel;

    private FilteredList<Fault> filteredFaults;

    // =========================
    // INITIALIZE
    // =========================

    @FXML
    public void initialize() {
        setupTableColumns();
        setupFilterOptions();
        setupFilteredList();
        setupSearchAndFilterListeners();
        playEntryAnimations();
    }

    private void setupTableColumns() {
        if (faultIdColumn != null) faultIdColumn.setCellValueFactory(new PropertyValueFactory<>("faultId"));
        if (areaColumn != null) areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
        if (categoryColumn != null) categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        if (priorityColumn != null) priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priorityText"));
        if (crewColumn != null) crewColumn.setCellValueFactory(new PropertyValueFactory<>("crew"));
        if (statusColumn != null) statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        setupPriorityColumn();
        setupStatusColumn();
    }

    private void setupFilterOptions() {
        if (categoryFilter != null) {
            categoryFilter.setItems(FXCollections.observableArrayList(
                    "All Categories", "Transformer Failure", "Meter Fault", "Cable Burst", "Fallen Conductor"
            ));
            categoryFilter.setValue("All Categories");
        }

        if (statusFilter != null) {
            statusFilter.setItems(FXCollections.observableArrayList(
                    "All Statuses", "OPEN", "ASSIGNED", "RESOLVED"
            ));
            statusFilter.setValue("All Statuses");
        }

        if (priorityFilter != null) {
            priorityFilter.setItems(FXCollections.observableArrayList(
                    "All Priorities", "Critical", "High", "Medium", "Low", "Very Low"
            ));
            priorityFilter.setValue("All Priorities");
        }
    }

    private void setupFilteredList() {
        filteredFaults = new FilteredList<>(FaultService.getFaults(), p -> true);
        if (faultTable != null) {
            faultTable.setItems(filteredFaults);
        }
        updateFaultCount();
    }

    private void setupSearchAndFilterListeners() {
        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        }
        if (categoryFilter != null) {
            categoryFilter.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        }
        if (statusFilter != null) {
            statusFilter.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        }
        if (priorityFilter != null) {
            priorityFilter.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        }
    }

    public void applyFilters() {
        if (filteredFaults == null) return;

        String searchText = searchField != null && searchField.getText() != null ? searchField.getText().trim().toLowerCase() : "";
        String cat = categoryFilter != null ? categoryFilter.getValue() : null;
        String stat = statusFilter != null ? statusFilter.getValue() : null;
        String prio = priorityFilter != null ? priorityFilter.getValue() : null;

        filteredFaults.setPredicate(fault -> {
            if (fault == null) return false;

            // Search text match across multiple fields
            boolean matchesSearch = searchText.isEmpty()
                    || (fault.getFaultId() != null && fault.getFaultId().toLowerCase().contains(searchText))
                    || (fault.getArea() != null && fault.getArea().toLowerCase().contains(searchText))
                    || (fault.getCategory() != null && fault.getCategory().toLowerCase().contains(searchText))
                    || (fault.getCrew() != null && fault.getCrew().toLowerCase().contains(searchText))
                    || (fault.getStatus() != null && fault.getStatus().toLowerCase().contains(searchText));

            if (!matchesSearch) return false;

            // Category match
            if (cat != null && !cat.equals("All Categories") && !cat.equalsIgnoreCase(fault.getCategory())) {
                return false;
            }

            // Status match
            if (stat != null && !stat.equals("All Statuses") && !stat.equalsIgnoreCase(fault.getStatus())) {
                return false;
            }

            // Priority match
            if (prio != null && !prio.equals("All Priorities") && !prio.equalsIgnoreCase(fault.getPriorityText())) {
                return false;
            }

            return true;
        });

        updateFaultCount();
    }

    public void refreshTable() {
        FaultService.refresh();
        filteredFaults = new FilteredList<>(FaultService.getFaults(), p -> true);
        if (faultTable != null) {
            faultTable.setItems(filteredFaults);
            faultTable.refresh();
        }
        applyFilters();
        updateFaultCount();
    }

    private void updateFaultCount() {
        if (totalFaultsCountLabel != null && filteredFaults != null) {
            totalFaultsCountLabel.setText("Total Records: " + filteredFaults.size());
        }
    }

    // =========================
    // CRUD ACTIONS
    // =========================

    @FXML
    public void openAddFaultModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/views/add-fault.fxml"));
            Parent root = loader.load();

            AddFaultController addController = loader.getController();
            addController.setController(this);

            Stage stage = new Stage();
            stage.setTitle("Report New Fault / Outage");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            showWarning("Dialog Error", "Could not open Add Fault window: " + e.getMessage());
        }
    }

    @FXML
    public void openEditFaultModal() {
        Fault selectedFault = faultTable != null ? faultTable.getSelectionModel().getSelectedItem() : null;
        if (selectedFault == null) {
            showWarning("Selection Required", "Please select a fault from the table to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/views/edit-fault.fxml"));
            Parent root = loader.load();

            EditFaultController editController = loader.getController();
            editController.setController(this);
            editController.setFault(selectedFault);

            Stage stage = new Stage();
            stage.setTitle("Edit Fault — " + selectedFault.getFaultId());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            showWarning("Dialog Error", "Could not open Edit Fault window: " + e.getMessage());
        }
    }

    @FXML
    public void handleDeleteFault() {
        Fault selectedFault = faultTable != null ? faultTable.getSelectionModel().getSelectedItem() : null;
        if (selectedFault == null) {
            showWarning("Selection Required", "Please select a fault to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete Fault " + selectedFault.getFaultId() + "?");
        confirm.setContentText("Are you sure you want to permanently remove this fault ticket?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            FaultDAO dao = new FaultDAO();
            dao.deleteFault(selectedFault.getId());
            refreshTable();
        }
    }

    @FXML
    public void handleAutoDispatch() {
        Fault selectedFault = faultTable != null ? faultTable.getSelectionModel().getSelectedItem() : null;
        if (selectedFault == null) {
            showWarning("Selection Required", "Please select a fault ticket for automatic crew dispatch.");
            return;
        }

        ResourceDAO resourceDAO = new ResourceDAO();
        String bestCrew = resourceDAO.getRecommendedCrew(selectedFault.getCategory());
        if (bestCrew == null || bestCrew.isEmpty()) {
            bestCrew = "Alpha Rapid Response";
        }

        selectedFault.setCrew(bestCrew);
        selectedFault.setStatus("ASSIGNED");

        FaultDAO dao = new FaultDAO();
        dao.updateFault(selectedFault);
        resourceDAO.setCrewBusy(bestCrew);

        refreshTable();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dispatch Successful");
        alert.setHeaderText("Crew Dispatched!");
        alert.setContentText("Assigned " + bestCrew + " to " + selectedFault.getFaultId() + " (" + selectedFault.getArea() + ").");
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // =========================
    // CELL FORMATTING & BADGES
    // =========================

    private void setupStatusColumn() {
        if (statusColumn == null) return;
        statusColumn.setCellFactory(column -> new TableCell<Fault, String>() {
            private final Label badge = new Label();
            {
                badge.setAlignment(Pos.CENTER);
                badge.setMinWidth(85);
                badge.setPadding(new javafx.geometry.Insets(5, 12, 5, 12));
                badge.setStyle("-fx-background-radius: 20; -fx-font-weight: bold;");
            }

            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                badge.setText(status.toUpperCase());
                switch (status.toUpperCase()) {
                    case "OPEN":
                        badge.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #92400E; -fx-background-radius: 20; -fx-font-weight: bold;");
                        break;
                    case "ASSIGNED":
                        badge.setStyle("-fx-background-color: #DBEAFE; -fx-text-fill: #1D4ED8; -fx-background-radius: 20; -fx-font-weight: bold;");
                        break;
                    case "RESOLVED":
                        badge.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #166534; -fx-background-radius: 20; -fx-font-weight: bold;");
                        break;
                    default:
                        badge.setStyle("-fx-background-color: #F1F5F9; -fx-text-fill: #475569; -fx-background-radius: 20; -fx-font-weight: bold;");
                }
                setGraphic(badge);
                setText(null);
                setAlignment(Pos.CENTER);
            }
        });
    }

    private void setupPriorityColumn() {
        if (priorityColumn == null) return;
        priorityColumn.setCellFactory(column -> new TableCell<Fault, String>() {
            private final Label badge = new Label();
            {
                badge.setAlignment(Pos.CENTER);
                badge.setMinWidth(80);
                badge.setPadding(new javafx.geometry.Insets(5, 12, 5, 12));
            }

            @Override
            protected void updateItem(String priority, boolean empty) {
                super.updateItem(priority, empty);
                if (empty || priority == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                badge.setText(priority);
                switch (priority.toUpperCase()) {
                    case "CRITICAL":
                        badge.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #B91C1C; -fx-background-radius: 20; -fx-font-weight: bold;");
                        break;
                    case "HIGH":
                        badge.setStyle("-fx-background-color: #FFEDD5; -fx-text-fill: #C2410C; -fx-background-radius: 20; -fx-font-weight: bold;");
                        break;
                    case "MEDIUM":
                        badge.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #A16207; -fx-background-radius: 20; -fx-font-weight: bold;");
                        break;
                    case "LOW":
                        badge.setStyle("-fx-background-color: #DBEAFE; -fx-text-fill: #1D4ED8; -fx-background-radius: 20; -fx-font-weight: bold;");
                        break;
                    default:
                        badge.setStyle("-fx-background-color: #F1F5F9; -fx-text-fill: #64748B; -fx-background-radius: 20; -fx-font-weight: bold;");
                }
                setGraphic(badge);
                setText(null);
                setAlignment(Pos.CENTER);
            }
        });
    }

    private void playEntryAnimations() {
        if (faultTable != null) {
            faultTable.setOpacity(0);
            faultTable.setTranslateY(15);

            FadeTransition fade = new FadeTransition(Duration.millis(400), faultTable);
            fade.setFromValue(0);
            fade.setToValue(1);

            TranslateTransition slide = new TranslateTransition(Duration.millis(400), faultTable);
            slide.setFromY(15);
            slide.setToY(0);

            ParallelTransition pt = new ParallelTransition(fade, slide);
            pt.play();
        }
    }
}
