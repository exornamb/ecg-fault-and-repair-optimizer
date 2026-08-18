package com.g15.dsa.controller;

import com.g15.dsa.algorithms.graph.Dijkstra;
import com.g15.dsa.structures.Graph;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.List;

/**
 * Dispatch & Routing Controller.
 * Provides a live Dijkstra demo over the Accra/Legon ECG substation network.
 */
public class DispatchController {

    // ── Substation node names ────────────────────────────────────────────────
    private static final String[] STATIONS = {
            "0: Achimota Substation",
            "1: Legon Campus (UG)",
            "2: East Legon Feeder",
            "3: Airport Hills Relay",
            "4: Cantonments Hub"
    };

    @FXML private ComboBox<String> cbSource;
    @FXML private ComboBox<String> cbTarget;
    @FXML private TextArea routeOutput;
    @FXML private Label lblVertices;
    @FXML private Label lblEdges;
    @FXML private Label lblDistance;
    @FXML private Label lblHops;

    /** Weighted directed graph of the Accra/Legon grid. */
    private Graph graph;

    @FXML
    public void initialize() {
        cbSource.setItems(FXCollections.observableArrayList(STATIONS));
        cbTarget.setItems(FXCollections.observableArrayList(STATIONS));
        cbSource.getSelectionModel().select(0);
        cbTarget.getSelectionModel().select(1);

        buildGraph();
        lblVertices.setText(String.valueOf(graph.getVertexCount()));

        int edgeCount = 0;
        for (int v = 0; v < graph.getVertexCount(); v++) edgeCount += graph.getNeighbors(v).size();
        lblEdges.setText(String.valueOf(edgeCount));

        routeOutput.setText(
                "Accra/Legon ECG Substation Network loaded.\n\n" +
                "5 Substations | 8 Directed Edges\n\n" +
                "Select source and destination nodes, then click 'Find Shortest Route' to run\n" +
                "Dijkstra's algorithm in real time.\n\n" +
                "The algorithm uses a Min-Heap (PriorityQueue) to greedily select the\n" +
                "unvisited node with the smallest tentative distance at each step.\n" +
                "Time Complexity: O((V + E) log V)"
        );
    }

    @FXML
    public void onRoute() {
        int src = cbSource.getSelectionModel().getSelectedIndex();
        int tgt = cbTarget.getSelectionModel().getSelectedIndex();

        if (src < 0 || tgt < 0) {
            routeOutput.setText("Please select both source and destination substations.");
            return;
        }

        if (src == tgt) {
            routeOutput.setText("Source and destination are the same node.");
            lblDistance.setText("0.0 km");
            lblHops.setText("0");
            return;
        }

        Dijkstra.Result result = Dijkstra.shortestPath(graph, src);
        double dist = result.getDistanceTo(tgt);
        List<Integer> path = result.getPathTo(tgt);

        StringBuilder sb = new StringBuilder();
        sb.append("─── Dijkstra Shortest Path Result ───\n\n");
        sb.append("Source      : ").append(STATIONS[src]).append("\n");
        sb.append("Destination : ").append(STATIONS[tgt]).append("\n\n");

        if (dist == Double.MAX_VALUE || path.isEmpty()) {
            sb.append("⚠ No path exists between the selected substations.");
            lblDistance.setText("∞");
            lblHops.setText("—");
        } else {
            sb.append(String.format("Distance    : %.1f km%n", dist));
            sb.append("Hops        : ").append(path.size() - 1).append("\n\n");
            sb.append("Optimal Route:\n");
            for (int i = 0; i < path.size(); i++) {
                int node = path.get(i);
                sb.append("  ").append(i == 0 ? "🟢 " : i == path.size() - 1 ? "🔴 " : "⚪ ");
                sb.append(STATIONS[node]);
                if (i < path.size() - 1) sb.append(" →\n");
            }
            sb.append("\n\n─── All Distances from ").append(STATIONS[src]).append(" ───\n");
            for (int v = 0; v < graph.getVertexCount(); v++) {
                double d = result.getDistanceTo(v);
                sb.append(String.format("  To %-35s : %s km%n",
                        STATIONS[v], d == Double.MAX_VALUE ? "UNREACHABLE" : String.format("%.1f", d)));
            }
            lblDistance.setText(String.format("%.1f km", dist));
            lblHops.setText(String.valueOf(path.size() - 1));
        }

        routeOutput.setText(sb.toString());
    }

    @FXML
    public void onReset() {
        cbSource.getSelectionModel().select(0);
        cbTarget.getSelectionModel().select(1);
        lblDistance.setText("—");
        lblHops.setText("—");
        initialize();
    }

    private void buildGraph() {
        graph = new Graph(5, true);   // 5 nodes, directed
        // Achimota → Legon (3.2 km)
        graph.addEdge(0, 1, 3.2);
        graph.addEdge(1, 0, 3.2);
        // Achimota → East Legon (5.7 km)
        graph.addEdge(0, 2, 5.7);
        graph.addEdge(2, 0, 5.7);
        // Legon → East Legon (2.1 km)
        graph.addEdge(1, 2, 2.1);
        graph.addEdge(2, 1, 2.1);
        // East Legon → Airport Hills (3.4 km)
        graph.addEdge(2, 3, 3.4);
        graph.addEdge(3, 2, 3.4);
        // Airport Hills → Cantonments (4.1 km)
        graph.addEdge(3, 4, 4.1);
        graph.addEdge(4, 3, 4.1);
        // Legon → Cantonments (6.8 km)
        graph.addEdge(1, 4, 6.8);
        graph.addEdge(4, 1, 6.8);
        // Achimota → Airport Hills (7.5 km)
        graph.addEdge(0, 3, 7.5);
        graph.addEdge(3, 0, 7.5);
        // Cantonments → Achimota (8.0 km)
        graph.addEdge(4, 0, 8.0);
        graph.addEdge(0, 4, 8.0);
    }
}
