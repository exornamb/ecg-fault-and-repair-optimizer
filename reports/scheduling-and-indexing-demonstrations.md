# Scheduling & Indexing Demonstrations
## Group 15 — Codebility v2.0 | ECG Dumsor Response Optimizer

---

## 1. Queue Scheduling Strategy Comparison

In the ECG Dumsor Response Optimizer, incoming outage and service requests are dispatched to field crews using four distinct queuing strategies:

| Strategy | Data Structure | Dispatch Policy | ECG Application Scenario |
|---|---|---|---|
| **FIFO Queue** | `Queue.java` (Linked FIFO) | First-Come-First-Served ($O(1)$) | Routine customer meter replacements and scheduled maintenance. |
| **Circular Queue** | `CircularQueue.java` (Array Ring Buffer) | Bounded Round-Robin Dispatch ($O(1)$) | Rotating available dispatch slots across active district repair bases. |
| **Deque** | `Deque.java` (Double-Ended) | Mixed Priority Injection ($O(1)$) | Urgent requests jump to the head via `addFirst()`; standard requests append via `addLast()`. |
| **Priority Queue** | `PriorityQueue.java` (Binary Min-Heap) | Urgency-Driven Dispatch ($O(\log n)$) | Critical hospital/substation blackouts (Urgency 5) always dispatched ahead of Urgency 1–4. |

### Step-by-Step Dispatch Trace:
1. **Initial State:** Outage requests arrive from Legon Hall (Urgency 3), UG Hospital (Urgency 5), and Commonwealth Hall (Urgency 4).
2. **Under FIFO:** Dispatched in arrival order: Legon Hall $\rightarrow$ UG Hospital $\rightarrow$ Commonwealth Hall.
3. **Under PriorityQueue (Heap):** UG Hospital (Urgency 5) floats to heap root via `siftUp` and is extracted first, followed by Commonwealth Hall (Urgency 4), ensuring critical healthcare infrastructure is restored immediately.

---

## 2. Tree Indexing Demonstrations

### 2.1 Binary Search Tree (BST) vs Red-Black Tree
When fault IDs or timestamps arrive in near-sorted order (e.g., sequential IDs `SR-001`, `SR-002`, `SR-003`, ...):
- **Unbalanced BST:** Degenerates into an $O(n)$ linear linked list, resulting in tree height equal to $n$.
- **Red-Black Tree:** Automatically applies left/right rotations and node recoloring (red/black properties) to guarantee that tree height never exceeds $2 \lfloor \log_2(n+1) \rfloor$, keeping search operations strictly $O(\log n)$.

```
[Red-Black Tree Balance Demonstration on Sorted Sequence: 10, 20, 30]

   Unbalanced BST:                Red-Black Tree (after Left-Rotate on 10):
        10 (Root)                              20 (Black Root)
          \                                   /  \
           20                               10    30  (Red Leaves)
             \
              30  (Height = 3)                     (Height = 2)
```

### 2.2 B-Tree Multi-Way Indexing
For high-volume persistent storage indexing (simulating database index pages):
- Nodes have a minimum degree $t \ge 2$.
- When a node fills with $2t - 1$ keys, inserting an additional key triggers a split: the median key is promoted to the parent node, and the remaining keys form two sibling child nodes.
- This guarantees shallow tree depth ($O(\log_t n)$) and minimal disk/memory page traversals.

---

## 3. DisjointSet & Kruskal Minimum Spanning Tree Demonstration

For reconnecting the Legon/Accra power distribution grid with minimal total feeder cable length:
1. All candidate feeder line edges are sorted by weight in ascending order.
2. `DisjointSet` maintains connected components of substations using **Union by Rank** and **Path Compression** ($O(\alpha(V))$ amortized).
3. For each edge $(u, v)$, `find(u)` and `find(v)` are evaluated:
   - If `find(u) == find(v)`: The edge would form an electrical feedback loop / cycle $\rightarrow$ **REJECTED**.
   - If `find(u) != find(v)`: The edge safely bridges two separate island grids $\rightarrow$ **ACCEPTED** and `union(u, v)` is invoked.
4. Process terminates once $|V| - 1$ edges are accepted, producing the globally minimal connected network.
