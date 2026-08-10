package service;

import algorithms.graph.Dijkstra;
import algorithms.structures.Graph;
import algorithms.structures.PriorityQueue;
import model.Fault;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DispatchService {

    private final GraphService graphService;

    public DispatchService() {

        graphService =
                new GraphService();
    }

    // =========================================================
    // PRIORITIZE FAULTS
    // =========================================================

    /**
     * Places faults into the custom PriorityQueue.
     *
     * Higher urgency gets higher priority.
     */
    public List<Fault> prioritizeFaults(
            List<Fault> faults) {

        PriorityQueue<Fault> queue =
                new PriorityQueue<>(
                        Comparator.comparingInt(
                                Fault::getUrgency
                        )
                );

        for (Fault fault : faults) {

            if (fault != null) {

                queue.enqueue(fault);
            }
        }

        List<Fault> prioritized =
                new ArrayList<>();

        while (!queue.isEmpty()) {

            prioritized.add(
                    queue.dequeue()
            );
        }

        return prioritized;
    }


    // =========================================================
    // SHORTEST ROUTE
    // =========================================================

    /**
     * Uses the custom Graph and Dijkstra algorithm
     * to find the shortest route between two locations.
     *
     * Location IDs are PostgreSQL database IDs.
     */
    public RouteResult findShortestRoute(
            int sourceLocationId,
            int targetLocationId) {

        try {

            Graph graph =
                    graphService.getGraph();

            int sourceVertex =
                    graphService.getVertex(
                            sourceLocationId
                    );

            int targetVertex =
                    graphService.getVertex(
                            targetLocationId
                    );

            Dijkstra.Result result =
                    Dijkstra.shortestPaths(
                            graph,
                            sourceVertex
                    );

            double distance =
                    result.getDistance(
                            targetVertex
                    );

            int[] pathVertices =
                    result.getPath(
                            targetVertex
                    );

            if (pathVertices.length == 0) {

                return new RouteResult(
                        false,
                        Double.POSITIVE_INFINITY,
                        List.of()
                );
            }

            List<String> pathNames =
                    new ArrayList<>();

            for (int vertex :
                    pathVertices) {

                pathNames.add(
                        graphService.getLocationName(
                                vertex
                        )
                );
            }

            return new RouteResult(
                    true,
                    distance,
                    pathNames
            );

        } catch (Exception e) {

            e.printStackTrace();

            return new RouteResult(
                    false,
                    Double.POSITIVE_INFINITY,
                    List.of()
            );
        }
    }


    // =========================================================
    // ROUTE RESULT
    // =========================================================

    public static class RouteResult {

        private final boolean reachable;
        private final double distanceKm;
        private final List<String> locations;

        public RouteResult(
                boolean reachable,
                double distanceKm,
                List<String> locations) {

            this.reachable =
                    reachable;

            this.distanceKm =
                    distanceKm;

            this.locations =
                    new ArrayList<>(
                            locations
                    );
        }

        public boolean isReachable() {
            return reachable;
        }

        public double getDistanceKm() {
            return distanceKm;
        }

        public List<String> getLocations() {
            return new ArrayList<>(
                    locations
            );
        }

        public String getFormattedRoute() {

            if (!reachable ||
                    locations.isEmpty()) {

                return "No route available";
            }

            return String.join(
                    " → ",
                    locations
            );
        }
    }
}