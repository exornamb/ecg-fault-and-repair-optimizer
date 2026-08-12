package controller;

import dao.FaultDAO;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Fault;
import service.FaultService;

import java.io.IOException;
import java.util.Optional;

public class FaultManagementController {

    // =========================
    // TABLE
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
    private ComboBox<String> priorityFilter;

    @FXML
    private ComboBox<String> statusFilter;

    @FXML
    private Label faultCountLabel;

    // =========================
    // FILTERED DATA
    // =========================

    private FilteredList<Fault> filteredFaults;

    // =========================
    // INITIALIZE
    // =========================

    @FXML
    public void initialize() {

        setupTable();

        setupFilters();

        setupSearchAndFiltering();

        updateFaultCount();

        playEntranceAnimations();
    }


    // =========================
    // ENTRANCE ANIMATIONS
    // =========================

    private void playEntranceAnimations() {

        // Search and filter controls
        animateNode(searchField, 0);
        animateNode(priorityFilter, 70);
        animateNode(statusFilter, 120);

        // Table
        animateNode(faultTable, 220);

        // Fault count
        animateNode(faultCountLabel, 300);
    }


    private void animateNode(Node node, int delay) {

        if (node == null) {
            return;
        }

        node.setOpacity(0);
        node.setTranslateY(15);

        javafx.animation.PauseTransition pause =
                new javafx.animation.PauseTransition(
                        Duration.millis(delay)
                );

        pause.setOnFinished(event -> {

            FadeTransition fade =
                    new FadeTransition(
                            Duration.millis(400),
                            node
                    );

            fade.setFromValue(0);
            fade.setToValue(1);

            TranslateTransition slide =
                    new TranslateTransition(
                            Duration.millis(400),
                            node
                    );

            slide.setFromY(15);
            slide.setToY(0);

            ParallelTransition animation =
                    new ParallelTransition(
                            fade,
                            slide
                    );

            animation.play();
        });

        pause.play();
    }


    // =========================
    // TABLE SETUP
    // =========================

    private void setupTable() {

        faultIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("faultId"));

        areaColumn.setCellValueFactory(
                new PropertyValueFactory<>("area"));

        categoryColumn.setCellValueFactory(
                new PropertyValueFactory<>("category"));

        priorityColumn.setCellValueFactory(
                new PropertyValueFactory<>("priorityText"));

        crewColumn.setCellValueFactory(
                new PropertyValueFactory<>("crew"));

        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("status"));

        setupStatusColumn();

        setupPriorityColumn();
    }


    // =========================
    // FILTER OPTIONS
    // =========================

    private void setupFilters() {

        priorityFilter.getItems().addAll(
                "All Priorities",
                "Critical",
                "High",
                "Medium",
                "Low",
                "Very Low"
        );

        statusFilter.getItems().addAll(
                "All Statuses",
                "OPEN",
                "ASSIGNED",
                "RESOLVED"
        );

        priorityFilter.setValue(
                "All Priorities");

        statusFilter.setValue(
                "All Statuses");
    }


    // =========================
    // SEARCH & FILTERING
    // =========================

    private void setupSearchAndFiltering() {

        filteredFaults =
                new FilteredList<>(
                        FaultService.getFaults(),
                        fault -> true
                );

        faultTable.setItems(
                filteredFaults);

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) ->
                        applyFilters()
        );

        priorityFilter.valueProperty().addListener(
                (observable, oldValue, newValue) ->
                        applyFilters()
        );

        statusFilter.valueProperty().addListener(
                (observable, oldValue, newValue) ->
                        applyFilters()
        );

        FaultService.getFaults().addListener(
                (ListChangeListener<Fault>) change ->
                        updateFaultCount()
        );
    }


    // =========================
    // APPLY FILTERS
    // =========================

    private void applyFilters() {

        String search =
                searchField.getText() == null
                        ? ""
                        : searchField.getText()
                        .trim()
                        .toLowerCase();

        String selectedPriority =
                priorityFilter.getValue();

        String selectedStatus =
                statusFilter.getValue();

        filteredFaults.setPredicate(fault -> {

            boolean matchesSearch =

                    search.isEmpty()

                            || containsIgnoreCase(
                            fault.getFaultId(),
                            search)

                            || containsIgnoreCase(
                            fault.getArea(),
                            search)

                            || containsIgnoreCase(
                            fault.getCategory(),
                            search)

                            || containsIgnoreCase(
                            fault.getPriorityText(),
                            search)

                            || containsIgnoreCase(
                            fault.getCrew(),
                            search)

                            || containsIgnoreCase(
                            fault.getStatus(),
                            search);

            boolean matchesPriority =

                    selectedPriority == null

                            || selectedPriority.equals(
                            "All Priorities")

                            || selectedPriority.equalsIgnoreCase(
                            fault.getPriorityText());

            boolean matchesStatus =

                    selectedStatus == null

                            || selectedStatus.equals(
                            "All Statuses")

                            || selectedStatus.equalsIgnoreCase(
                            fault.getStatus());

            return matchesSearch
                    && matchesPriority
                    && matchesStatus;
        });

        updateFaultCount();
    }


    // =========================
    // SEARCH HELPER
    // =========================

    private boolean containsIgnoreCase(
            String value,
            String search) {

        return value != null
                && value.toLowerCase().contains(search);
    }


    // =========================
    // FAULT COUNT
    // =========================

    private void updateFaultCount() {

        if (filteredFaults == null) {
            return;
        }

        int count = filteredFaults.size();

        if (count == 1) {

            faultCountLabel.setText(
                    "1 fault");

        } else {

            faultCountLabel.setText(
                    count + " faults");
        }
    }


    // =========================
    // ADD FAULT
    // =========================

    @FXML
    private void addFault(ActionEvent event) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/fxml/views/add-fault.fxml")
                    );

            Parent root = loader.load();

            AddFaultController controller =
                    loader.getController();

            controller.setController(this);

            Stage stage = new Stage();

            stage.initModality(
                    Modality.APPLICATION_MODAL);

            stage.setTitle(
                    "Add New Fault");

            stage.setScene(
                    new Scene(root));

            stage.setResizable(false);

            stage.showAndWait();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    // =========================
    // EDIT FAULT
    // =========================

    @FXML
    private void editFault() {

        Fault selectedFault =
                faultTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedFault == null) {

            showWarning(
                    "No Fault Selected",
                    "Please select a fault to edit."
            );

            return;
        }

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/fxml/views/edit-fault.fxml")
                    );

            Parent root = loader.load();

            EditFaultController controller =
                    loader.getController();

            controller.setController(this);

            controller.setFault(
                    selectedFault);

            Stage stage = new Stage();

            stage.initModality(
                    Modality.APPLICATION_MODAL);

            stage.setTitle(
                    "Edit Fault");

            stage.setScene(
                    new Scene(root));

            stage.showAndWait();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    // =========================
    // DELETE FAULT
    // =========================

    @FXML
    private void deleteFault(ActionEvent event) {

        Fault selectedFault =
                faultTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedFault == null) {

            showWarning(
                    "No Fault Selected",
                    "Please select a fault to delete."
            );

            return;
        }

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Delete Fault"
        );

        confirmation.setHeaderText(
                "Delete " +
                        selectedFault.getFaultId() +
                        "?"
        );

        confirmation.setContentText(
                "Are you sure you want to delete this fault?"
        );

        Optional<ButtonType> result =
                confirmation.showAndWait();

        if (result.isPresent()
                && result.get() == ButtonType.OK) {

            FaultDAO dao =
                    new FaultDAO();

            dao.deleteFault(
                    selectedFault.getId()
            );

            FaultService.refresh();

            filteredFaults =
                    new FilteredList<>(
                            FaultService.getFaults(),
                            fault -> true
                    );

            faultTable.setItems(
                    filteredFaults
            );

            applyFilters();

            faultTable.refresh();

            updateFaultCount();
        }
    }


    // =========================
    // REFRESH TABLE
    // =========================

    public void refreshTable() {

        FaultService.refresh();

        filteredFaults =
                new FilteredList<>(
                        FaultService.getFaults(),
                        fault -> true
                );

        faultTable.setItems(
                filteredFaults
        );

        applyFilters();

        faultTable.refresh();

        updateFaultCount();
    }


    // =========================
    // WARNING DIALOG
    // =========================

    private void showWarning(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.WARNING
                );

        alert.setTitle(
                title
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }

    private void setupStatusColumn() {

        statusColumn.setCellFactory(column ->
                new TableCell<Fault, String>() {

                    private final Label badge = new Label();

                    {
                        badge.setAlignment(Pos.CENTER);
                        badge.setMinWidth(85);
                        badge.setPadding(
                                new javafx.geometry.Insets(5, 12, 5, 12)
                        );
                        badge.setStyle(
                                "-fx-background-radius: 20;" +
                                        "-fx-font-weight: bold;"
                        );
                    }

                    @Override
                    protected void updateItem(
                            String status,
                            boolean empty) {

                        super.updateItem(status, empty);

                        if (empty || status == null) {

                            setGraphic(null);
                            setText(null);

                            return;
                        }

                        badge.setText(status);

                        switch (status.toUpperCase()) {

                            case "OPEN":

                                badge.setStyle(
                                        "-fx-background-color:#FEF3C7;" +
                                                "-fx-text-fill:#92400E;" +
                                                "-fx-background-radius:20;" +
                                                "-fx-font-weight:bold;"
                                );

                                break;

                            case "ASSIGNED":

                                badge.setStyle(
                                        "-fx-background-color:#DBEAFE;" +
                                                "-fx-text-fill:#1D4ED8;" +
                                                "-fx-background-radius:20;" +
                                                "-fx-font-weight:bold;"
                                );

                                break;

                            case "RESOLVED":

                                badge.setStyle(
                                        "-fx-background-color:#DCFCE7;" +
                                                "-fx-text-fill:#166534;" +
                                                "-fx-background-radius:20;" +
                                                "-fx-font-weight:bold;"
                                );

                                break;

                            default:

                                badge.setStyle(
                                        "-fx-background-color:#F1F5F9;" +
                                                "-fx-text-fill:#475569;" +
                                                "-fx-background-radius:20;" +
                                                "-fx-font-weight:bold;"
                                );
                        }

                        setGraphic(badge);
                        setText(null);
                        setAlignment(Pos.CENTER);
                    }
                }
        );
    }

    private void setupPriorityColumn() {

        priorityColumn.setCellFactory(column ->
                new TableCell<Fault, String>() {

                    private final Label badge = new Label();

                    {
                        badge.setAlignment(Pos.CENTER);
                        badge.setMinWidth(80);
                        badge.setPadding(
                                new javafx.geometry.Insets(5, 12, 5, 12)
                        );
                    }

                    @Override
                    protected void updateItem(
                            String priority,
                            boolean empty) {

                        super.updateItem(priority, empty);

                        if (empty || priority == null) {

                            setGraphic(null);
                            setText(null);

                            return;
                        }

                        badge.setText(priority);

                        switch (priority.toUpperCase()) {

                            case "CRITICAL":

                                badge.setStyle(
                                        "-fx-background-color:#FEE2E2;" +
                                                "-fx-text-fill:#B91C1C;" +
                                                "-fx-background-radius:20;" +
                                                "-fx-font-weight:bold;"
                                );

                                break;

                            case "HIGH":

                                badge.setStyle(
                                        "-fx-background-color:#FFEDD5;" +
                                                "-fx-text-fill:#C2410C;" +
                                                "-fx-background-radius:20;" +
                                                "-fx-font-weight:bold;"
                                );

                                break;

                            case "MEDIUM":

                                badge.setStyle(
                                        "-fx-background-color:#FEF3C7;" +
                                                "-fx-text-fill:#A16207;" +
                                                "-fx-background-radius:20;" +
                                                "-fx-font-weight:bold;"
                                );

                                break;

                            case "LOW":

                                badge.setStyle(
                                        "-fx-background-color:#DBEAFE;" +
                                                "-fx-text-fill:#1D4ED8;" +
                                                "-fx-background-radius:20;" +
                                                "-fx-font-weight:bold;"
                                );

                                break;

                            case "VERY LOW":

                                badge.setStyle(
                                        "-fx-background-color:#F1F5F9;" +
                                                "-fx-text-fill:#64748B;" +
                                                "-fx-background-radius:20;" +
                                                "-fx-font-weight:bold;"
                                );

                                break;

                            default:

                                badge.setStyle(
                                        "-fx-background-color:#F1F5F9;" +
                                                "-fx-text-fill:#475569;" +
                                                "-fx-background-radius:20;" +
                                                "-fx-font-weight:bold;"
                                );
                        }

                        setGraphic(badge);
                        setText(null);
                        setAlignment(Pos.CENTER);
                    }
                }
        );
    }
}