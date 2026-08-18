# Comprehensive Algorithm Trace Tables
## Group 15 — Codebility v2.0 | ECG Dumsor Response Optimizer
**Courses:** DCIT 204 (Data Structures & Algorithms I) & DCIT 308 (Data Structures & Algorithms II)  
**University:** University of Ghana, Legon — Department of Computer Science  
**Student Parameter Reference:** Michelle Nana Abena Asantewaa Sarfo (ID: `22396802`)

---

## Overview & Index of Trace Tables

This document provides rigorous, step-by-step execution traces and state transition tables for all six core algorithm categories required by the project brief:

1. [Trace Table 1: Binary Search (DCIT 204 — Searching)](#1-binary-search-trace-table)
2. [Trace Table 2: Insertion Sort (DCIT 204 — Sorting)](#2-insertion-sort-trace-table)
3. [Trace Table 3: QuickSort & MergeSort (DCIT 204 — Divide & Conquer)](#3-quicksort--mergesort-trace-tables)
4. [Trace Table 4: Dijkstra Shortest Path (DCIT 204 — Greedy Graph Routing)](#4-dijkstra-shortest-path-trace-table)
5. [Trace Table 5: Kruskal & Prim Minimum Spanning Tree (DCIT 204 — Greedy MST)](#5-kruskal--prim-minimum-spanning-tree-trace-tables)
6. [Trace Table 6: 0/1 Knapsack Dynamic Programming Table (DCIT 204 — DP Truck Optimization)](#6-01-knapsack-dynamic-programming-trace-table)

---

## 1. Binary Search Trace Table

### Problem Specification
* **Precondition:** Input array $A[0 \dots n-1]$ is monotonically sorted in ascending order ($A[i] \le A[i+1]$).
* **Dataset:** Sorted ECG Outage Report IDs: $A = [12, 24, 38, 45, 59, 72, 85, 99]$ ($n = 8$).
* **Target Key:** $T = 72$.
* **Algorithm Invariant:** If $T \in A$, then $T \in A[\text{low} \dots \text{high}]$.
* **Midpoint Formula:** $\text{mid} = \text{low} + \lfloor(\text{high} - \text{low}) / 2\rfloor$.

### Step-by-Step Execution Trace

| Iteration | `low` | `high` | `mid` | $A[\text{mid}]$ | Comparison ($T$ vs $A[\text{mid}]$) | Action Taken | Search Space Remaining |
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| **Init** | `0` | `7` | — | — | — | Initial range $[0 \dots 7]$ | $[12, 24, 38, 45, 59, 72, 85, 99]$ |
| **Pass 1** | `0` | `7` | `3` | `45` | $72 > 45$ | $T > A[3] \implies \text{low} = \text{mid} + 1 = 4$ | $[59, 72, 85, 99]$ |
| **Pass 2** | `4` | `7` | `5` | `72` | $72 == 72$ | **Match Found!** Return index `5` | $[72]$ |

### Complexity & Analysis
* **Comparisons:** 2 iterations.
* **Return Value:** Index `5` ($A[5] = 72$).
* **Time Complexity:** $O(\log n)$ worst/average case; space complexity: $O(1)$ auxiliary.

---

## 2. Insertion Sort Trace Table

### Problem Specification
* **Input Array:** Substation Emergency Priority Queue IDs: $A = [68, 22, 96, 39, 14, 80]$ ($n = 6$).
* **Loop Invariant:** At the start of iteration $i$, prefix $A[0 \dots i-1]$ consists of the original elements of $A[0 \dots i-1]$ but in fully sorted order.
* **Inner Loop:** Shifts elements greater than $\text{key} = A[i]$ one position to the right.

### Step-by-Step Execution Trace

| Pass ($i$) | Key ($A[i]$) | Comparisons & Inner Shifts ($j = i-1 \dots 0$) | Array State After Pass | Sorted Prefix $A[0 \dots i]$ |
|:---:|:---:|:---|:---|:---|
| **0 (Init)** | — | Initial unsorted array | `[68, 22, 96, 39, 14, 80]` | `[68]` |
| **Pass 1 ($i=1$)** | `22` | Compare $22 < 68 \implies$ shift $68 \to A[1]$; insert $22 \to A[0]$ | `[22, 68, 96, 39, 14, 80]` | `[22, 68]` |
| **Pass 2 ($i=2$)** | `96` | Compare $96 > 68 \implies 0$ shifts; insert $96 \to A[2]$ | `[22, 68, 96, 39, 14, 80]` | `[22, 68, 96]` |
| **Pass 3 ($i=3$)** | `39` | Compare $39 < 96 \implies$ shift $96$; $39 < 68 \implies$ shift $68$; $39 > 22 \implies$ stop; insert $39 \to A[1]$ | `[22, 39, 68, 96, 14, 80]` | `[22, 39, 68, 96]` |
| **Pass 4 ($i=4$)** | `14` | Compare $14 < 96, 68, 39, 22 \implies$ shift all 4 elements; insert $14 \to A[0]$ | `[14, 22, 39, 68, 96, 80]` | `[14, 22, 39, 68, 96]` |
| **Pass 5 ($i=5$)** | `80` | Compare $80 < 96 \implies$ shift $96$; $80 > 68 \implies$ stop; insert $80 \to A[4]$ | `[14, 22, 39, 68, 80, 96]` | `[14, 22, 39, 68, 80, 96]` |

### Summary Metrics
* **Total Key Comparisons:** $1 + 1 + 3 + 4 + 2 = 11$.
* **Total Shifts:** $1 + 0 + 2 + 4 + 1 = 8$.
* **Final Sorted Array:** `[14, 22, 39, 68, 80, 96]`.

---

## 3. QuickSort & MergeSort Trace Tables

### 3.1 QuickSort Partition Trace (Lomuto Partition Scheme)
* **Input Subarray:** $A = [45, 12, 85, 32, 89, 39, 69]$ with $\text{low} = 0, \text{high} = 6$.
* **Pivot Selection:** Last element $P = A[6] = 69$.
* **Partition Invariant:** Elements in $A[\text{low} \dots i] \le P$ and elements in $A[i+1 \dots j-1] > P$.

| Step ($j$) | $A[j]$ | Comparison ($A[j] \le 69$) | Pointer $i$ | Swap Executed | Array State $A[0 \dots 6]$ |
|:---:|:---:|:---:|:---:|:---:|:---|
| **Init** | — | — | `-1` | None | `[45, 12, 85, 32, 89, 39 | 69]` |
| $j=0$ | `45` | $45 \le 69$ (True) | `0` | Swap $A[0] \leftrightarrow A[0]$ (`45` with `45`) | `[45, 12, 85, 32, 89, 39 | 69]` |
| $j=1$ | `12` | $12 \le 69$ (True) | `1` | Swap $A[1] \leftrightarrow A[1]$ (`12` with `12`) | `[45, 12, 85, 32, 89, 39 | 69]` |
| $j=2$ | `85` | $85 \le 69$ (False) | `1` | No swap | `[45, 12, 85, 32, 89, 39 | 69]` |
| $j=3$ | `32` | $32 \le 69$ (True) | `2` | Swap $A[2] \leftrightarrow A[3]$ (`85` with `32`) | `[45, 12, 32, 85, 89, 39 | 69]` |
| $j=4$ | `89` | $89 \le 69$ (False) | `2` | No swap | `[45, 12, 32, 85, 89, 39 | 69]` |
| $j=5$ | `39` | $39 \le 69$ (True) | `3` | Swap $A[3] \leftrightarrow A[5]$ (`85` with `39`) | `[45, 12, 32, 39, 89, 85 | 69]` |
| **Final** | Pivot | End of loop | `4` | Swap $A[i+1] \leftrightarrow A[\text{high}]$ (`89` with `69`) | `[45, 12, 32, 39, 69, 85, 89]` |

* **Pivot Index Returned:** $4$ (Value: `69`).
* **Sub-problems Created:** Left: $A[0 \dots 3] = [45, 12, 32, 39]$; Right: $A[5 \dots 6] = [85, 89]$.

---

### 3.2 MergeSort 2-Way Merge Trace
* **Merging Subarrays:** Left $L = [14, 68, 96]$, Right $R = [22, 39, 80]$.
* **Pointers:** $i = 0$ (Left), $j = 0$ (Right), $k = 0$ (Merged Output $M$).

| Step ($k$) | Left Pointer $i$ ($L[i]$) | Right Pointer $j$ ($R[j]$) | Comparison ($L[i] \le R[j]$) | Element Placed in $M[k]$ | Pointers Incremented | Merged Array $M[0 \dots k]$ |
|:---:|:---:|:---:|:---:|:---:|:---:|:---|
| **$k=0$** | $i=0$ (`14`) | $j=0$ (`22`) | $14 \le 22$ (True) | `14` | $i \to 1, k \to 1$ | `[14]` |
| **$k=1$** | $i=1$ (`68`) | $j=0$ (`22`) | $68 \le 22$ (False) | `22` | $j \to 1, k \to 2$ | `[14, 22]` |
| **$k=2$** | $i=1$ (`68`) | $j=1$ (`39`) | $68 \le 39$ (False) | `39` | $j \to 2, k \to 3$ | `[14, 22, 39]` |
| **$k=3$** | $i=1$ (`68`) | $j=2$ (`80`) | $68 \le 80$ (True) | `68` | $i \to 2, k \to 4$ | `[14, 22, 39, 68]` |
| **$k=4$** | $i=2$ (`96`) | $j=2$ (`80`) | $96 \le 80$ (False) | `80` | $j \to 3 (\text{End}), k \to 5$ | `[14, 22, 39, 68, 80]` |
| **$k=5$** | $i=2$ (`96`) | Exhausted | Copy remaining left | `96` | $i \to 3, k \to 6$ | `[14, 22, 39, 68, 80, 96]` |

---

## 4. Dijkstra Shortest Path Trace Table

### Network Topography (ECG Accra / Legon Grid)
* **Vertices ($V = 5$):**
  * `0`: Achimota Substation (Source)
  * `1`: Legon Campus (UG)
  * `2`: East Legon Feeder
  * `3`: Airport Hills Relay
  * `4`: Cantonments Hub
* **Directed Weighted Edges ($E = 8$):**
  * $(0 \to 1, 3.2\text{ km})$, $(0 \to 2, 5.7\text{ km})$, $(0 \to 3, 7.5\text{ km})$
  * $(1 \to 0, 3.2\text{ km})$, $(1 \to 2, 2.1\text{ km})$, $(1 \to 4, 6.8\text{ km})$
  * $(2 \to 1, 2.1\text{ km})$, $(2 \to 3, 3.4\text{ km})$
  * $(3 \to 4, 4.1\text{ km})$, $(4 \to 0, 8.0\text{ km})$

### Step-by-Step Priority Queue & Relaxation Trace

* **Initial State:** $dist = [0.0, \infty, \infty, \infty, \infty]$, $pred = [-1, -1, -1, -1, -1]$, $S = \emptyset$.
* $PQ = \{(0, 0.0)\}$.

| Step | Extracted $(u, dist[u])$ | Visited Set $S$ | Relaxed Edge $(u \to v, w)$ | Tentative Distance Check | Update Triggered | Tentative $dist$ Vector $[0, 1, 2, 3, 4]$ | Predecessor $pred$ $[0, 1, 2, 3, 4]$ | Priority Queue State ($PQ$) |
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---|
| **0 (Init)** | — | $\emptyset$ | — | — | — | `[0.0, ∞, ∞, ∞, ∞]` | `[-1, -1, -1, -1, -1]` | `{(0, 0.0)}` |
| **1** | `(0, 0.0)` | $\{0\}$ | $(0 \to 1, 3.2)$<br>$(0 \to 2, 5.7)$<br>$(0 \to 3, 7.5)$ | $0+3.2 < \infty$<br>$0+5.7 < \infty$<br>$0+7.5 < \infty$ | Yes ($dist[1]=3.2$)<br>Yes ($dist[2]=5.7$)<br>Yes ($dist[3]=7.5$) | `[0.0, 3.2, 5.7, 7.5, ∞]` | `[-1, 0, 0, 0, -1]` | `{(1, 3.2), (2, 5.7), (3, 7.5)}` |
| **2** | `(1, 3.2)` | $\{0, 1\}$ | $(1 \to 0, 3.2)$<br>$(1 \to 2, 2.1)$<br>$(1 \to 4, 6.8)$ | $3.2+3.2 \ge 0.0$<br>$3.2+2.1=5.3 < 5.7$<br>$3.2+6.8=10.0 < \infty$ | Ignored (visited)<br>**Yes** ($dist[2]=5.3$)<br>Yes ($dist[4]=10.0$) | `[0.0, 3.2, 5.3, 7.5, 10.0]` | `[-1, 0, 1, 0, 1]` | `{(2, 5.3), (3, 7.5), (4, 10.0)}` |
| **3** | `(2, 5.3)` | $\{0, 1, 2\}$ | $(2 \to 1, 2.1)$<br>$(2 \to 3, 3.4)$ | $5.3+2.1 \ge 3.2$<br>$5.3+3.4=8.7 \ge 7.5$ | Ignored (visited)<br>No ($8.7 \ge 7.5$) | `[0.0, 3.2, 5.3, 7.5, 10.0]` | `[-1, 0, 1, 0, 1]` | `{(3, 7.5), (4, 10.0)}` |
| **4** | `(3, 7.5)` | $\{0, 1, 2, 3\}$ | $(3 \to 4, 4.1)$ | $7.5+4.1=11.6 \ge 10.0$ | No ($11.6 \ge 10.0$) | `[0.0, 3.2, 5.3, 7.5, 10.0]` | `[-1, 0, 1, 0, 1]` | `{(4, 10.0)}` |
| **5** | `(4, 10.0)` | $\{0, 1, 2, 3, 4\}$ | $(4 \to 0, 8.0)$ | $10.0+8.0 \ge 0.0$ | Ignored (visited) | `[0.0, 3.2, 5.3, 7.5, 10.0]` | `[-1, 0, 1, 0, 1]` | $\emptyset$ (Terminated) |

### Reconstructed Shortest Paths from Node 0 (Achimota Substation)
* **To Node 1 (Legon Campus):** Path: $0 \to 1$ | **Distance:** $3.2\text{ km}$ (Effective: $3.2 \times 1.2 = 3.84\text{ km}$)
* **To Node 2 (East Legon):** Path: $0 \to 1 \to 2$ | **Distance:** $5.3\text{ km}$ (Effective: $5.3 \times 1.2 = 6.36\text{ km}$)
* **To Node 3 (Airport Hills):** Path: $0 \to 3$ | **Distance:** $7.5\text{ km}$ (Effective: $7.5 \times 1.2 = 9.00\text{ km}$)
* **To Node 4 (Cantonments):** Path: $0 \to 1 \to 4$ | **Distance:** $10.0\text{ km}$ (Effective: $10.0 \times 1.2 = 12.00\text{ km}$)

---

## 5. Kruskal & Prim Minimum Spanning Tree Trace Tables

### 5.1 Kruskal's Algorithm Trace Table
* **Network Nodes ($V = 5$):** $0$: Achimota, $1$: Legon, $2$: East Legon, $3$: Madina, $4$: Adenta.
* **Candidate Feeder Edges (Sorted Ascending by Length):**
  1. $(2, 3, 1.0\text{ km})$
  2. $(1, 2, 2.0\text{ km})$
  3. $(3, 4, 3.0\text{ km})$
  4. $(0, 1, 4.0\text{ km})$
  5. $(1, 3, 5.0\text{ km})$
  6. $(0, 2, 8.0\text{ km})$
* **DisjointSet Initial Components:** $\{0\}, \{1\}, \{2\}, \{3\}, \{4\}$. Target MST Edge Count: $|V| - 1 = 4$.

| Iteration | Edge $(u, v)$ | Weight ($w$) | `find(u)` | `find(v)` | Cycle Condition (`find(u) == find(v)`) | Action Taken | Connected Disjoint Components | Cumulative MST Cost |
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---|:---:|
| **1** | $(2, 3)$ | `1.0 km` | `2` | `3` | $2 \ne 3$ (False) | **ACCEPTED** (Union 2, 3) | $\{0\}, \{1\}, \{2, 3\}, \{4\}$ | `1.0 km` |
| **2** | $(1, 2)$ | `2.0 km` | `1` | `2` | $1 \ne 2$ (False) | **ACCEPTED** (Union 1, 2) | $\{0\}, \{1, 2, 3\}, \{4\}$ | `3.0 km` |
| **3** | $(3, 4)$ | `3.0 km` | `1` | `4` | $1 \ne 4$ (False) | **ACCEPTED** (Union 3, 4) | $\{0\}, \{1, 2, 3, 4\}$ | `6.0 km` |
| **4** | $(0, 1)$ | `4.0 km` | `0` | `1` | $0 \ne 1$ (False) | **ACCEPTED** (Union 0, 1) | $\{0, 1, 2, 3, 4\}$ | **`10.0 km`** |
| **5** | $(1, 3)$ | `5.0 km` | `0` | `0` | $0 == 0$ (**True**) | **REJECTED (Cycle: $1-2-3-1$)** | $\{0, 1, 2, 3, 4\}$ | `10.0 km` |
| **6** | $(0, 2)$ | `8.0 km` | `0` | `0` | $0 == 0$ (**True**) | **REJECTED (Cycle: $0-1-2-0$)** | $\{0, 1, 2, 3, 4\}$ | `10.0 km` |

* **Final Minimum Spanning Tree:** Edges: $\{(2,3), (1,2), (3,4), (0,1)\}$ | **Total Minimum Cable Length:** **`10.0 km`**.

---

### 5.2 Prim's Algorithm Trace Table
* **Start Node:** Vertex `0` (Achimota Substation).
* **Cut Set Growth:**

| Step | Visited Cut $S$ | Cut Edges Available in PriorityQueue | Minimum Cut Edge Picked | Added Vertex | Cumulative MST Weight |
|:---:|:---|:---|:---:|:---:|:---:|
| **1** | $\{0\}$ | $(0,1, 4.0), (0,2, 8.0)$ | $(0,1, 4.0)$ | `1` | `4.0 km` |
| **2** | $\{0, 1\}$ | $(1,2, 2.0), (1,3, 5.0), (0,2, 8.0)$ | $(1,2, 2.0)$ | `2` | `6.0 km` |
| **3** | $\{0, 1, 2\}$ | $(2,3, 1.0), (1,3, 5.0), (0,2, 8.0)$ | $(2,3, 1.0)$ | `3` | `7.0 km` |
| **4** | $\{0, 1, 2, 3\}$ | $(3,4, 3.0), (1,3, 5.0), (0,2, 8.0)$ | $(3,4, 3.0)$ | `4` | **`10.0 km`** |

* **Invariant Verified:** Both Kruskal and Prim produce identical optimal cost (**`10.0 km`**).

---

## 6. 0/1 Knapsack Dynamic Programming Trace Table

### Problem Specification
* **ECG Emergency Repair Truck Weight Capacity:** $W = 50\text{ kg}$.
* **Available Equipment Items ($n = 5$):**
  1. **Item 1 ($I_1$):** Transformer Coil Pack — Weight $w_1 = 15\text{ kg}$, Value $v_1 = 80$
  2. **Item 2 ($I_2$):** Safety Harness & Insulators — Weight $w_2 = 10\text{ kg}$, Value $v_2 = 50$
  3. **Item 3 ($I_3$):** Underground Cable Splicer — Weight $w_3 = 20\text{ kg}$, Value $v_3 = 100$
  4. **Item 4 ($I_4$):** Heavy Duty Hydraulic Jack — Weight $w_4 = 25\text{ kg}$, Value $v_4 = 120$
  5. **Item 5 ($I_5$):** Digital Multimeter Kit — Weight $w_5 = 8\text{ kg}$, Value $v_5 = 40$

* **DP Recurrence Relation:**
  $$DP[i][w] = \begin{cases}
  DP[i-1][w] & \text{if } w_i > w \\
  \max(DP[i-1][w], DP[i-1][w - w_i] + v_i) & \text{if } w_i \le w
  \end{cases}$$

---

### Complete 2D Dynamic Programming Table ($DP[i][w]$)

| Item $i$ / Weight $w$ | $0\text{ kg}$ | $10\text{ kg}$ | $15\text{ kg}$ | $20\text{ kg}$ | $25\text{ kg}$ | $30\text{ kg}$ | $35\text{ kg}$ | $40\text{ kg}$ | $45\text{ kg}$ | $50\text{ kg}$ |
|:---|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| **$0$ (No Items)** | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| **$I_1$ ($w=15, v=80$)** | 0 | 0 | 80 | 80 | 80 | 80 | 80 | 80 | 80 | 80 |
| **$I_2$ ($w=10, v=50$)** | 0 | 50 | 80 | 80 | 130 | 130 | 130 | 130 | 130 | 130 |
| **$I_3$ ($w=20, v=100$)**| 0 | 50 | 80 | 100 | 130 | 150 | 180 | 180 | 230 | 230 |
| **$I_4$ ($w=25, v=120$)**| 0 | 50 | 80 | 100 | 130 | 150 | 180 | 200 | 230 | **250** |
| **$I_5$ ($w=8, v=40$)**  | 0 | 50 | 80 | 100 | 130 | 150 | 180 | 200 | 230 | **260** |

---

### Step-by-Step Backtracking Reconstruction (Path from $DP[5][50] = 260$)

1. **At $(i=5, w=50)$:**
   * $DP[5][50] = 260 \ne DP[4][50] = 250 \implies$ **Item 5 ($I_5$: Multimeter) was SELECTED**.
   * Remaining capacity: $w = 50 - 8 = 42\text{ kg}$.

2. **At $(i=4, w=42)$:**
   * $DP[4][42] = 200$ and $DP[3][42] = 180$.
   * Since $DP[4][42] \ne DP[3][42] \implies$ **Item 4 ($I_4$: Hydraulic Jack) was SELECTED**.
   * Remaining capacity: $w = 42 - 25 = 17\text{ kg}$.

3. **At $(i=3, w=17)$:**
   * $w_3 = 20 > 17 \implies$ Item 3 could not fit ($DP[3][17] = DP[2][17] = 80$).
   * **Item 3 ($I_3$) was NOT SELECTED**.
   * Remaining capacity: $w = 17\text{ kg}$.

4. **At $(i=2, w=17)$:**
   * $DP[2][17] = 80$ and $DP[1][17] = 80$.
   * Since $DP[2][17] == DP[1][17]$ (and including $I_2$ yields $DP[1][7] + 50 = 50 < 80$), **Item 2 ($I_2$) was NOT SELECTED**.
   * Remaining capacity: $w = 17\text{ kg}$.

5. **At $(i=1, w=17)$:**
   * $DP[1][17] = 80 \ne DP[0][17] = 0 \implies$ **Item 1 ($I_1$: Transformer Coil Pack) was SELECTED**.
   * Remaining capacity: $w = 17 - 15 = 2\text{ kg}$.

6. **At $(i=0, w=2)$:** Base case reached.

---

### Optimal Packing Solution Summary

| Selected Item | Equipment Name | Weight ($w_i$) | Repair Value ($v_i$) | Cumulative Weight | Cumulative Value |
|:---:|:---|:---:|:---:|:---:|:---:|
| $I_1$ | Transformer Coil Pack | $15\text{ kg}$ | 80 | $15\text{ kg}$ | 80 |
| $I_4$ | Heavy Duty Hydraulic Jack | $25\text{ kg}$ | 120 | $40\text{ kg}$ | 200 |
| $I_5$ | Digital Multimeter Kit | $8\text{ kg}$ | 40 | **$48\text{ kg}$** | **260** |

* **Total Weight Packed:** **$48\text{ kg} \le 50\text{ kg}$** (Truck Capacity satisfied).
* **Maximum Emergency Value Achieved:** **`260`**.
