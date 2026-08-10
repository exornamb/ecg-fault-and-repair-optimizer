package controller;
import javafx.concurrent.Task;
import model.Crew;
import dao.FaultDAO;
import dao.ResourceDAO;
import service.DispatchService;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import javafx.scene.control.Button;
import model.Fault;
import service.FaultService;


public class DispatchController {

    private final DispatchService dispatchService =
            new DispatchService();

    @FXML
    private Button assignCrewButton;

    @FXML
    private Label recommendedCrewLabel;

    @FXML
    private Label recommendationReasonLabel;


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
    private TableColumn<Fault, String> priorityColumn;

    @FXML
    private TableColumn<Fault, String> crewColumn;

    @FXML
    private TableColumn<Fault, String> statusColumn;


    // =========================
    // CREW SELECTION
    // =========================

    @FXML
    private ComboBox<String> crewBox;


    // =========================
    // SUMMARY LABELS
    // =========================

    @FXML
    private Label pendingCountLabel;

    @FXML
    private Label assignedCountLabel;

    @FXML
    private Label availableCrewCountLabel;

    @FXML
    private Label queueCountLabel;


    // =========================
    // SELECTED FAULT DETAILS
    // =========================

    @FXML
    private Label selectedFaultLabel;

    @FXML
    private Label selectedAreaLabel;

    @FXML
    private Label selectedPriorityLabel;


    // =========================
    // INITIALIZE
    // =========================

    @FXML
    public void initialize() {
        setupAssignButton();

        setupTable();

        setupCrews();

        setupFaultSelection();

        setupDataListener();

        updateSummary();

        setupCrewListener();

        playEntranceAnimations();
    }

    private void setupAssignButton() {

        assignCrewButton.setDisable(true);

        faultTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldFault, newFault) -> {
                    updateAssignButton();
                });

        crewBox.valueProperty()
                .addListener((obs, oldCrew, newCrew) -> {
                    updateAssignButton();
                });
    }

    private void updateAssignButton() {

        boolean faultSelected =
                faultTable.getSelectionModel()
                        .getSelectedItem() != null;

        boolean crewSelected =
                crewBox.getValue() != null;

        assignCrewButton.setDisable(
                !(faultSelected && crewSelected)
        );
    }


    // =========================
    // CREW LISTENER
    // =========================

    private void setupCrewListener() {

        crewBox.valueProperty().addListener(
                (obs, oldCrew, newCrew) -> {

                    Fault selectedFault =
                            faultTable
                                    .getSelectionModel()
                                    .getSelectedItem();

                    if (selectedFault != null) {
                        showRecommendation(selectedFault);
                    }
                }
        );
    }


    // =========================
    // ENTRANCE ANIMATIONS
    // =========================

    private void playEntranceAnimations() {

        // Summary information
        animateNode(
                pendingCountLabel,
                0
        );

        animateNode(
                assignedCountLabel,
                70
        );

        animateNode(
                availableCrewCountLabel,
                140
        );

        animateNode(
                queueCountLabel,
                210
        );


        // Selected fault information
        animateNode(
                selectedFaultLabel,
                280
        );

        animateNode(
                selectedAreaLabel,
                330
        );

        animateNode(
                selectedPriorityLabel,
                380
        );


        // Crew recommendation
        animateNode(
                recommendedCrewLabel,
                430
        );

        animateNode(
                recommendationReasonLabel,
                480
        );


        // Crew selector
        animateNode(
                crewBox,
                530
        );


        // Main dispatch table
        animateNode(
                faultTable,
                600
        );
    }


    // =========================
    // ANIMATION HELPER
    // =========================

    private void animateNode(
            Node node,
            int delay) {

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

        priorityColumn.setCellValueFactory(
                new PropertyValueFactory<>("priorityText"));

        crewColumn.setCellValueFactory(
                new PropertyValueFactory<>("crew"));

        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("status"));

        // Load the shared fault dataset
        faultTable.setItems(
                FXCollections.observableArrayList(
                        dispatchService.prioritizeFaults(
                                FaultService.getFaults()
                        )
                )
        );

        // Apply badge styling
        setupStatusColumn();
        setupPriorityColumn();
    }


    // =========================
    // CREW SETUP
    // =========================

    private void setupCrews() {

        ResourceDAO dao =
                new ResourceDAO();

        crewBox.setItems(
                dao.getAvailableCrews()
        );
    }


    // =========================
    // TABLE SELECTION
    // =========================

    private void setupFaultSelection() {

        faultTable
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable,
                         oldFault,
                         selectedFault) -> {

                            displaySelectedFault(
                                    selectedFault);
                        }
                );
    }


    // =========================
    // DISPLAY SELECTED FAULT
    // =========================

    private void displaySelectedFault(
            Fault fault) {

        if (fault == null) {

            selectedFaultLabel.setText(
                    "No fault selected");

            selectedAreaLabel.setText("-");

            selectedPriorityLabel.setText("-");

            crewBox.setValue(null);

            showRecommendation(null);

            return;
        }


        selectedFaultLabel.setText(
                fault.getFaultId());

        selectedAreaLabel.setText(
                fault.getArea());

        selectedPriorityLabel.setText(
                fault.getPriorityText());


        ResourceDAO dao =
                new ResourceDAO();


        String recommendedCrew =
                dao.getRecommendedCrew(
                        fault.getCategory()
                );


        if (recommendedCrew != null) {

            crewBox.setValue(
                    recommendedCrew);

        } else {

            crewBox.setValue(null);
        }


        showRecommendation(fault);
    }


    // =========================
    // SHARED DATA LISTENER
    // =========================

    private void setupDataListener() {

        FaultService.getFaults().addListener(
                (ListChangeListener<Fault>) change -> {

                    faultTable.setItems(
                            FXCollections.observableArrayList(
                                    dispatchService.prioritizeFaults(
                                            FaultService.getFaults()
                                    )
                            )
                    );

                    faultTable.refresh();

                    updateSummary();

                    crewBox.setItems(
                            new ResourceDAO().getAvailableCrews()
                    );
                }
        );
    }


    // =========================
    // ASSIGN CREW
    // =========================

    @FXML
    private void assignCrew() {

        Fault selectedFault =
                faultTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedFault == null) {

            showWarning(
                    "No Fault Selected",
                    "Please select a fault from the dispatch queue."
            );

            return;
        }

        String selectedCrew =
                crewBox.getValue();

        if (selectedCrew == null) {

            showWarning(
                    "No Crew Selected",
                    "Please select a response crew."
            );

            return;
        }

        // Save these before starting the background task
        int faultId = selectedFault.getId();
        String faultIdText = selectedFault.getFaultId();
        String crewName = selectedCrew;

        // Disable the button immediately
        assignCrewButton.setDisable(true);

        // Visual loading state
        assignCrewButton.setText("⏳ Assigning");

        /*
         * Background task:
         * Database operations happen away from the JavaFX UI thread.
         */
        Task<Void> assignmentTask = new Task<>() {

            @Override
            protected Void call() throws Exception {

                FaultDAO faultDAO =
                        new FaultDAO();

                ResourceDAO resourceDAO =
                        new ResourceDAO();

                // Assign crew to fault
                faultDAO.assignCrew(
                        faultId,
                        crewName
                );

                // Mark crew as busy
                resourceDAO.setCrewBusy(
                        crewName
                );

                return null;
            }
        };


        /*
         * Animated loading text.
         */
        javafx.animation.Timeline loadingAnimation =
                new javafx.animation.Timeline(

                        new javafx.animation.KeyFrame(
                                Duration.ZERO,
                                e -> assignCrewButton.setText(
                                        "⏳ Assigning"
                                )
                        ),

                        new javafx.animation.KeyFrame(
                                Duration.millis(350),
                                e -> assignCrewButton.setText(
                                        "⏳ Assigning."
                                )
                        ),

                        new javafx.animation.KeyFrame(
                                Duration.millis(700),
                                e -> assignCrewButton.setText(
                                        "⏳ Assigning.."
                                )
                        ),

                        new javafx.animation.KeyFrame(
                                Duration.millis(1050),
                                e -> assignCrewButton.setText(
                                        "⏳ Assigning..."
                                )
                        )
                );

        loadingAnimation.setCycleCount(
                javafx.animation.Animation.INDEFINITE
        );

        loadingAnimation.play();


        /*
         * Successful assignment.
         */
        assignmentTask.setOnSucceeded(event -> {

            loadingAnimation.stop();

            // Refresh application data
            FaultService.refresh();

            // Refresh available crews
            crewBox.setItems(
                    new ResourceDAO()
                            .getAvailableCrews()
            );

            // Refresh fault table
            faultTable.setItems(
                    FaultService.getFaults()
            );

            faultTable.refresh();

            // Update dashboard counters
            updateSummary();

            // Show success state
            assignCrewButton.setText(
                    "✓ Crew Assigned"
            );

            showInformation(
                    "Crew Assigned",
                    crewName +
                            " has been assigned to " +
                            faultIdText +
                            "."
            );

            /*
             * Return the button to its normal state
             * after a short success animation.
             */
            javafx.animation.PauseTransition resetButton =
                    new javafx.animation.PauseTransition(
                            Duration.millis(1200)
                    );

            resetButton.setOnFinished(e -> {

                assignCrewButton.setText(
                        "Assign Crew"
                );

                updateAssignButton();
            });

            resetButton.play();
        });


        /*
         * If something goes wrong.
         */
        assignmentTask.setOnFailed(event -> {

            loadingAnimation.stop();

            assignCrewButton.setText(
                    "Assign Crew"
            );

            updateAssignButton();

            Throwable error =
                    assignmentTask.getException();

            error.printStackTrace();

            showWarning(
                    "Assignment Failed",
                    "The crew could not be assigned. " +
                            "Please try again."
            );
        });


        /*
         * Run database work in the background.
         */
        Thread assignmentThread =
                new Thread(
                        assignmentTask,
                        "crew-assignment-thread"
                );

        assignmentThread.setDaemon(true);

        assignmentThread.start();
    }


    // =========================
    // SUMMARY
    // =========================

    private void updateSummary() {

        int pending = 0;

        int assigned = 0;


        for (Fault fault :
                faultTable.getItems()) {

            if ("OPEN".equalsIgnoreCase(
                    fault.getStatus())) {

                pending++;
            }


            if ("Assigned".equalsIgnoreCase(
                    fault.getStatus())) {

                assigned++;
            }
        }


        animateNumber(
                pendingCountLabel,
                pending
        );

        animateNumber(
                assignedCountLabel,
                assigned
        );


        int total =
                faultTable.getItems().size();


        if (total == 1) {

            queueCountLabel.setText(
                    "1 fault");

        } else {

            queueCountLabel.setText(
                    total + " faults");
        }


        ResourceDAO resourceDAO =
                new ResourceDAO();


        animateNumber(
                availableCrewCountLabel,
                resourceDAO.getAvailableCrewCount()
        );
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
    // INFORMATION
    // =========================

    private void showInformation(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }


    // =========================
    // CREW RECOMMENDATION
    // =========================

    private void showRecommendation(Fault fault) {

        if (fault == null) {

            recommendedCrewLabel.setText("-");
            recommendationReasonLabel.setText("-");

            return;
        }

        ResourceDAO dao = new ResourceDAO();

        Crew crew = dao.getBestCrew(
                fault.getCategory()
        );

        if (crew == null) {

            recommendedCrewLabel.setText(
                    "No Available Crew"
            );

            recommendationReasonLabel.setText(
                    "No available " +
                            fault.getCategory() +
                            " response crew."
            );

            animateRecommendation();

            return;
        }

        recommendedCrewLabel.setText(
                crew.getName()
        );

        String reason =
                "✓ Type: " +
                        crew.getType() +

                        "\n✓ Status: " +
                        crew.getAvailability() +

                        "\n✓ Capacity: " +
                        crew.getCapacity();


// =========================================================
// SHORTEST ROUTE USING DIJKSTRA
// =========================================================

        ResourceDAO resourceDAO =
                new ResourceDAO();

        FaultDAO faultDAO =
                new FaultDAO();

        int crewLocationId =
                resourceDAO.getHomeLocation(
                        crew.getName()
                );

        int faultLocationId =
                faultDAO.getLocationId(
                        fault.getId()
                );

        if (crewLocationId != -1 &&
                faultLocationId != -1) {

            DispatchService.RouteResult route =
                    dispatchService.findShortestRoute(
                            crewLocationId,
                            faultLocationId
                    );

            if (route.isReachable()) {

                reason +=
                        "\n\n✓ Shortest Route:" +
                                "\n" +
                                route.getFormattedRoute() +

                                "\n\n✓ Distance: " +
                                String.format(
                                        "%.2f km",
                                        route.getDistanceKm()
                                );

            } else {

                reason +=
                        "\n\n⚠ No route available";
            }
        }

        recommendationReasonLabel.setText(
                reason
        );

        animateRecommendation();
    }

    private void animateRecommendation() {

        Node crewLabel = recommendedCrewLabel;
        Node reasonLabel = recommendationReasonLabel;

        crewLabel.setOpacity(0);
        crewLabel.setTranslateY(8);

        reasonLabel.setOpacity(0);
        reasonLabel.setTranslateY(8);

        FadeTransition crewFade =
                new FadeTransition(
                        Duration.millis(300),
                        crewLabel
                );

        crewFade.setFromValue(0);
        crewFade.setToValue(1);

        TranslateTransition crewSlide =
                new TranslateTransition(
                        Duration.millis(300),
                        crewLabel
                );

        crewSlide.setFromY(8);
        crewSlide.setToY(0);

        ParallelTransition crewAnimation =
                new ParallelTransition(
                        crewFade,
                        crewSlide
                );


        FadeTransition reasonFade =
                new FadeTransition(
                        Duration.millis(350),
                        reasonLabel
                );

        reasonFade.setFromValue(0);
        reasonFade.setToValue(1);

        TranslateTransition reasonSlide =
                new TranslateTransition(
                        Duration.millis(350),
                        reasonLabel
                );

        reasonSlide.setFromY(8);
        reasonSlide.setToY(0);

        ParallelTransition reasonAnimation =
                new ParallelTransition(
                        reasonFade,
                        reasonSlide
                );


        crewAnimation.setOnFinished(
                event -> reasonAnimation.play()
        );

        crewAnimation.play();
    }

    // =========================
    // STATUS BADGE
    // =========================

    private void setupStatusColumn() {

        statusColumn.setCellFactory(column ->
                new TableCell<Fault, String>() {

                    private final Label badge = new Label();

                    {
                        badge.setAlignment(javafx.geometry.Pos.CENTER);
                        badge.setMinWidth(85);
                        badge.setPadding(
                                new javafx.geometry.Insets(5, 12, 5, 12)
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
                        setAlignment(javafx.geometry.Pos.CENTER);
                    }
                }
        );
    }


    // =========================
    // PRIORITY BADGE
    // =========================

    private void setupPriorityColumn() {

        priorityColumn.setCellFactory(column ->
                new TableCell<Fault, String>() {

                    private final Label badge = new Label();

                    {
                        badge.setAlignment(javafx.geometry.Pos.CENTER);
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
                        setAlignment(javafx.geometry.Pos.CENTER);
                    }
                }
        );
    }

    private void animateNumber(Label label, int target) {

        if (label == null) {
            return;
        }

        label.setText("0");

        IntegerProperty value =
                new SimpleIntegerProperty(0);

        value.addListener((obs, oldValue, newValue) ->
                label.setText(
                        String.valueOf(newValue.intValue())
                )
        );

        Timeline timeline = new Timeline(

                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(value, 0)
                ),

                new KeyFrame(
                        Duration.millis(900),
                        new KeyValue(value, target)
                )
        );

        timeline.play();
    }

}


