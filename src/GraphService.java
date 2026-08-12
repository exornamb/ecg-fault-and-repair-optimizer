package service;

import structures.Graph;
import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import structures.HashTable;

public class GraphService {

    // Database location ID -> Graph vertex
    private final HashTable<Integer, Integer> locationToVertex = new HashTable<>();

    // Graph vertex -> Database location ID
    private final HashTable<Integer, Integer> vertexToLocation = new HashTable<>();

    // Graph vertex -> Location name
    private final HashTable<Integer, String> vertexToLocationName = new HashTable<>();

    private Graph graph;

    /**
     * Builds the graph from the PostgreSQL
     * locations and roads tables.
     */
    public Graph buildGraph() throws SQLException {

        locationToVertex.clear();
        vertexToLocation.clear();
        vertexToLocationName.clear();

        loadLocations();

        graph = new Graph(locationToVertex.size());

        loadRoads();

        return graph;
    }

    /**
     * Loads locations and creates the mapping:
     * <p>
     * Database ID -> Graph vertex
     */
    private void loadLocations() throws SQLException {

        String sql = """
                SELECT id, name
                FROM locations
                ORDER BY id;
                """;

        try (Connection conn = DatabaseConnection.getConnection();

             PreparedStatement stmt = conn.prepareStatement(sql);

             ResultSet rs = stmt.executeQuery()) {

            int vertex = 0;

            while (rs.next()) {

                int locationId = rs.getInt("id");

                String locationName = rs.getString("name");

                locationToVertex.put(locationId, vertex);

                vertexToLocation.put(vertex, locationId);

                vertexToLocationName.put(vertex, locationName);

                vertex++;
            }
        }
    }

    /**
     * Loads roads and adds them to the custom Graph.
     * <p>
     * The road distance in kilometres is used
     * as the Dijkstra edge weight.
     */
    private void loadRoads() throws SQLException {

        String sql = """
                SELECT
                    from_id,
                    to_id,
                    km
                FROM roads
                WHERE from_id < to_id
                ORDER BY id;
                """;

        try (Connection conn = DatabaseConnection.getConnection();

             PreparedStatement stmt = conn.prepareStatement(sql);

             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                int fromLocationId = rs.getInt("from_id");

                int toLocationId = rs.getInt("to_id");

                double distance = rs.getDouble("km");

                Integer fromVertex = locationToVertex.get(fromLocationId);

                Integer toVertex = locationToVertex.get(toLocationId);

                // Skip invalid road references
                if (fromVertex == null || toVertex == null) {

                    continue;
                }

                graph.addUndirectedEdge(fromVertex, toVertex, distance);
            }
        }
    }

    /**
     * Returns the database location ID
     * corresponding to a graph vertex.
     */
    public int getLocationId(int vertex) {

        Integer locationId = vertexToLocation.get(vertex);

        if (locationId == null) {

            throw new IllegalArgumentException("No database location exists for graph vertex " + vertex);
        }

        return locationId;
    }

    /**
     * Returns the graph vertex corresponding
     * to a database location ID.
     */
    public int getVertex(int locationId) {

        Integer vertex = locationToVertex.get(locationId);

        if (vertex == null) {

            throw new IllegalArgumentException("Location ID " + locationId + " does not exist in the graph");
        }

        return vertex;
    }

    /**
     * Returns the actual location name
     * for a graph vertex.
     */
    public String getLocationName(int vertex) {

        String name = vertexToLocationName.get(vertex);

        if (name == null) {

            throw new IllegalArgumentException("No location name exists for graph vertex " + vertex);
        }

        return name;
    }

    /**
     * Returns the constructed graph.
     */
    public Graph getGraph() {

        if (graph == null) {

            try {
                buildGraph();

            } catch (SQLException e) {

                throw new RuntimeException("Unable to build road network graph", e);
            }
        }

        return graph;
    }

    /**
     * Returns the number of locations
     * represented by the graph.
     */
    public int getLocationCount() {

        return locationToVertex.size();
    }

    /**
     * Returns the number of roads
     * represented by the graph.
     */
    public int getRoadCount() {

        if (graph == null) {
            getGraph();
        }

        return graph.getEdgeCount() / 2;
    }
}