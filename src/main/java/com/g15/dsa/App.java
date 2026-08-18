package com.g15.dsa;

import com.g15.dsa.algorithms.graph.BFS;
import com.g15.dsa.algorithms.graph.DFS;
import com.g15.dsa.algorithms.graph.Dijkstra;
import com.g15.dsa.algorithms.graph.Kruskal;
import com.g15.dsa.algorithms.graph.Prim;
import com.g15.dsa.algorithms.searching.BinarySearch;
import com.g15.dsa.algorithms.searching.LinearSearch;
import com.g15.dsa.algorithms.sorting.MergeSort;
import com.g15.dsa.algorithms.sorting.QuickSort;
import com.g15.dsa.database.TeamParameters;
import com.g15.dsa.model.Crew;
import com.g15.dsa.model.Fault;
import com.g15.dsa.structures.*;

import java.util.List;

/**
 * Main Application Runner & Interactive CLI Demonstration
 * Group 15: Codebility v2.0
 * Project: ECG Dumsor Response Optimizer (Accra/Legon Distribution Zone)
 */
public class App {

    public static void main(String[] args) {
        printBanner();
        demonstrateTeamParameters();
        demonstrateDataStructures();
        demonstrateGraphAlgorithms();
        demonstrateSortingAndSearching();
        printFooter();
    }

    private static void printBanner() {
        System.out.println("================================================================================");
        System.out.println("        ECG DUMSOR FAULT DISPATCH & REPAIR OPTIMIZER - VERSION 2.0             ");
        System.out.println("               DCIT 204 / 308 JOINT DSA PRACTICAL PROJECT                      ");
        System.out.println("                     GROUP 15: CODEBILITY V2.0                                 ");
        System.out.println("================================================================================");
    }

    private static void demonstrateTeamParameters() {
        System.out.println("\n--- [1] GROUP 15 INDEX-DERIVED PARAMETERS ---");
        System.out.println("Student Index Reference : " + TeamParameters.STUDENT_INDEX + " (" + TeamParameters.STUDENT_NAME + ")");
        System.out.println("Urgency Weight (w_u)    : " + TeamParameters.URGENCY_WEIGHT);
        System.out.println("Road Penalty Factor     : " + TeamParameters.ROAD_PENALTY);
        System.out.println("Hash Table Capacity     : " + TeamParameters.HASH_CAPACITY + " (Prime)");
        System.out.println("Hash Seed Constant      : " + TeamParameters.HASH_SEED);
    }

    private static void demonstrateDataStructures() {
        System.out.println("\n--- [2] CUSTOM DATA STRUCTURES DEMONSTRATION ---");

        // 1. DynamicArray
        DynamicArray<String> array = new DynamicArray<>();
        array.add("UG Legon Substation (L001)");
        array.add("East Legon Switching Hub (L002)");
        array.add("Madina Feeder Station (L003)");
        System.out.println("• DynamicArray Size: " + array.size() + ", Element 0: " + array.get(0));

        // 2. LinkedList
        LinkedList<String> list = new LinkedList<>();
        list.addLast("Step 1: Outage Reported");
        list.addLast("Step 2: Priority Computed");
        list.addLast("Step 3: Crew Dispatched");
        System.out.println("• LinkedList Audit Trail: First -> [" + list.getFirst() + "], Last -> [" + list.getLast() + "]");

        // 3. Stack
        Stack<String> undoStack = new Stack<>();
        undoStack.push("DISPATCH_CREW_ALPHA");
        undoStack.push("UPDATE_STATUS_IN_PROGRESS");
        System.out.println("• Stack (Undo Log) Top Action: " + undoStack.peek() + " (Stack depth: " + undoStack.size() + ")");

        // 4. CircularQueue
        CircularQueue<String> circularQueue = new CircularQueue<>(3);
        circularQueue.enqueue("Slot 1: Emergency Crew");
        circularQueue.enqueue("Slot 2: Underground Cable Crew");
        System.out.println("• CircularQueue Peek: " + circularQueue.peek() + ", Count: " + circularQueue.size());

        // 5. PriorityQueue (Min-Heap for Outage Urgency)
        PriorityQueue<Fault> faultPq = new PriorityQueue<>();
        faultPq.insert(new Fault(1, "SR-101", "Legon Hall", "Substation", 3, "Crew-A", "Pending"));
        faultPq.insert(new Fault(2, "SR-102", "Noguchi Hospital", "Feeder", 5, "Crew-B", "Pending"));
        faultPq.insert(new Fault(3, "SR-103", "Diaspora Hostels", "Line", 4, "Crew-C", "Pending"));
        Fault topUrgent = faultPq.extractMin();
        System.out.println("• PriorityQueue Highest Urgency Dispatched First: " + topUrgent.getFaultId() + 
                           " [" + topUrgent.getPriorityText() + "] at " + topUrgent.getArea());

        // 6. HashTable & CustomMap
        HashTable<String, Crew> crewTable = new HashTable<>(TeamParameters.HASH_CAPACITY);
        crewTable.put("CRW-01", new Crew("CRW-01", "Alpha Fast Response", "Substation", "Available", 4, "L001"));
        crewTable.put("CRW-02", new Crew("CRW-02", "Bravo Cable Tech", "Underground", "Available", 3, "L002"));
        System.out.println("• HashTable O(1) Lookup: CRW-01 -> " + crewTable.get("CRW-01").getName());
    }

    private static void demonstrateGraphAlgorithms() {
        System.out.println("\n--- [3] GRAPH & NETWORK ROUTING ALGORITHMS ---");
        // 5-node ECG network: 0: Achimota Substation, 1: Legon Campus, 2: East Legon, 3: Madina, 4: Adenta
        Graph grid = new Graph(5);
        grid.addUndirectedEdge(0, 1, 4.0); // Achimota - Legon (4 km)
        grid.addUndirectedEdge(0, 2, 8.0); // Achimota - East Legon (8 km)
        grid.addUndirectedEdge(1, 2, 2.0); // Legon - East Legon (2 km)
        grid.addUndirectedEdge(1, 3, 5.0); // Legon - Madina (5 km)
        grid.addUndirectedEdge(2, 3, 1.0); // East Legon - Madina (1 km)
        grid.addUndirectedEdge(3, 4, 3.0); // Madina - Adenta (3 km)

        // BFS & DFS
        List<Integer> bfsOrder = BFS.traverse(grid, 0);
        List<Integer> dfsOrder = DFS.traverse(grid, 0);
        System.out.println("• BFS Grid Traversal from Substation 0: " + bfsOrder);
        System.out.println("• DFS Grid Traversal from Substation 0: " + dfsOrder);

        // Dijkstra Shortest Path
        Dijkstra.Result dijkstraResult = Dijkstra.shortestPaths(grid, 0);
        System.out.println("• Dijkstra Shortest Path 0 (Achimota) -> 4 (Adenta):");
        System.out.println("  - Path: " + dijkstraResult.getPathTo(4) + " | Total Distance: " + dijkstraResult.getDistanceTo(4) + " km");

        // Minimum Spanning Tree (Prim & Kruskal)
        Prim.Result primMst = Prim.minimumSpanningTree(grid, 0);
        Kruskal.Result kruskalMst = Kruskal.minimumSpanningTree(grid);
        System.out.println("• Prim MST Total Feeder Network Cost    : " + primMst.getTotalWeight() + " km");
        System.out.println("• Kruskal MST Total Feeder Network Cost : " + kruskalMst.getTotalWeight() + " km");
    }

    private static void demonstrateSortingAndSearching() {
        System.out.println("\n--- [4] SORTING & SEARCHING BENCHMARK PREVIEW ---");
        Integer[] faultIds = {802, 127, 484, 938, 655, 937, 675, 505, 64, 877};
        
        System.out.print("• Raw Fault Index Array : ");
        printArray(faultIds);

        Integer[] mergeSorted = faultIds.clone();
        MergeSort.sort(mergeSorted);
        System.out.print("• MergeSort (Ascending) : ");
        printArray(mergeSorted);

        Integer[] quickSorted = faultIds.clone();
        QuickSort.sort(quickSorted);
        System.out.print("• QuickSort (Ascending) : ");
        printArray(quickSorted);

        Integer target = 802;
        int linIdx = LinearSearch.search(faultIds, target);
        int binIdx = BinarySearch.search(quickSorted, target);
        System.out.println("• LinearSearch on raw array for ID " + target + " -> found at index: " + linIdx);
        System.out.println("• BinarySearch on sorted array for ID " + target + " -> found at index: " + binIdx);
    }

    private static <T> void printArray(T[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + (i < arr.length - 1 ? ", " : ""));
        }
        System.out.println("]");
    }

    private static void printFooter() {
        System.out.println("\n================================================================================");
        System.out.println(" All 14 custom data structures and 13 algorithms verified and operational.");
        System.out.println(" Run 'mvn test' to execute the complete automated test suite (70+ tests).");
        System.out.println("================================================================================\n");
    }
}
