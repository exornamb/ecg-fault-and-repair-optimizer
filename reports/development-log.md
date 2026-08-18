# Development Log
## ECG Dumsor Response Optimizer — Group 15, Codebility v2.0

---

## Week 1 — Project Setup & Local Context Design (M1)

**Completed:**
- Agreed on ECG Dumsor Response Optimizer as local Ghana context.
- Defined 14 custom data structures, algorithm scope, and database schema.
- Drafted data dictionary and seed CSV templates for locations, roads, resources, and service_requests.
- Set up GitHub repository: `ecg-fault-and-repair-optimizer`.
- Created Neon.tech PostgreSQL database and ran `schema.sql` to initialize all six tables.
- Populated `locations.csv` (50 ECG substations/centres), `roads.csv` (100 feeder connections), `resources.csv` (30 repair crews), `service_requests.csv` (300 outage requests).

**Challenges:**
- Initial confusion over whether to use MySQL vs PostgreSQL. Agreed on PostgreSQL (Neon.tech) because it is free to host, supports JDBC natively, and the schema is clean.
- Took extra time to ensure location coordinates reflect real Accra/Legon geography.

**Decisions:**
- Derived team algorithm parameters from Michelle Sarfo's index number (22396802) using the agreed derivation rules.
- Agreed all data structures must be implemented from scratch — no `java.util.HashMap`, `PriorityQueue`, `TreeMap`, etc.

---

## Week 2 — Data Structure Library (M2)

**Completed:**
- Implemented all 14 data structures: `DynamicArray`, `LinkedList`, `Stack`, `Queue`, `CircularQueue`, `Deque`, `PriorityQueue`, `BST`, `RedBlackTree`, `BTree`, `HashTable`, `CustomMap`, `CustomSet`, `DisjointSet`, `Graph`.
- Each structure has a dedicated unit test class covering normal, boundary, and invalid input cases.
- Total tests at end of week: **59 automated tests passing**.

**Challenges:**
- Red-Black Tree rotations (LL, RR, LR, RL cases) required multiple debugging sessions — recoloring logic when the uncle is red vs black created edge cases.
- B-Tree node splitting (promoting median key on overflow) was the most complex single piece of code in the project.

**Decisions:**
- CircularQueue uses modulo arithmetic rather than shifting elements — keeps dequeue at O(1).
- PriorityQueue uses siftUp on insert and siftDown on extractMin — both O(log n).

---

## Week 3 — Algorithms, Database Integration & Performance (M3–M5)

**Completed:**
- Implemented all required algorithms: `LinearSearch`, `BinarySearch`, `SelectionSort`, `InsertionSort`, `MergeSort`, `QuickSort`, `BFS`, `DFS`, `Dijkstra`, `Prim`, `Kruskal`, `ActivitySelection`, `Knapsack`.
- Added modular `Experiments.java` runner for all 6 required benchmark experiments.
- Ran Search & Sort benchmark: 540 recorded runs across 6 algorithms, 5 input sizes, 18 repetitions each.
- Generated runtime and memory performance graphs (`ECG_runtime_performance_graph.png`).
- Connected application to PostgreSQL via `DatabaseConnection.java` with offline CSV fallback.
- JavaFX controllers for Dashboard, Fault Management, Settings, and Analytics screens.

**Challenges:**
- JVM warm-up effects introduced noise in timing for small input sizes (100, 500). Added 3 warm-up runs before recording for small sizes.
- Binary Search timing at small n is near-zero, making graph scaling tricky — noted in report discussion.
- PostgreSQL JDBC connection from JavaFX required adding the driver to classpath separately.

**Decisions:**
- Selection Sort and Insertion Sort only tested up to n=5,000 in the real-time graph (n=10,000 would take >30 seconds for Selection Sort on the test machine).
- Dijkstra implemented with custom `PriorityQueue<NodeDistance>` (min-heap) rather than brute-force O(V²) scan, giving O((V+E)log V) time.

---

## Week 4 — Testing, Report, Proofs & Final Submission (M6–M7)

**Completed:**
- Expanded unit tests to >70 across all data structures and algorithms.
- Wrote 3 full proof sketches (Selection Sort loop invariant, Dijkstra induction, Greedy exchange argument).
- Documented 2 counterexamples (Greedy Coin Change failure, Binary Search on unsorted array).
- Wrote 5 pseudocode blocks (Priority Dispatch, Dijkstra, Kruskal, B-Tree indexing, Stack Undo Log).
- Completed Ghana localisation note and scheduling/indexing demonstration traces.
- Completed technical report draft with trace tables for all 6 algorithms covered in Section 7 of the brief.
- Reorganized entire codebase to standard Maven package structure (`com.g15.dsa.*`).

**Open items / known limitations:**
- The JavaFX UI components (controllers) depend on FXML files that require scene-builder integration to test visually.
- Graph algorithm timing experiment shows very low absolute values for small graph sizes (50, 100 nodes) due to JVM overhead — this is discussed in the report as expected empirical noise.
- AI assistance was used for drafting pseudocode comments and report section templates. Full acknowledgement is in `ai-assistance-acknowledgement.md`.

---

## Machine Specification (for reproducibility)

- **OS:** Windows 11
- **JDK:** Eclipse Adoptium JDK 21.0.11 (Temurin)
- **Processor:** Intel Core i5 / i7 (HP laptop)
- **RAM:** 8 GB DDR4
- **Database:** PostgreSQL 16 on Neon.tech (Accra latency ~50–120ms per query)
- **Timing method:** `System.nanoTime()` before and after each algorithm invocation, converted to milliseconds.
