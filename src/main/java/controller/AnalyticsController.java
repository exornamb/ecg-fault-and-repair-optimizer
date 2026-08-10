package controller;

import dao.FaultDAO;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import model.CrewWorkload;
import model.Fault;
import service.FaultService;
import dao.AlgorithmRunDAO;
import javafx.scene.chart.LineChart;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;

import java.util.Map;

public class AnalyticsController {

    // =========================
    // KPI LABELS
    // =========================

    @FXML
    private Label totalFaultsLabel;

    @FXML
    private Label highPriorityLabel;

    @FXML
    private Label resolvedFaultsLabel;

    @FXML
    private Label resolutionRateLabel;


    // =========================
    // CHARTS
    // =========================

    @FXML
    private BarChart<String, Number> areaChart;

    @FXML
    private PieChart priorityChart;

    @FXML
    private LineChart<String, Number> runtimeChart;

    @FXML
    private LineChart<String, Number> memoryChart;


    // =========================
    // CREW WORKLOAD TABLE
    // =========================

    @FXML
    private TableView<CrewWorkload> workloadTable;

    @FXML
    private TableColumn<CrewWorkload, String> crewNameColumn;

    @FXML
    private TableColumn<CrewWorkload, Integer> assignedFaultsColumn;

    @FXML
    private TableColumn<CrewWorkload, String> availabilityColumn;

    @FXML
    private TableColumn<CrewWorkload, Integer> capacityColumn;


    // =========================
    // INITIALIZE
    // =========================

    @FXML
    public void initialize() {

        refreshAnalytics();

        FaultService.getFaults().addListener(
                (ListChangeListener<Fault>) change ->
                        refreshAnalytics()
        );

        playEntranceAnimations();
    }


    // =========================
    // REFRESH ALL ANALYTICS
    // =========================

    private void refreshAnalytics() {

        updateStatistics();

        loadAreaChart();

        loadPriorityChart();

        loadCrewWorkload();

        loadRuntimeChart();

        loadMemoryChart();
    }


    // =========================
    // KPI STATISTICS
    // =========================

    private void updateStatistics() {

        int totalFaults =
                FaultService.getFaults().size();

        int highPriority = 0;

        int resolved = 0;


        for (Fault fault :
                FaultService.getFaults()) {

            if ("High".equalsIgnoreCase(
                    fault.getPriorityText())) {

                highPriority++;
            }

            if ("RESOLVED".equalsIgnoreCase(
                    fault.getStatus())) {

                resolved++;
            }
        }


        animateNumber(
                totalFaultsLabel,
                totalFaults
        );

        animateNumber(
                highPriorityLabel,
                highPriority
        );

        animateNumber(
                resolvedFaultsLabel,
                resolved
        );


        double resolutionRate = 0;

        if (totalFaults > 0) {

            resolutionRate =
                    (resolved * 100.0)
                            / totalFaults;
        }


        animatePercentage(
                resolutionRateLabel,
                resolutionRate
        );
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
    // PERCENTAGE ANIMATION
    // =========================

    private void animatePercentage(
            Label label,
            double target) {

        if (label == null) {
            return;
        }


        javafx.beans.property.DoubleProperty
                value =
                new javafx.beans.property.SimpleDoubleProperty(
                        0
                );


        value.addListener(
                (obs, oldValue, newValue) ->
                        label.setText(
                                String.format(
                                        "%.1f%%",
                                        newValue.doubleValue()
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
                                Duration.millis(1000),

                                new KeyValue(
                                        value,
                                        target
                                )
                        )
                );


        timeline.play();
    }


    // =========================
    // AREA CHART
    // =========================

    private void loadAreaChart() {

        areaChart.getData().clear();


        FaultDAO dao =
                new FaultDAO();


        Map<String, Integer> areaCounts =
                dao.getAreaCounts();


        XYChart.Series<String, Number> series =
                new XYChart.Series<>();


        for (Map.Entry<String, Integer> entry :
                areaCounts.entrySet()) {

            series.getData().add(

                    new XYChart.Data<>(
                            entry.getKey(),
                            entry.getValue()
                    )
            );
        }


        areaChart.getData().add(series);
    }


    // =========================
    // PRIORITY PIE CHART
    // =========================

    private void loadPriorityChart() {

        FaultDAO dao =
                new FaultDAO();


        Map<String, Integer> counts =
                dao.getPriorityCounts();


        priorityChart.setData(

                FXCollections.observableArrayList(

                        new PieChart.Data(
                                "Critical",
                                counts.getOrDefault(
                                        "Critical",
                                        0
                                )
                        ),

                        new PieChart.Data(
                                "High",
                                counts.getOrDefault(
                                        "High",
                                        0
                                )
                        ),

                        new PieChart.Data(
                                "Medium",
                                counts.getOrDefault(
                                        "Medium",
                                        0
                                )
                        ),

                        new PieChart.Data(
                                "Low",
                                counts.getOrDefault(
                                        "Low",
                                        0
                                )
                        ),

                        new PieChart.Data(
                                "Very Low",
                                counts.getOrDefault(
                                        "Very Low",
                                        0
                                )
                        )
                )
        );


        priorityChart.setLabelsVisible(true);

        priorityChart.setLegendVisible(true);
    }


    // =========================
    // CREW WORKLOAD
    // =========================

    private void loadCrewWorkload() {

        FaultDAO dao =
                new FaultDAO();

        crewNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("crewName"));

        assignedFaultsColumn.setCellValueFactory(
                new PropertyValueFactory<>("assignedFaults"));

        availabilityColumn.setCellValueFactory(
                new PropertyValueFactory<>("availability"));

        capacityColumn.setCellValueFactory(
                new PropertyValueFactory<>("capacity"));

        workloadTable.setItems(
                dao.getCrewWorkload());
    }

    // =========================
// DSA RUNTIME PERFORMANCE
// =========================

    private void loadRuntimeChart() {

        runtimeChart.getData().clear();

        AlgorithmRunDAO dao =
                new AlgorithmRunDAO();

        Map<String, Map<Integer, Double>> results =
                dao.getAverageRuntime();

        for (Map.Entry<String, Map<Integer, Double>> algorithmEntry :
                results.entrySet()) {

            XYChart.Series<String, Number> series =
                    new XYChart.Series<>();

            series.setName(
                    algorithmEntry.getKey()
            );

            for (Map.Entry<Integer, Double> result :
                    algorithmEntry.getValue().entrySet()) {

                series.getData().add(
                        new XYChart.Data<>(
                                String.valueOf(
                                        result.getKey()
                                ),
                                result.getValue()
                        )
                );
            }

            runtimeChart.getData().add(series);
        }
    }

    // =========================
// DSA MEMORY PERFORMANCE
// =========================

    private void loadMemoryChart() {

        memoryChart.getData().clear();

        AlgorithmRunDAO dao =
                new AlgorithmRunDAO();

        Map<String, Map<Integer, Double>> results =
                dao.getAverageMemory();

        for (Map.Entry<String, Map<Integer, Double>> algorithmEntry :
                results.entrySet()) {

            XYChart.Series<String, Number> series =
                    new XYChart.Series<>();

            series.setName(
                    algorithmEntry.getKey()
            );

            for (Map.Entry<Integer, Double> result :
                    algorithmEntry.getValue().entrySet()) {

                series.getData().add(
                        new XYChart.Data<>(
                                String.valueOf(
                                        result.getKey()
                                ),
                                result.getValue()
                        )
                );
            }

            memoryChart.getData().add(series);
        }
    }




    // =========================
    // ENTRANCE ANIMATIONS
    // =========================

    private void playEntranceAnimations() {

        // KPI statistics
        animateNode(
                totalFaultsLabel,
                0
        );

        animateNode(
                highPriorityLabel,
                70
        );

        animateNode(
                resolvedFaultsLabel,
                140
        );

        animateNode(
                resolutionRateLabel,
                210
        );


        // Charts
        animateNode(
                areaChart,
                300
        );

        animateNode(
                priorityChart,
                420
        );


        // Crew workload
        animateNode(
                workloadTable,
                540
        );

        animateNode(
                runtimeChart,
                660
        );

        animateNode(
                memoryChart,
                780
        );
    }


    // =========================
    // FADE + SLIDE
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
}