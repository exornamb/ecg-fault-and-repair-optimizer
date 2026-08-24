package com.g15.dsa.controller;

import com.g15.dsa.model.Fault;
import com.g15.dsa.service.FaultService;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class DatabaseController {

    @FXML
    private Label totalRecordsLabel;

    @FXML
    private Label pendingRecordsLabel;

    @FXML
    private Label assignedRecordsLabel;

    @FXML
    private Label resolvedRecordsLabel;

    @FXML
    private Label recordCountLabel;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Fault> faultTable;

    @FXML
    private TableColumn<Fault, String> faultIdColumn;

    @FXML
    private TableColumn<Fault, String> areaColumn;

    @FXML
    private TableColumn<Fault, String> priorityColumn;

    @FXML
    private TableColumn<Fault, String> crewColumn;

    @FXML
    private TableColumn<Fault, String> statusColumn;

    private FilteredList<Fault> filteredFaults;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadData();
        setupSearch();
    }

    private void setupTableColumns() {
        if (faultIdColumn != null) faultIdColumn.setCellValueFactory(new PropertyValueFactory<>("faultId"));
        if (areaColumn != null) areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
        if (priorityColumn != null) priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priorityText"));
        if (crewColumn != null) crewColumn.setCellValueFactory(new PropertyValueFactory<>("crew"));
        if (statusColumn != null) statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadData() {
        filteredFaults = new FilteredList<>(FaultService.getFaults(), p -> true);
        if (faultTable != null) {
            faultTable.setItems(filteredFaults);
        }
        updateCounts();
    }

    private void setupSearch() {
        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldVal, newVal) -> {
                String query = (newVal != null) ? newVal.trim().toLowerCase() : "";
                filteredFaults.setPredicate(f -> {
                    if (f == null) return false;
                    if (query.isEmpty()) return true;
                    return (f.getFaultId() != null && f.getFaultId().toLowerCase().contains(query))
                            || (f.getArea() != null && f.getArea().toLowerCase().contains(query))
                            || (f.getCrew() != null && f.getCrew().toLowerCase().contains(query))
                            || (f.getStatus() != null && f.getStatus().toLowerCase().contains(query))
                            || (f.getCategory() != null && f.getCategory().toLowerCase().contains(query));
                });
                if (recordCountLabel != null) {
                    recordCountLabel.setText(filteredFaults.size() + " records");
                }
            });
        }
    }

    private void updateCounts() {
        int total = FaultService.getFaults().size();
        int pending = 0;
        int assigned = 0;
        int resolved = 0;

        for (Fault f : FaultService.getFaults()) {
            String s = f.getStatus() != null ? f.getStatus().toUpperCase() : "";
            if (s.contains("RESOLVED")) {
                resolved++;
            } else if (s.contains("ASSIGNED") || (f.getCrew() != null && !f.getCrew().isEmpty() && !"Unassigned".equalsIgnoreCase(f.getCrew()))) {
                assigned++;
            } else {
                pending++;
            }
        }

        if (totalRecordsLabel != null) totalRecordsLabel.setText(String.valueOf(total));
        if (pendingRecordsLabel != null) pendingRecordsLabel.setText(String.valueOf(pending));
        if (assignedRecordsLabel != null) assignedRecordsLabel.setText(String.valueOf(assigned));
        if (resolvedRecordsLabel != null) resolvedRecordsLabel.setText(String.valueOf(resolved));
        if (recordCountLabel != null) recordCountLabel.setText(total + " records");
    }

    @FXML
    public void refreshDatabase() {
        FaultService.refresh();
        loadData();
    }
}
