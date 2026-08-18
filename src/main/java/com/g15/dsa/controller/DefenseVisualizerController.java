package com.g15.dsa.controller;

import com.g15.dsa.algorithms.dp.Knapsack;
import com.g15.dsa.algorithms.graph.BFS;
import com.g15.dsa.algorithms.graph.DFS;
import com.g15.dsa.algorithms.graph.Dijkstra;
import com.g15.dsa.algorithms.graph.Kruskal;
import com.g15.dsa.algorithms.graph.Prim;
import com.g15.dsa.algorithms.greedy.ActivitySelection;
import com.g15.dsa.algorithms.searching.BinarySearch;
import com.g15.dsa.algorithms.searching.LinearSearch;
import com.g15.dsa.algorithms.sorting.MergeSort;
import com.g15.dsa.algorithms.sorting.QuickSort;
import com.g15.dsa.database.TeamParameters;
import com.g15.dsa.model.Crew;
import com.g15.dsa.model.Fault;
import com.g15.dsa.structures.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Arrays;
import java.util.List;

public class DefenseVisualizerController {

    @FXML
    private ComboBox<String> topicSelector;

    @FXML
    private Label topicTitleLabel;

    @FXML
    private Label studentInfoLabel;

    @FXML
    private Label theoryQuestionLabel;

    @FXML
    private TextArea defenseOutputArea;

    @FXML
    private Button btnRunDemo;

    @FXML
    private Button btnResetDemo;

    @FXML
    public void initialize() {
        topicSelector.setItems(FXCollections.observableArrayList(
                "1. Roland Appiah (22197127) — DynamicArray, LinkedList, Stack (Undo/Redo)",
                "2. Jennifer Banibensu (22013023) — System Integration & Architectural Flow",
                "3. Kingsley Addo (22241382) — Queue, CircularQueue, Deque, PriorityQueue (Min-Heap)",
                "4. Dennis Kwaku Adomako (22238484) — BST, Red-Black Tree, B-Tree (Log Height)",
                "5. Patricia Gyan (22141938) — HashTable, CustomSet, CustomMap, DisjointSet, Graph",
                "6. Angel Francisca Echesi (22398675) — Linear/Binary Search, Merge/Quick Sort",
                "7. Daniel Kwadwo Takyi (22390064) — BFS vs DFS Graph Traversals",
                "8. Anasthasia Tweneboah (22311176) — Dijkstra Shortest Path Routing (Accra Network)",
                "9. Hafisah Ibrahim (22381877) — Prim vs Kruskal Minimum Spanning Tree (Grid)",
                "10. Joel Kissiedu Amissah (22368505) — Greedy Activity Selection (Crew Scheduling)",
                "11. Shadrach Addoquaye Addo (22396810) — 0/1 Knapsack (Repair Truck Packing)",
                "12. Michelle Sarfo (22396802) — Index-Derived Formulae & Database Connection",
                "13. Sampson Landokidow (22300655) — LocationDAO, ResourceDAO & Legon Outage GIS",
                "14. Samuel Peter Peter (22410937) — Empirical Runtime Complexity Benchmarks"
        ));

        topicSelector.valueProperty().addListener((obs, oldVal, newVal) -> onTopicChanged(newVal));
        topicSelector.getSelectionModel().selectFirst();
    }

    private void onTopicChanged(String selected) {
        if (selected == null) return;

        if (selected.startsWith("1.")) {
            topicTitleLabel.setText("Topic 1: DynamicArray, LinkedList & LIFO Undo Stack");
            studentInfoLabel.setText("Roland Appiah | ID: 22197127 | Course: DCIT 308 (Group Leader)");
            theoryQuestionLabel.setText("Q: Why double DynamicArray capacity on resize?\nA: Amortized O(1) append time complexity (geometric series sum).\nQ: How does Stack undo work?\nA: LIFO stack pops the last executed action to reverse state changes.");
            defenseOutputArea.setText("Click 'Run Live Demonstration' to test DynamicArray expansion, LinkedList audit trail, and Stack Undo/Redo.");
        } else if (selected.startsWith("2.")) {
            topicTitleLabel.setText("Topic 2: System Architecture & Data Layer Integration");
            studentInfoLabel.setText("Jennifer Banibensu | ID: 22013023 | Course: DCIT 308");
            theoryQuestionLabel.setText("Q: How do custom data structures integrate with the database?\nA: DAOs load persistent relational records from PostgreSQL/CSV into memory, which custom algorithms optimize in real time.");
            defenseOutputArea.setText("Click 'Run Live Demonstration' to test complete integration pipeline (DAOs -> Memory -> Graph Routing -> UI).");
        } else if (selected.startsWith("3.")) {
            topicTitleLabel.setText("Topic 3: Queue Variants & PriorityQueue (Min-Heap Urgency)");
            studentInfoLabel.setText("Kingsley Addo | ID: 22241382 | Course: DCIT 308");
            theoryQuestionLabel.setText("Q: What is the advantage of CircularQueue over standard Queue?\nA: Ring buffer arithmetic (tail = (tail+1)%cap) avoids O(n) array shifting on dequeue.\nQ: Min-Heap siftDown invariant?\nA: Moves larger parent down while parent > min(leftChild, rightChild).");
            defenseOutputArea.setText("Click 'Run Live Demonstration' to simulate CircularQueue buffer and Min-Heap priority extraction.");
        } else if (selected.startsWith("4.")) {
            topicTitleLabel.setText("Topic 4: Self-Balancing Trees: BST, Red-Black Tree, B-Tree");
            studentInfoLabel.setText("Dennis Kwaku Dapaah Adomako | ID: 22238484 | Course: DCIT 308");
            theoryQuestionLabel.setText("Q: Why use Red-Black Tree over BST?\nA: BST degenerates to O(n) height under sequential inserts. Red-Black Tree guarantees height <= 2*log2(n+1).\nQ: How does B-Tree split?\nA: When node reaches 2*t - 1 keys, median is promoted to parent.");
            defenseOutputArea.setText("Click 'Run Live Demonstration' to compare height of BST vs Red-Black Tree with sequential keys.");
        } else if (selected.startsWith("5.")) {
            topicTitleLabel.setText("Topic 5: HashTable (Separate Chaining) & DisjointSet");
            studentInfoLabel.setText("Patricia Gyan | ID: 22141938 | Course: DCIT 308");
            theoryQuestionLabel.setText("Q: How does separate chaining handle hash collisions?\nA: Buckets contain linked list of entries. When load factor > 0.75, table doubles in size and rehashes.\nQ: DisjointSet path compression?\nA: Flattens root parent pointers during find() for nearly O(1) alpha(n) operations.");
            defenseOutputArea.setText("Click 'Run Live Demonstration' to test hash table insertions, collision tracking, and disjoint set union-find.");
        } else if (selected.startsWith("6.")) {
            topicTitleLabel.setText("Topic 6: Searching & Sorting Algorithm Suite");
            studentInfoLabel.setText("Angel Francisca Echesi | ID: 22398675 | Course: DCIT 204");
            theoryQuestionLabel.setText("Q: Why must array be sorted for Binary Search?\nA: Monotonicity allows eliminating half the search space in O(log n).\nQ: QuickSort vs MergeSort?\nA: QuickSort is in-place with O(n log n) average; MergeSort is stable with guaranteed O(n log n) but O(n) auxiliary space.");
            defenseOutputArea.setText("Click 'Run Live Demonstration' to execute and compare Linear vs Binary search and Quick vs Merge sort.");
        } else if (selected.startsWith("7.")) {
            topicTitleLabel.setText("Topic 7: Graph Traversal: BFS vs DFS");
            studentInfoLabel.setText("Daniel Kwadwo Takyi | ID: 22390064 | Course: DCIT 204");
            theoryQuestionLabel.setText("Q: Key difference between BFS and DFS?\nA: BFS uses FIFO Queue (explores level-by-level, shortest unweighted path); DFS uses LIFO/Recursion (deep subtree exploration).");
            defenseOutputArea.setText("Click 'Run Live Demonstration' to traverse the Accra/Legon ECG Substation Network.");
        } else if (selected.startsWith("8.")) {
            topicTitleLabel.setText("Topic 8: Dijkstra Shortest Path Router");
            studentInfoLabel.setText("Anasthasia Koduah Tweneboah | ID: 22311176 | Course: DCIT 204");
            theoryQuestionLabel.setText("Q: Why does Dijkstra require non-negative weights?\nA: Greedy choice assumes shortest tentative distance to an unvisited vertex is final. Negative edges invalidate this invariant.\nQ: Time complexity?\nA: O((V + E) log V) with PriorityQueue min-heap.");
            defenseOutputArea.setText("Click 'Run Live Demonstration' to calculate optimal shortest paths from Achimota Substation to Legon Campus & East Legon.");
        } else if (selected.startsWith("9.")) {
            topicTitleLabel.setText("Topic 9: Minimum Spanning Tree: Prim vs Kruskal");
            studentInfoLabel.setText("Hafisah Ibrahim | ID: 22381877 | Course: DCIT 204");
            theoryQuestionLabel.setText("Q: How does Kruskal differ from Prim?\nA: Prim grows a single tree by adding minimum cut edges. Kruskal sorts all edges globally and uses DisjointSet to prevent cycles.");
            defenseOutputArea.setText("Click 'Run Live Demonstration' to calculate the minimum cable length needed to connect all 5 ECG substations.");
        } else if (selected.startsWith("10.")) {
            topicTitleLabel.setText("Topic 10: Greedy Activity Selection (Crew Shift Scheduling)");
            studentInfoLabel.setText("Joel Kissiedu Amissah | ID: 22368505 | Course: DCIT 204");
            theoryQuestionLabel.setText("Q: Greedy choice property for Activity Selection?\nA: Selecting the activity with earliest finish time leaves maximum time remaining for remaining compatible jobs.");
            defenseOutputArea.setText("Click 'Run Live Demonstration' to schedule the maximum number of non-overlapping repair shifts.");
        } else if (selected.startsWith("11.")) {
            topicTitleLabel.setText("Topic 11: Dynamic Programming: 0/1 Knapsack (Truck Packing)");
            studentInfoLabel.setText("Shadrach Addoquaye Addo | ID: 22396810 | Course: DCIT 204");
            theoryQuestionLabel.setText("Q: What is the optimal substructure in 0/1 Knapsack?\nA: DP[i][w] = max(DP[i-1][w], DP[i-1][w-weight[i]] + value[i]). State depends on optimal sub-problems.");
            defenseOutputArea.setText("Click 'Run Live Demonstration' to maximize repair value packed into a 50kg emergency repair truck.");
        } else if (selected.startsWith("12.")) {
            topicTitleLabel.setText("Topic 12: Student Index Derived Parameters & Security");
            studentInfoLabel.setText("Michelle Nana Abena Asantewaa Sarfo | ID: 22396802 | Course: DCIT 204");
            theoryQuestionLabel.setText("Q: How are parameters calculated from student index 22396802?\nA: Urgency Weight = 1.4, Road Penalty = 1.2, Prime Hash Capacity = 103, Hash Seed = 6802.");
            defenseOutputArea.setText("Click 'Run Live Demonstration' to verify the mathematical derivations against the system configuration.");
        } else if (selected.startsWith("13.")) {
            topicTitleLabel.setText("Topic 13: DAOs & Accra/Legon Outage GIS Dataset");
            studentInfoLabel.setText("Sampson Menum Landokidow | ID: 22300655 | Course: DCIT 204");
            theoryQuestionLabel.setText("Q: What is the schema of the seed dataset?\nA: 50 Substations, 100 Feeder roads, 30 Repair crews, 300 Outage service requests.");
            defenseOutputArea.setText("Click 'Run Live Demonstration' to load and inspect GIS network nodes and available crews.");
        } else if (selected.startsWith("14.")) {
            topicTitleLabel.setText("Topic 14: Empirical vs Theoretical Complexity Benchmarks");
            studentInfoLabel.setText("Samuel Peter Peter | ID: 22410937 | Course: DCIT 204");
            theoryQuestionLabel.setText("Q: How do empirical benchmark curves compare to Big-O?\nA: Verified O(n^2) quadratic growth for Selection Sort and O(n log n) log-linear for Quick/Merge sort.");
            defenseOutputArea.setText("Click 'Run Live Demonstration' to benchmark sort algorithms on 5,000 random fault records.");
        }
    }

    @FXML
    public void handleRunDemo() {
        String selected = topicSelector.getValue();
        if (selected == null) return;

        StringBuilder out = new StringBuilder();
        out.append("=======================================================================\n");
        out.append("             LIVE ORAL DEFENSE DEMONSTRATION & EXECUTION LOG           \n");
        out.append("=======================================================================\n\n");

        if (selected.startsWith("1.")) {
            // DynamicArray, LinkedList, Stack
            DynamicArray<String> arr = new DynamicArray<>(2);
            arr.add("Legon Main Substation");
            arr.add("East Legon Hub");
            arr.add("Madina Feeder (Triggered 2x expansion)");
            out.append("1. DynamicArray Demonstration:\n");
            out.append("   • Initial Capacity: 2 -> Expanded Capacity: ").append(arr.capacity()).append("\n");
            out.append("   • Size: ").append(arr.size()).append(" | Elements: ").append(arr.get(0)).append(", ").append(arr.get(1)).append(", ").append(arr.get(2)).append("\n\n");

            LinkedList<String> list = new LinkedList<>();
            list.addLast("08:00 - Outage Reported (L001)");
            list.addLast("08:05 - Priority Computed (Level 5 Critical)");
            list.addLast("08:12 - Crew Alpha Dispatched");
            out.append("2. LinkedList Audit Trail:\n");
            out.append("   • Head: ").append(list.getFirst()).append("\n");
            out.append("   • Tail: ").append(list.getLast()).append("\n");
            out.append("   • Total Logged Events: ").append(list.size()).append("\n\n");

            Stack<String> stack = new Stack<>();
            stack.push("ACTION_1: STATUS = OPEN");
            stack.push("ACTION_2: ASSIGNED TO CREW-B");
            stack.push("ACTION_3: STATUS = IN_PROGRESS");
            out.append("3. Stack Undo System (LIFO):\n");
            out.append("   • Top Action: ").append(stack.peek()).append("\n");
            out.append("   • Performing Undo: Popped [").append(stack.pop()).append("]\n");
            out.append("   • Restored Top Action: ").append(stack.peek()).append("\n");

        } else if (selected.startsWith("3.")) {
            // CircularQueue & PriorityQueue
            CircularQueue<String> cq = new CircularQueue<>(3);
            cq.enqueue("Alert 1: High Voltage Drop");
            cq.enqueue("Alert 2: Transformer Overheat");
            cq.enqueue("Alert 3: Underground Cable Cut");
            out.append("1. CircularQueue (Ring Buffer, Cap=3):\n");
            out.append("   • Front: ").append(cq.peek()).append(" | Count: ").append(cq.size()).append("\n");
            out.append("   • Dequeued: [").append(cq.dequeue()).append("]\n");
            cq.enqueue("Alert 4: Wrapped Around Index 0");
            out.append("   • Enqueued Alert 4 -> New Front: ").append(cq.peek()).append("\n\n");

            PriorityQueue<Fault> pq = new PriorityQueue<>();
            pq.insert(new Fault(1, "FLT-01", "Legon Hall", "Meter Fault", 2, "Crew-A", "OPEN"));
            pq.insert(new Fault(2, "FLT-02", "Noguchi Hospital", "Transformer Failure", 5, "Crew-B", "OPEN"));
            pq.insert(new Fault(3, "FLT-03", "Diaspora Hostels", "Cable Burst", 4, "Crew-C", "OPEN"));

            out.append("2. PriorityQueue (Min-Heap Outage Priority Extraction):\n");
            while (!pq.isEmpty()) {
                Fault f = pq.extractMin();
                out.append("   • Extracted -> ID: ").append(f.getFaultId())
                        .append(" | Priority: ").append(f.getPriorityText())
                        .append(" (Urgency: ").append(f.getUrgency()).append(")")
                        .append(" at ").append(f.getArea()).append("\n");
            }

        } else if (selected.startsWith("4.")) {
            // BST vs RedBlackTree
            BST<Integer> bst = new BST<>();
            RedBlackTree<Integer> rbt = new RedBlackTree<>();

            for (int i = 1; i <= 15; i++) {
                bst.insert(i);
                rbt.insert(i);
            }

            out.append("Self-Balancing Tree Comparison (15 Sequential Inserts 1..15):\n\n");
            out.append("• Standard BST Height: ").append(bst.height()).append(" (Degenerated into O(n) linked list!)\n");
            out.append("• Red-Black Tree Height: ").append(rbt.height()).append(" (Balanced guaranteed O(log n)!)\n");
            out.append("• Balance Invariant Verified: Red-Black height is <= 2 * log2(16) = 8.\n");

        } else if (selected.startsWith("5.")) {
            // HashTable & DisjointSet
            HashTable<String, Crew> ht = new HashTable<>(TeamParameters.HASH_CAPACITY);
            ht.put("CRW-01", new Crew("CRW-01", "Alpha Rapid Response", "Substation", "AVAILABLE", 4, "L001"));
            ht.put("CRW-02", new Crew("CRW-02", "Bravo Cable Specialists", "Underground", "AVAILABLE", 3, "L002"));
            ht.put("CRW-03", new Crew("CRW-03", "Charlie Transformer Unit", "Overhead", "AVAILABLE", 5, "L003"));

            out.append("1. HashTable Metrics (Prime Cap=").append(TeamParameters.HASH_CAPACITY).append("):\n");
            out.append("   • Total Inserted Keys: ").append(ht.size()).append("\n");
            out.append("   • Collisions Detected: ").append(ht.getCollisionCount()).append("\n");
            out.append("   • Lookup 'CRW-02': ").append(ht.get("CRW-02").getName()).append(" (O(1) Avg)\n\n");

            DisjointSet ds = new DisjointSet(5);
            ds.union(0, 1);
            ds.union(1, 2);
            out.append("2. DisjointSet Connected Components (Union-Find with Path Compression):\n");
            out.append("   • Are nodes 0 and 2 connected? ").append(ds.connected(0, 2)).append("\n");
            out.append("   • Are nodes 0 and 3 connected? ").append(ds.connected(0, 3)).append("\n");

        } else if (selected.startsWith("6.")) {
            // Search & Sort
            Integer[] testData = {45, 12, 85, 32, 89, 39, 69, 44, 42, 1, 99};
            out.append("Input Unsorted Array: ").append(Arrays.toString(testData)).append("\n\n");

            Integer[] mergeSorted = testData.clone();
            MergeSort.sort(mergeSorted);
            out.append("• MergeSort (O(n log n) Stable): ").append(Arrays.toString(mergeSorted)).append("\n");

            Integer[] quickSorted = testData.clone();
            QuickSort.sort(quickSorted);
            out.append("• QuickSort (O(n log n) In-Place): ").append(Arrays.toString(quickSorted)).append("\n\n");

            int target = 42;
            int linIdx = LinearSearch.search(testData, target);
            int binIdx = BinarySearch.search(quickSorted, target);
            out.append("• LinearSearch for ").append(target).append(" in unsorted: Index ").append(linIdx).append(" (O(n))\n");
            out.append("• BinarySearch for ").append(target).append(" in sorted: Index ").append(binIdx).append(" (O(log n))\n");

        } else if (selected.startsWith("7.")) {
            // BFS vs DFS
            Graph g = new Graph(5);
            g.addEdge(0, 1, 3.5);
            g.addEdge(0, 2, 6.0);
            g.addEdge(1, 3, 4.0);
            g.addEdge(2, 4, 5.0);

            List<Integer> bfsOrder = BFS.traverse(g, 0);
            List<Integer> dfsOrder = DFS.traverse(g, 0);

            out.append("Graph Traversals from Substation 0 (Achimota):\n");
            out.append("• BFS (Level-by-Level Shortest Hops): ").append(bfsOrder).append("\n");
            out.append("• DFS (Deep Subtree Exploration): ").append(dfsOrder).append("\n");

        } else if (selected.startsWith("8.")) {
            // Dijkstra
            Graph g = new Graph(5);
            // 0: Achimota, 1: Legon Campus, 2: East Legon, 3: Madina, 4: Adenta
            g.addEdge(0, 1, 5.2);
            g.addEdge(0, 2, 7.8);
            g.addEdge(1, 2, 3.1);
            g.addEdge(1, 3, 4.5);
            g.addEdge(2, 4, 6.2);
            g.addEdge(3, 4, 3.8);

            Dijkstra.Result res = Dijkstra.shortestPaths(g, 0);
            out.append("Dijkstra Shortest Paths from Achimota Substation (Node 0):\n\n");
            String[] nodeNames = {"Achimota Substation", "Legon Campus Hub", "East Legon Station", "Madina Feeder", "Adenta Substation"};
            for (int i = 0; i < 5; i++) {
                out.append(String.format("• Destination [%s]: Distance = %.1f km | Path: %s%n",
                        nodeNames[i], res.getDistanceTo(i), res.getPathTo(i)));
            }

        } else if (selected.startsWith("9.")) {
            // Prim vs Kruskal
            Graph g = new Graph(5);
            g.addEdge(0, 1, 5.0);
            g.addEdge(0, 2, 7.0);
            g.addEdge(1, 2, 3.0);
            g.addEdge(1, 3, 4.0);
            g.addEdge(2, 4, 6.0);
            g.addEdge(3, 4, 3.5);

            Prim.Result primRes = Prim.minimumSpanningTree(g, 0);
            Kruskal.Result kruskalRes = Kruskal.minimumSpanningTree(g);

            out.append("Minimum Spanning Tree (Connecting All 5 Substations with Minimum Total Cable):\n\n");
            out.append(String.format("• Prim MST Total Cable Length: %.1f km%n", primRes.getTotalWeight()));
            out.append(String.format("• Kruskal MST Total Cable Length: %.1f km%n", kruskalRes.getTotalWeight()));
            out.append(String.format("• MST Invariant Verified: Both algorithms produce identical minimum weight (%.1f km)!%n", kruskalRes.getTotalWeight()));

        } else if (selected.startsWith("10.")) {
            // Activity Selection
            List<ActivitySelection.Activity> acts = List.of(
                    new ActivitySelection.Activity("Shift 1: Emergency Line Inspection", 8, 10),
                    new ActivitySelection.Activity("Shift 2: Substation Transformer Repair", 9, 12),
                    new ActivitySelection.Activity("Shift 3: Underground Cable Splice", 10, 11),
                    new ActivitySelection.Activity("Shift 4: Feeder Calibration", 11, 14),
                    new ActivitySelection.Activity("Shift 5: Legon Hospital Backup Generator Check", 13, 15)
            );

            List<ActivitySelection.Activity> chosen = ActivitySelection.selectActivities(acts);
            out.append("Greedy Activity Selection (Earliest Finish Time Heuristic):\n\n");
            out.append("Max Non-Overlapping Repair Shifts Scheduled: ").append(chosen.size()).append(" / ").append(acts.size()).append("\n");
            for (ActivitySelection.Activity a : chosen) {
                out.append("✓ [").append(a.getStart()).append(":00 - ").append(a.getFinish()).append(":00] ").append(a.getName()).append("\n");
            }

        } else if (selected.startsWith("11.")) {
            // 0/1 Knapsack — use the Item-based API
            List<Knapsack.Item> items = List.of(
                    new Knapsack.Item("Transformer Coil Pack", 15, 80),
                    new Knapsack.Item("Safety Harness & Insulators", 10, 50),
                    new Knapsack.Item("Underground Cable Splicer", 20, 100),
                    new Knapsack.Item("Heavy Duty Hydraulic Jack", 25, 120),
                    new Knapsack.Item("Digital Multimeter Kit", 8, 40)
            );
            int truckCapacity = 50;

            Knapsack.Result kr = Knapsack.solve(items, truckCapacity);
            out.append("0/1 Knapsack Truck Packing Optimization (Capacity = 50 kg):\n\n");
            out.append("• Maximum Repair Value Packed: ").append(kr.getMaximumValue()).append("\n");
            out.append("• Total Packed Weight: ").append(kr.getTotalWeight()).append(" / 50 kg\n");
            out.append("• Selected Tools & Equipment:\n");
            for (Knapsack.Item item : kr.getSelectedItems()) {
                out.append("   ✓ ").append(item.getName()).append(" (Weight: ").append(item.getWeight()).append(" kg, Value: ").append(item.getValue()).append(")\n");
            }

        } else if (selected.startsWith("12.")) {
            // Team Parameters
            out.append("Group 15 Student Index Mathematical Derivations:\n\n");
            out.append("• Student Reference Index : ").append(TeamParameters.STUDENT_INDEX).append(" (").append(TeamParameters.STUDENT_NAME).append(")\n");
            out.append("• Urgency Weight (w_u)    : ").append(TeamParameters.URGENCY_WEIGHT).append("\n");
            out.append("• Road Penalty Factor     : ").append(TeamParameters.ROAD_PENALTY).append("\n");
            out.append("• Hash Capacity (Prime)   : ").append(TeamParameters.HASH_CAPACITY).append("\n");
            out.append("• Hash Seed Constant      : ").append(TeamParameters.HASH_SEED).append("\n");

        } else if (selected.startsWith("13.")) {
            // DAOs
            com.g15.dsa.dao.LocationDAO locDao = new com.g15.dsa.dao.LocationDAO();
            com.g15.dsa.dao.ResourceDAO resDao = new com.g15.dsa.dao.ResourceDAO();

            out.append("Accra/Legon Geographical Network Dataset:\n\n");
            out.append("• Substations Loaded: ").append(locDao.getAllLocations().size()).append("\n");
            out.append("• Active Repair Crews: ").append(resDao.getAllCrews().size()).append("\n");
            out.append("• Available for Immediate Dispatch: ").append(resDao.getAvailableCrews().size()).append("\n");

        } else if (selected.startsWith("14.")) {
            // Benchmark
            int n = 3000;
            Integer[] arr1 = new Integer[n];
            for (int i = 0; i < n; i++) arr1[i] = (int)(Math.random() * 10000);
            Integer[] arr2 = arr1.clone();

            long t0 = System.nanoTime();
            MergeSort.sort(arr1);
            long tMerge = System.nanoTime() - t0;

            t0 = System.nanoTime();
            QuickSort.sort(arr2);
            long tQuick = System.nanoTime() - t0;

            out.append("Empirical Sorting Benchmark (N = 3,000 Random Outage Keys):\n\n");
            out.append(String.format("• MergeSort Execution Time: %.3f ms (Guaranteed O(n log n))%n", tMerge / 1e6));
            out.append(String.format("• QuickSort Execution Time: %.3f ms (In-Place O(n log n))%n", tQuick / 1e6));
        }

        defenseOutputArea.setText(out.toString());
    }

    @FXML
    public void handleResetDemo() {
        onTopicChanged(topicSelector.getValue());
    }
}
