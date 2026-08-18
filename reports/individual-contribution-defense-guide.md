# Individual Contribution & Oral Defense Guide
## Group 15 — Codebility v2.0 | ECG Dumsor Response Optimizer

---

## Team Roster & Defense Reference

| # | Name | Student ID | Course | Assigned Files & Topics | Key Questions & Answers for Oral Defense |
|---|---|---|---|---|---|
| 1 | **Roland Appiah** *(Leader)* | 22197127 | DCIT 308 | `App.java`, `DynamicArray.java`, `LinkedList.java`, `Stack.java` | **Q:** Why double dynamic array size? **A:** Amortized $O(1)$ append cost. **Q:** How does Stack undo work? **A:** LIFO action stack reverses dispatch/assignment changes. |
| 2 | **Jennifer Banibensu** | 22013023 | DCIT 308 | Documentation, Checklist, Technical Report | **Q:** How do components integrate? **A:** DAOs persist state, custom data structures power in-memory dispatch, and algorithms optimize routing and priorities. |
| 3 | **Kingsley Addo** | 22241382 | DCIT 308 | `Queue.java`, `CircularQueue.java`, `Deque.java`, `PriorityQueue.java` | **Q:** Difference between CircularQueue and standard Queue? **A:** CircularQueue uses modulo ring buffer to avoid element shifting. **Q:** Min-Heap siftDown logic? **A:** Compares parent with smallest child and swaps until heap order is restored. |
| 4 | **Dennis Kwaku Dapaah Adomako** | 22238484 | DCIT 308 | `BST.java`, `RedBlackTree.java`, `BTree.java` | **Q:** Why use Red-Black Tree over BST? **A:** Guaranteed $O(\log n)$ height prevents worst-case $O(n)$ degeneration under sequential inputs. **Q:** B-Tree split? **A:** Median key promoted to parent. |
| 5 | **Patricia Gyan** | 22141938 | DCIT 308 | `HashTable.java`, `CustomSet.java`, `CustomMap.java`, `DisjointSet.java`, `Graph.java` | **Q:** How are collisions handled? **A:** Separate chaining with linked buckets. Rehashes to $2\times$ size when load factor exceeds 0.75. **Q:** Path compression? **A:** Flattens tree during `find()`. |
| 6 | **Angel Francisca Echesi** | 22398675 | DCIT 204 | `LinearSearch.java`, `BinarySearch.java`, `SelectionSort.java`, `InsertionSort.java`, `MergeSort.java`, `QuickSort.java` | **Q:** Why does Binary Search fail on unsorted arrays? **A:** Violates monotonicity assumption. **Q:** Selection Sort invariant? **A:** Prefix $arr[0..i-1]$ holds sorted smallest $i$ elements. |
| 7 | **Daniel Kwadwo Takyi** | 22390064 | DCIT 204 | `BFS.java`, `DFS.java` | **Q:** Difference in traversal strategy? **A:** BFS uses FIFO Queue (level-by-level shortest hops); DFS uses LIFO/Recursion (deep path exploration). |
| 8 | **Anasthasia Koduah Tweneboah** | 22311176 | DCIT 204 | `Dijkstra.java` | **Q:** Why does Dijkstra require non-negative weights? **A:** Greedy assumption that shortest known distance cannot be reduced by subsequent positive edges. |
| 9 | **Hafisah Ibrahim** | 22381877 | DCIT 204 | `Prim.java`, `Kruskal.java` | **Q:** Prim vs Kruskal? **A:** Prim grows tree vertex-by-vertex using minimum cut edge; Kruskal sorts all edges globally and uses DisjointSet to prevent cycles. |
| 10 | **Joel Kissiedu Amissah** | 22368505 | DCIT 204 | `ActivitySelection.java` | **Q:** Greedy choice property? **A:** Selecting activity with earliest finish time leaves maximum time remaining for subsequent compatible activities. |
| 11 | **Shadrach Addoquaye Addo** | 22396810 | DCIT 204 | `Knapsack.java` | **Q:** Optimal substructure in 0/1 Knapsack? **A:** $DP[i][w] = \max(DP[i-1][w], DP[i-1][w-w_i] + v_i)$. Solution reconstructed by backtracking from $DP[n][W]$. |
| 12 | **Michelle Nana Abena Asantewaa Sarfo** | 22396802 | DCIT 204 | `TeamParameters.java`, `DatabaseConnection.java` | **Q:** How are parameters derived? **A:** From student index `22396802`: Urgency weight = 1.4, Road penalty = 1.2, Hash capacity = 103 (prime), Hash seed = 6802. |
| 13 | **Sampson Menum Landokidow** | 22300655 | DCIT 204 | `LocationDAO.java`, `ResourceDAO.java`, Seed Datasets | **Q:** Structure of Legon dataset? **A:** 50 ECG substations, 100 feeder roads, 30 repair units, 300 outage records with Accra/Legon GIS coordinates. |
| 14 | **Samuel Peter Peter** | 22410937 | DCIT 204 | `Experiments.java`, `AlgorithmRunDAO.java`, `FaultDAO.java` | **Q:** Empirical vs theoretical complexity? **A:** Verified quadratic growth for $O(n^2)$ Selection/Insertion sort and log-linear growth for $O(n \log n)$ Merge/Quick sort across 540 runs. |

---

## Live Modification Guide (Ready for Examination)

Every team member can demonstrate code modifications during the oral exam:
1. **Change Priority Queue order:** In `Fault.java`, swap `Integer.compare(other.urgency, this.urgency)` to reverse priority order.
2. **Adjust Hash Table sizing:** In `TeamParameters.java`, change `HASH_CAPACITY` to any prime number.
3. **Change Dijkstra starting node:** In `App.java` or `GraphService.java`, pass any vertex index $0 \dots |V|-1$.
4. **Add new ECG substation:** Append row to `data/locations.csv` and add connected edge in `data/roads.csv`.
