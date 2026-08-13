package controller;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;

import javafx.scene.Node;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.util.Duration;

import model.Fault;
import service.FaultService;


public class DatabaseController {

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
    // SEARCH
    // =========================

    @FXML
    private TextField searchField;


    // =========================
    // STATISTICS
    // =========================

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

        setupDatabase();

        setupSearch();

        updateStatistics();

        FaultService.getFaults().addListener(

                (ListChangeListener<Fault>) change -> {

                    updateStatistics();
                }
        );

        playEntranceAnimations();
    }


    // =========================
    // TABLE SETUP
    // =========================

    private void setupTable() {

        faultIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("faultId")
        );

        areaColumn.setCellValueFactory(
                new PropertyValueFactory<>("area")
        );

        priorityColumn.setCellValueFactory(
                new PropertyValueFactory<>("priorityText")
        );

        crewColumn.setCellValueFactory(
                new PropertyValueFactory<>("crew")
        );

        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("status")
        );
    }


    // =========================
    // DATABASE DATA
    // =========================

    private void setupDatabase() {

        filteredFaults =
                new FilteredList<>(
                        FaultService.getFaults(),
                        fault -> true
                );

        faultTable.setItems(
                filteredFaults
        );
    }


    // =========================
    // SEARCH
    // =========================

    private void setupSearch() {

        searchField.textProperty().addListener(

                (observable, oldValue, newValue) ->

                        applySearch()
        );
    }


    private void applySearch() {

        String search =

                searchField.getText() == null

                        ? ""

                        : searchField
                        .getText()
                        .trim()
                        .toLowerCase();


        filteredFaults.setPredicate(fault -> {

            if (search.isEmpty()) {

                return true;
            }


            return

                    containsIgnoreCase(
                            fault.getFaultId(),
                            search
                    )

                            ||

                            containsIgnoreCase(
                                    fault.getArea(),
                                    search
                            )

                            ||

                            containsIgnoreCase(
                                    fault.getPriorityText(),
                                    search
                            )

                            ||

                            containsIgnoreCase(
                                    fault.getCrew(),
                                    search
                            )

                            ||

                            containsIgnoreCase(
                                    fault.getStatus(),
                                    search
                            );
        });


        updateRecordCount();
    }


    // =========================
    // SEARCH HELPER
    // =========================

    private boolean containsIgnoreCase(

            String value,

            String search) {

        return value != null

                && value
                .toLowerCase()
                .contains(search);
    }


    // =========================
    // STATISTICS
    // =========================

    private void updateStatistics() {

        int total =
                FaultService.getFaults().size();

        int open = 0;

        int assigned = 0;

        int resolved = 0;


        for (Fault fault :

                FaultService.getFaults()) {

            if (fault.getStatus() == null) {

                continue;
            }


            switch (

                    fault.getStatus()
                            .toUpperCase()) {

                case "OPEN":

                    open++;

                    break;


                case "ASSIGNED":

                    assigned++;

                    break;


                case "RESOLVED":

                    resolved++;

                    break;
            }
        }


        animateNumber(
                totalRecordsLabel,
                total
        );

        animateNumber(
                pendingRecordsLabel,
                open
        );

        animateNumber(
                assignedRecordsLabel,
                assigned
        );

        animateNumber(
                resolvedRecordsLabel,
                resolved
        );


        updateRecordCount();
    }


    // =========================
    // NUMBER COUNT-UP
    // =========================

    private void animateNumber(

            Label label,

            int target) {

        if (label == null) {

            return;
        }


        IntegerProperty value =

                new SimpleIntegerProperty(0);


        value.addListener(

                (obs, oldValue, newValue) ->

                        label.setText(

                                String.valueOf(
                                        newValue.intValue()
                                )
                        )
        );


        Timeline timeline =

                new Timeline(

                        new KeyFrame(

                                Duration.ZERO,

                                new KeyValue(
                                        value,
                                        0
                                )
                        ),

                        new KeyFrame(

                                Duration.millis(850),

                                new KeyValue(
                                        value,
                                        target
                                )
                        )
                );


        timeline.play();
    }


    // =========================
    // RECORD COUNT
    // =========================

    private void updateRecordCount() {

        if (filteredFaults == null) {

            return;
        }


        int count =
                filteredFaults.size();


        if (count == 1) {

            recordCountLabel.setText(
                    "1 record"
            );

        } else {

            recordCountLabel.setText(
                    count + " records"
            );
        }
    }


    // =========================
    // REFRESH DATABASE
    // =========================

    @FXML
    private void refreshDatabase(

            ActionEvent event) {

        applySearch();

        updateStatistics();

        faultTable.refresh();


        // Small refresh animation
        animateRefresh();
    }


    // =========================
    // PAGE ENTRANCE ANIMATION
    // =========================

    private void playEntranceAnimations() {

        // Search
        animateNode(
                searchField,
                100
        );


        // Table
        animateNode(
                faultTable,
                300
        );


        // Record count
        animateNode(
                recordCountLabel,
                400
        );
    }


    // =========================
    // NODE FADE + SLIDE
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

                            Duration.millis(450),

                            node
                    );


            fade.setFromValue(0);

            fade.setToValue(1);


            TranslateTransition slide =

                    new TranslateTransition(

                            Duration.millis(450),

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
    // REFRESH ANIMATION
    // =========================

    private void animateRefresh() {

        FadeTransition fade =

                new FadeTransition(

                        Duration.millis(180),

                        faultTable
                );


        fade.setFromValue(0.65);

        fade.setToValue(1);


        fade.play();
    }
}