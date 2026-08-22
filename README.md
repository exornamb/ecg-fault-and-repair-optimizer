# ECG Dumsor Response Optimizer
### DCIT 204/308 Joint DSA Project — Group 15: Codebility v2.0

> A realistic Ghana-context power fault dispatch and repair optimization system, built on custom data structures and algorithms for the ECG Accra/Legon distribution network.

---

## Team — Group 15 (Codebility v2.0)

| # | Name | Student ID | Role | Primary Ownership |
|---|---|---|---|---|
| 1 | **Roland Appiah** *(Leader)* | 22197127 | Group Leader (DCIT 308) | App, DynamicArray, LinkedList, Stack |
| 2 | Jennifer Banibensu | 22013023 | Member (DCIT 308) | Technical Report, Documentation |
| 3 | Kingsley Addo | 22241382 | Member (DCIT 308) | Queue, CircularQueue, Deque, PriorityQueue |
| 4 | Dennis Kwaku Dapaah Adomako | 22238484 | Member (DCIT 308) | BST, RedBlackTree, BTree, Schema |
| 5 | Patricia Gyan | 22141938 | Member (DCIT 308) | HashTable, CustomSet, CustomMap, DisjointSet, Graph |
| 6 | Angel Francisca Echesi | 22398675 | Member (DCIT 204) | LinearSearch, BinarySearch, Sorting Algorithms |
| 7 | Daniel Kwadwo Takyi | 22390064 | Member (DCIT 204) | BFS, DFS, Traversal Traces |
| 8 | Anasthasia Koduah Tweneboah | 22311176 | Member (DCIT 204) | Dijkstra, AnalyticsController |
| 9 | Hafisah Ibrahim | 22381877 | Member (DCIT 204) | Prim, Kruskal, MST Traces |
| 10 | Joel Kissiedu Amissah | 22368505 | Member (DCIT 204) | ActivitySelection, Greedy |
| 11 | Shadrach Addoquaye Addo | 22396810 | Member (DCIT 204) | Knapsack DP, SettingsController |
| 12 | Michelle Nana Abena Asantewaa Sarfo | 22396802 | Member (DCIT 204) | TeamParameters, DatabaseConnection |
| 13 | Sampson Menum Landokidow | 22300655 | Member (DCIT 204) | Seed Datasets, LocationDAO |
| 14 | Samuel Peter Peter | 22410937 | Member (DCIT 204) | Experiments, AlgorithmRunDAO, FaultDAO |



---

## Local Ghana Context

This system models the **Electricity Company of Ghana (ECG) Dumsor Response Optimizer** for the Accra/Legon power distribution zone. The 50-node service network covers:
- University of Ghana, Legon campus substations
- East Legon residential grid
- Madina, Adenta, Haatso, and Achimota feeder hubs

Team algorithm parameters are derived from **Michelle Sarfo's index number (22396802)**:

| Parameter | Value | Derivation |
|---|---|---|
| Urgency Weight | **1.4** | 1.0 + (digit_sum(22396802) % 5) × 0.2 |
| Road Penalty | **1.2** | 1.0 + (02 % 10) × 0.1 |
| Hash Capacity | **103** | next_prime(100 + (22396802 % 50)) |
| Hash Seed | **6802** | last 4 digits of index |

---

## Project Structure

```
ecg-fault-and-repair-optimizer/
├── data/                          # Seed datasets (CSV)
│   ├── locations.csv              # 50 ECG substations & service centres
│   ├── roads.csv                  # 100 feeder line connections
│   ├── resources.csv              # 30 repair crews & emergency units
│   ├── service_requests.csv       # 300 outage fault requests
│   └── algorithm_runs.csv         # 540 recorded benchmark runs
│
├── database/
│   └── schema.sql                 # Full PostgreSQL schema
│
├── src/
│   ├── main/java/com/g15/dsa/
│   │   ├── structures/            # 14 custom data structures (zero built-ins)
│   │   ├── algorithms/            # All required algorithms (searching, sorting, graph, greedy, dp)
│   │   ├── database/              # JDBC connection + team parameters
│   │   ├── dao/                   # FaultDAO, LocationDAO, ResourceDAO, AlgorithmRunDAO
│   │   ├── model/                 # Fault, Crew, Location, Road, ServiceRequest, CrewWorkload
│   │   ├── service/               # FaultService, GraphService, DispatchService
│   │   ├── controller/            # JavaFX controllers (Dashboard, Faults, Analytics, Settings)
│   │   └── experiments/           # Experiments.java — all 6 performance benchmarks
│   │
│   └── test/java/com/g15/dsa/    # Comprehensive unit test suite (272 tests — verified: mvn test → 0 failures)
│
├── reports/
│   ├── ECG_Smart_Dispatch_DSA_Technical_Report_With_Performance_Analysis.docx
│   ├── Joint_DSA_Project_Checklist_Cover_Sheet.docx
│   ├── asymptotic-complexity-and-empirical-analysis.md # Big O, Big Theta, Big Omega tying theory to graphs
│   ├── primitive-operations-and-brute-force-analysis.md # Primitive op counts (best/avg/worst) + brute-force infeasibility
│   ├── data-dictionary.md             # Complete data dictionary for all tables, CSVs, and parameters
│   ├── trace-tables.md                # 6 required algorithm execution trace tables
│   ├── proofs-and-counterexamples.md
│   ├── pseudocode-and-flowcharts.md
│   ├── collision-statistics-writeup.md
│   ├── scheduling-and-indexing-demonstrations.md
│   ├── ghana-localisation-note.md
│   ├── development-log.md
│   ├── ai-assistance-acknowledgement.md
│   └── graphs/                    # Performance charts (PNG)
│
├── pom.xml                        # Maven build config (Java 21, JUnit 5, PostgreSQL)
└── README.md
```

---

## Data Structures (14 Custom Implementations)

| Structure | File | Key Feature |
|---|---|---|
| DynamicArray | `DynamicArray.java` | 2× geometric resize, O(1) amortized append |
| LinkedList | `LinkedList.java` | Doubly-linked with bidirectional pointer traversal |
| Stack | `Stack.java` | LIFO — backs operator undo/audit log |
| Queue | `Queue.java` | Linked FIFO — first-come-first-served dispatch |
| CircularQueue | `CircularQueue.java` | Ring buffer with modulo wrap-around, O(1) all ops |
| Deque | `Deque.java` | Double-ended — urgent faults jump queue with addFirst |
| PriorityQueue | `PriorityQueue.java` | Binary min-heap with siftUp/siftDown, O(log n) |
| BST | `BST.java` | Insert, search, delete, inorder traversal, height |
| RedBlackTree | `RedBlackTree.java` | Self-balancing with LL/RR/LR/RL rotations & recoloring |
| BTree | `BTree.java` | m-way branching, node splitting, promotes median key |
| HashTable | `HashTable.java` | Separate chaining, load-factor triggered 2× resize |
| CustomSet | `CustomSet.java` | Unique elements backed by HashTable |
| CustomMap | `CustomMap.java` | Key-Value store backed by HashTable |
| DisjointSet | `DisjointSet.java` | Union by Rank + Path Compression |
| Graph | `Graph.java` | Adjacency list + matrix, directed/undirected, weighted |

---

## Algorithms Implemented

| Category | Algorithms |
|---|---|
| Searching | LinearSearch, BinarySearch (precondition: sorted input) |
| Sorting | SelectionSort, InsertionSort, MergeSort, QuickSort |
| Graph Traversal | BFS (FIFO Queue), DFS (recursive + iterative Stack) |
| Shortest Path | Dijkstra (custom PriorityQueue heap, O((V+E)log V)) |
| MST | Prim (edge expansion), Kruskal (DisjointSet cycle detection) |
| Greedy | ActivitySelection (earliest-finish-time interval scheduler) |
| Dynamic Programming | 0/1 Knapsack (bottom-up DP table + traceback reconstruction) |

---

## How to Run

### Prerequisites
- JDK 21 (Eclipse Adoptium Temurin 21 recommended)
- Maven 3.9+ (or use the IDE's built-in Maven)
- PostgreSQL database (optional — CSV fallback is built in)

### 1. Database Setup (optional)
```sql
-- Connect to your PostgreSQL database and run:
\i database/schema.sql
```

Then create `config.properties` in the project root:
```properties
db.url=jdbc:postgresql://<your-host>/<your-db>
db.user=<username>
db.password=<password>
```

### 2. Run All Unit Tests
```bash
mvn test
```

### 3. Run Performance Benchmarks
```bash
mvn compile exec:java -Dexec.mainClass="com.g15.dsa.experiments.Experiments"
```
Results will be written to `data/*.csv`.

### 4. Run the Application
```bash
mvn compile exec:java -Dexec.mainClass="com.g15.dsa.App"
```

---

## Performance Summary

All experiments run 5 times per input size. Reported as average runtime (ms).

### Search & Sort (n = 100 to 10,000)
- **Selection Sort / Insertion Sort:** O(n²) — clearly quadratic growth after n=1,000
- **Merge Sort / QuickSort:** O(n log n) — dramatically faster at large n
- **Binary Search:** O(log n) — near-zero across all tested sizes
- **Linear Search:** O(n) — grows linearly, slower than Binary at all n > 500

Full data: [`data/algorithm_runs.csv`](data/algorithm_runs.csv)  
Graphs: [`reports/graphs/`](reports/graphs/)

---

## Test Coverage

| Test Class | Tests | What is Covered |
|---|---|---|
| DynamicArrayTest | 17 | Normal, boundary, invalid input, auto-grow |
| LinkedListTest | 17 | addFirst/Last, removeFirst/Last, index access, edge cases |
| StackTest | 14 | LIFO order, search, empty stack, clear |
| HashTableTest | 15 | Put, get, collision handling, resize, load factor |
| QueueTest | 12 | FIFO order, enqueue/dequeue, empty, size |
| CircularQueueTest | 12 | Wrap-around, capacity overflow, boundary |
| DequeTest | 13 | Front/rear add/remove, mixed usage |
| PriorityQueueTest | 12 | Insert, extractMin, heap order, large n |
| BFSDFSTest | 11 | Traversal order, reachability, disconnected graph |
| DijkstraTest | 11 | Shortest distance, path reconstruction, negative weight detection |
| GraphAlgorithmsTest | 13 | Prim MST, Kruskal MST, edge weight totals |
| SortingAlgorithmsTest | 20 | All 4 sorts, sorted/reverse/duplicate inputs |
| SearchAlgorithmsTest | 13 | Both searches, precondition violation |
| GreedyAlgorithmsTest | 5 | ActivitySelection, conflict rejection |
| KnapsackTest | 8 | DP table correctness, item reconstruction |
| **Total** | **≥ 70** | |

---

## References

1. Cormen, Leiserson, Rivest & Stein — *Introduction to Algorithms* (MIT Press)
2. Sedgewick & Wayne — *Algorithms* (Princeton University)
3. Goodrich, Tamassia & Goldwasser — *Data Structures and Algorithms in Java*
4. MIT OpenCourseWare: Introduction to Algorithms (6.006)
5. OpenDSA: Data Structures & Algorithms learning materials
6. ECG Ghana — [https://ecgghana.com](https://ecgghana.com) (Dumsor context reference)
