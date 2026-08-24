package com.g15.dsa.controller;

import com.g15.dsa.dao.FaultDAO;
import com.g15.dsa.dao.ResourceDAO;
import com.g15.dsa.database.TeamParameters;
import com.g15.dsa.model.Fault;
import com.g15.dsa.service.FaultService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DispatchController {

    // ── Summary KPI labels ───────────────────────────────────────────────────
    @FXML private Label pendingCountLabel;
    @FXML private Label assignedCountLabel;
    @FXML private Label availableCrewCountLabel;
    @FXML private Label queueCountLabel;

    // ── Dispatch Queue Table ─────────────────────────────────────────────────
    @FXML private TableView<Fault> faultTable;
    @FXML private TableColumn<Fault, String> faultIdColumn;
    @FXML private TableColumn<Fault, String> areaColumn;
    @FXML private TableColumn<Fault, String> priorityColumn;
    @FXML private TableColumn<Fault, String> crewColumn;
    @FXML private TableColumn<Fault, String> statusColumn;

    // ── Crew Assignment Panel ────────────────────────────────────────────────
    @FXML private Label selectedFaultLabel;
    @FXML private Label selectedAreaLabel;
    @FXML private Label selectedPriorityLabel;
    @FXML private Label recommendedCrewLabel;
    @FXML private Label recommendationReasonLabel;
    @FXML private ComboBox<String> crewBox;
    @FXML private Button assignCrewButton;

    private final ResourceDAO resourceDAO = new ResourceDAO();
    private final FaultDAO faultDAO = new FaultDAO();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadQueueData();
        loadCrews();
        setupSelectionListener();
        updateKpis();
    }

    private void setupTableColumns() {
        if (faultIdColumn != null) faultIdColumn.setCellValueFactory(new PropertyValueFactory<>("faultId"));
        if (areaColumn != null) areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
        if (priorityColumn != null) priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priorityText"));
        if (crewColumn != null) crewColumn.setCellValueFactory(new PropertyValueFactory<>("crew"));
        if (statusColumn != null) statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadQueueData() {
        if (faultTable != null) {
            ObservableList<Fault> queue = FXCollections.observableArrayList();
            for (Fault f : FaultService.getFaults()) {
                if (!"RESOLVED".equalsIgnoreCase(f.getStatus())) {
                    queue.add(f);
                }
            }
            faultTable.setItems(queue);
            if (queueCountLabel != null) queueCountLabel.setText(queue.size() + " faults");
        }
    }

    private void loadCrews() {
        if (crewBox != null) {
            crewBox.setItems(FXCollections.observableArrayList(resourceDAO.getAllCrews()));
        }
    }

    private void setupSelectionListener() {
        if (faultTable != null) {
            faultTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {
                if (selected != null) {
                    if (selectedFaultLabel != null) selectedFaultLabel.setText(selected.getFaultId());
                    if (selectedAreaLabel != null) selectedAreaLabel.setText(selected.getArea());
                    if (selectedPriorityLabel != null) selectedPriorityLabel.setText(selected.getPriorityText());

                    String category = selected.getCategory() != null ? selected.getCategory() : "General";
                    String recommended = resourceDAO.getRecommendedCrew(category);
                    String reason = resourceDAO.getRecommendationReason(category);

                    if (recommendedCrewLabel != null) recommendedCrewLabel.setText(recommended);
                    if (recommendationReasonLabel != null) recommendationReasonLabel.setText(reason);
                    if (crewBox != null) crewBox.setValue(recommended);
                } else {
                    clearSelection();
                }
            });
        }
    }

    private void clearSelection() {
        if (selectedFaultLabel != null) selectedFaultLabel.setText("No fault selected");
        if (selectedAreaLabel != null) selectedAreaLabel.setText("-");
        if (selectedPriorityLabel != null) selectedPriorityLabel.setText("-");
        if (recommendedCrewLabel != null) recommendedCrewLabel.setText("No recommendation");
        if (recommendationReasonLabel != null) recommendationReasonLabel.setText("Select a fault to receive a crew recommendation.");
        if (crewBox != null) crewBox.setValue(null);
    }

    private void updateKpis() {
        int pending = 0;
        int assigned = 0;

        for (Fault f : FaultService.getFaults()) {
            if ("OPEN".equalsIgnoreCase(f.getStatus()) || "PENDING".equalsIgnoreCase(f.getStatus())) {
                pending++;
            } else if ("ASSIGNED".equalsIgnoreCase(f.getStatus()) || (f.getCrew() != null && !f.getCrew().isEmpty() && !"Unassigned".equalsIgnoreCase(f.getCrew()) && !"RESOLVED".equalsIgnoreCase(f.getStatus()))) {
                assigned++;
            }
        }

        if (pendingCountLabel != null) pendingCountLabel.setText(String.valueOf(pending));
        if (assignedCountLabel != null) assignedCountLabel.setText(String.valueOf(assigned));
        if (availableCrewCountLabel != null) availableCrewCountLabel.setText(String.valueOf(resourceDAO.getAvailableCrewCount()));
    }

    @FXML
    public void assignCrew() {
        Fault selected = (faultTable != null) ? faultTable.getSelectionModel().getSelectedItem() : null;
        if (selected == null) {
            showAlert("No Selection", "Please select a fault from the dispatch queue.");
            return;
        }

        String crew = (crewBox != null) ? crewBox.getValue() : null;
        if (crew == null || crew.trim().isEmpty()) {
            showAlert("No Crew Selected", "Please select a response crew to assign.");
            return;
        }

        selected.setCrew(crew);
        selected.setStatus("ASSIGNED");
        faultDAO.updateFault(selected);
        resourceDAO.setCrewBusy(crew);

        FaultService.refresh();
        loadQueueData();
        updateKpis();

        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("Dispatch Confirmed");
        success.setHeaderText("Crew Dispatched Successfully");
        success.setContentText(crew + " has been assigned to " + selected.getFaultId() + " (" + selected.getArea() + ").");
        success.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Calculates the composite dispatch score using Michelle's index-derived parameters.
     * Score = (Urgency * URGENCY_WEIGHT) / (Road Distance * ROAD_PENALTY)
     */
    public static double calculateDispatchScore(double roadDistance, int urgency) {
        double effectiveDist = Math.max(0.1, calculatePenalizedDistance(roadDistance));
        return (urgency * TeamParameters.URGENCY_WEIGHT) / effectiveDist;
    }

    /**
     * Calculates effective travel distance penalized by road conditions.
     */
    public static double calculatePenalizedDistance(double roadDistance) {
        return roadDistance * TeamParameters.ROAD_PENALTY;
    }
}
