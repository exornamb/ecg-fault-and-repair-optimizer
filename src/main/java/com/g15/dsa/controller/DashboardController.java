package com.g15.dsa.controller;

import com.g15.dsa.dao.FaultDAO;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import com.g15.dsa.model.Fault;
import com.g15.dsa.service.FaultService;

import java.util.Map;

public class DashboardController {

    // =========================================================
    // KPI LABELS
    // =========================================================

    @FXML
    private Label activeFaultsLabel;

    @FXML
    private Label activeCrewsLabel;

    @FXML
    private Label avgResponseLabel;

    @FXML
    private Label repairsTodayLabel;

    // =========================================================
    // RECENT FAULTS TABLE
    // =========================================================

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

    // =========================================================
    // CHARTS
    // =========================================================

    @FXML
    private LineChart<String, Number> faultChart;

    @FXML
    private PieChart pieChart;

    // =========================================================
    // INITIALIZE
    // =========================================================

    @FXML
    public void initialize() {

        setupFaultTable();

        faultTable.setItems(
                FaultService.getFaults());

        loadDashboardStatisticsWithAnimation();

        loadFaultChart();

        loadPieChart();

        playDashboardAnimations();

        // =====================================================
        // LIVE DATA UPDATE
        // =====================================================

        FaultService.getFaults().addListener(
                (ListChangeListener<Fault>) change -> {

                    updateDashboardStatistics();

                    loadPieChart();

                    loadFaultChart();

                    faultTable.refresh();

                });
    }

    private void loadDashboardStatisticsWithAnimation() {

        PauseTransition pause = new PauseTransition(
                Duration.millis(180));

        pause.setOnFinished(event -> {

            updateDashboardStatistics();

        });

        pause.play();
    }

    // =========================================================
    // TABLE SETUP
    // =========================================================

    private void setupFaultTable() {

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

        setupPriorityColumn();
        setupStatusColumn();
    }

    private void setupStatusColumn() {

        statusColumn.setCellFactory(column -> new TableCell<Fault, String>() {

            private final Label badge = new Label();

            {
                badge.setAlignment(Pos.CENTER);
                badge.setMinWidth(85);
                badge.setPadding(
                        new javafx.geometry.Insets(5, 12, 5, 12));
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
                                        "-fx-font-weight:bold;");
                        break;

                    case "ASSIGNED":
                        badge.setStyle(
                                "-fx-background-color:#DBEAFE;" +
                                        "-fx-text-fill:#1D4ED8;" +
                                        "-fx-background-radius:20;" +
                                        "-fx-font-weight:bold;");
                        break;

                    case "RESOLVED":
                        badge.setStyle(
                                "-fx-background-color:#DCFCE7;" +
                                        "-fx-text-fill:#166534;" +
                                        "-fx-background-radius:20;" +
                                        "-fx-font-weight:bold;");
                        break;

                    default:
                        badge.setStyle(
                                "-fx-background-color:#F1F5F9;" +
                                        "-fx-text-fill:#475569;" +
                                        "-fx-background-radius:20;" +
                                        "-fx-font-weight:bold;");
                }

                setGraphic(badge);
                setText(null);
                setAlignment(Pos.CENTER);
            }
        });
    }

    private void setupPriorityColumn() {

        priorityColumn.setCellFactory(column -> new TableCell<Fault, String>() {

            private final Label badge = new Label();

            {
                badge.setAlignment(Pos.CENTER);
                badge.setMinWidth(80);
                badge.setPadding(
                        new javafx.geometry.Insets(5, 12, 5, 12));
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
                                        "-fx-font-weight:bold;");
                        break;

                    case "HIGH":
                        badge.setStyle(
                                "-fx-background-color:#FFEDD5;" +
                                        "-fx-text-fill:#C2410C;" +
                                        "-fx-background-radius:20;" +
                                        "-fx-font-weight:bold;");
                        break;

                    case "MEDIUM":
                        badge.setStyle(
                                "-fx-background-color:#FEF3C7;" +
                                        "-fx-text-fill:#A16207;" +
                                        "-fx-background-radius:20;" +
                                        "-fx-font-weight:bold;");
                        break;

                    case "LOW":
                        badge.setStyle(
                                "-fx-background-color:#DBEAFE;" +
                                        "-fx-text-fill:#1D4ED8;" +
                                        "-fx-background-radius:20;" +
                                        "-fx-font-weight:bold;");
                        break;

                    case "VERY LOW":
                        badge.setStyle(
                                "-fx-background-color:#F1F5F9;" +
                                        "-fx-text-fill:#64748B;" +
                                        "-fx-background-radius:20;" +
                                        "-fx-font-weight:bold;");
                        break;

                    default:
                        badge.setStyle(
                                "-fx-background-color:#F1F5F9;" +
                                        "-fx-text-fill:#475569;" +
                                        "-fx-background-radius:20;" +
                                        "-fx-font-weight:bold;");
                }

                setGraphic(badge);
                setText(null);
                setAlignment(Pos.CENTER);
            }
        });
    }

    // =========================================================
    // DASHBOARD STATISTICS
    // =========================================================

    private void updateDashboardStatistics() {

        FaultDAO dao = new FaultDAO();

        int activeFaults = dao.getActiveFaultCount();

        int activeCrews = dao.getActiveCrewCount();

        int resolvedFaults = dao.getResolvedFaultCount();

        animateNumber(
                activeFaultsLabel,
                activeFaults);

        animateNumber(
                activeCrewsLabel,
                activeCrews);

        animateNumber(
                repairsTodayLabel,
                resolvedFaults);

        /*
         * Temporary value.
         *
         * Later we can calculate this using
         * submitted and resolved timestamps.
         */

        avgResponseLabel.setText("18 min");
    }

    // =========================================================
    // KPI NUMBER ANIMATION
    // =========================================================

    private void animateNumber(Label label, int target) {

        if (label == null) {
            return;
        }

        // Start from zero
        label.setText("0");

        IntegerProperty animatedValue = new SimpleIntegerProperty(0);

        animatedValue.addListener(
                (obs, oldValue, newValue) -> {

                    label.setText(
                            String.valueOf(
                                    newValue.intValue()));
                });

        Timeline timeline = new Timeline(

                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(
                                animatedValue,
                                0)),

                new KeyFrame(
                        Duration.millis(1000),
                        new KeyValue(
                                animatedValue,
                                target)));

        timeline.play();
    }

    // =========================================================
    // DASHBOARD ENTRY ANIMATIONS
    // =========================================================

    private void playDashboardAnimations() {

        animateNode(
                faultChart,
                100);

        animateNode(
                pieChart,
                200);

        animateNode(
                faultTable,
                300);

    }

    // =========================================================
    // NODE FADE + SLIDE
    // =========================================================

    private void animateNode(
            javafx.scene.Node node,
            int delay) {

        if (node == null) {
            return;
        }

        node.setOpacity(0);

        node.setTranslateY(15);

        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                Duration.millis(delay));

        pause.setOnFinished(event -> {

            FadeTransition fade = new FadeTransition(
                    Duration.millis(450),
                    node);

            fade.setFromValue(0);

            fade.setToValue(1);

            TranslateTransition slide = new TranslateTransition(
                    Duration.millis(450),
                    node);

            slide.setFromY(15);

            slide.setToY(0);

            ParallelTransition animation = new ParallelTransition(
                    fade,
                    slide);

            animation.play();

        });

        pause.play();
    }

    // =========================================================
    // FAULT TREND CHART
    // =========================================================

    private void loadFaultChart() {

        faultChart.getData().clear();

        FaultDAO dao = new FaultDAO();

        Map<String, Integer> counts = dao.getWeeklyFaultCounts();

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        series.setName("Faults");

        for (Map.Entry<String, Integer> entry : counts.entrySet()) {

            series.getData().add(

                    new XYChart.Data<>(
                            entry.getKey(),
                            entry.getValue())

            );

        }

        faultChart.getData().add(series);
    }

    // =========================================================
    // FAULT CATEGORY PIE CHART
    // =========================================================

    private void loadPieChart() {

        FaultDAO dao = new FaultDAO();

        Map<String, Integer> counts = dao.getFaultCategoryCounts();

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : counts.entrySet()) {

            data.add(

                    new PieChart.Data(
                            entry.getKey(),
                            entry.getValue())

            );

        }

        pieChart.setData(data);

        pieChart.setLabelsVisible(true);

        pieChart.setLegendVisible(true);
    }
}