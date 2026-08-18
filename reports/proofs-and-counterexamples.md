# Proof Sketches & Counterexamples
## Group 15 — Codebility v2.0 | ECG Dumsor Response Optimizer

---

## Proof Sketch 1: Loop Invariant for Selection Sort

**Claim:** After pass `i`, the sub-array `arr[0..i-1]` contains the `i` smallest elements of the original array, in sorted order.

**Loop Invariant:** At the start of iteration `i` (0-indexed), `arr[0..i-1]` is sorted and contains the `i` minimum elements.

**Initialization (i = 0):** The empty prefix trivially satisfies the invariant — zero elements are trivially sorted.

**Maintenance:** At the start of iteration `i`, we scan `arr[i..n-1]` to find the minimum element and swap it with `arr[i]`. After the swap, `arr[i]` holds the smallest element of the remaining unsorted portion. Since all elements in `arr[0..i-1]` are smaller (by the invariant) and `arr[i]` is the smallest of `arr[i..n-1]`, the prefix `arr[0..i]` is sorted. The invariant is maintained.

**Termination:** When `i = n - 1`, the invariant gives us `arr[0..n-2]` sorted with the `n-1` smallest elements. The last element `arr[n-1]` must be the maximum. The entire array is therefore sorted. ∎

**Complexity:** O(n²) comparisons in all cases — the nested scan always runs the full remaining range regardless of input order.

---

## Proof Sketch 2: Dijkstra Shortest Path Correctness (Induction on visited set S)

**Claim:** When a vertex `v` is extracted from the priority queue and added to the visited set `S`, `dist[v]` is the true shortest path distance from source `s` to `v`.

**Base case (|S| = 0 → add `s`):** `dist[s] = 0` is trivially the shortest path from `s` to itself. Any other path has positive weight (non-negative edge weights), so 0 is optimal.

**Inductive step:** Assume that for all `u ∈ S`, `dist[u]` is the correct shortest distance. Let `v` be the next vertex extracted (minimum tentative distance). Suppose for contradiction that a shorter path `P` to `v` exists with actual cost `d < dist[v]`. This path must leave `S` at some point — let `(x, y)` be the first edge crossing from `S` to `V\S` along `P`. Since all edge weights are ≥ 0, the path from `y` to `v` has non-negative cost, so `dist[x] + w(x,y) ≤ d < dist[v]`. But `dist[x] + w(x,y)` was already computed when `x` was visited and would have been set as `dist[y]`. Since the priority queue is a min-heap, `y` would have been extracted before `v` — contradiction with `v` being the current minimum. ∎

**Precondition (explicitly stated in code):** All edge weights must be ≥ 0. Our `Dijkstra.java` throws `IllegalArgumentException` if a negative edge weight is detected during relaxation.

---

## Proof Sketch 3: Greedy Exchange Argument for Activity Selection

**Claim:** The greedy algorithm that always selects the compatible activity with the earliest finish time produces an optimal (maximum cardinality) set of activities.

**Greedy Choice Property:** Consider the optimal solution `OPT`. Let `g1` be the activity selected by the greedy algorithm (earliest finish time). If `OPT` does not include `g1`, take any activity `o1 ∈ OPT` scheduled first. Since `g1` has the earliest finish time across all activities, `finish(g1) ≤ finish(o1)`. Replace `o1` with `g1` in `OPT` — since `g1` finishes no later than `o1`, no activity compatible with `o1` is now incompatible with `g1`. The modified solution has the same cardinality. Therefore, there exists an optimal solution that includes the greedy choice `g1`.

**Optimal Substructure:** After selecting `g1` and removing all activities incompatible with it, the remaining problem is: choose a maximum set of compatible activities from the reduced list. This is the same problem on a smaller instance, so the structure repeats recursively.

By induction, the greedy algorithm produces a globally optimal selection. ∎

**DP Optimal Substructure (Knapsack):** For the 0/1 Knapsack, define `DP[i][w]` as the maximum value using items `1..i` with capacity `w`. Either item `i` is included (`DP[i-1][w - weight_i] + value_i`) or excluded (`DP[i-1][w]`). The recurrence takes the maximum of these two. Correctness follows because every optimal solution to the weight-`w` problem with items `1..i` either includes or excludes item `i`, and the sub-solutions are themselves optimal (optimal substructure). ∎

---

## Counterexample 1: Greedy Coin Change Failure

**Denominations:** {1, 3, 4}. **Target sum:** 6.

**Greedy strategy (always pick largest coin ≤ remaining):**
- Pick 4 → remaining: 2
- Pick 1 → remaining: 1
- Pick 1 → remaining: 0
- **Result: 4 + 1 + 1 = 6, using 3 coins.**

**Optimal solution:**
- Pick 3 → remaining: 3
- Pick 3 → remaining: 0
- **Result: 3 + 3 = 6, using only 2 coins.**

**Conclusion:** The greedy "largest-first" strategy fails because local optimality does not guarantee global optimality. Dynamic Programming is required for the general Coin Change problem.

---

## Counterexample 2: Invalid Precondition — Binary Search on Unsorted Array

**Precondition (required):** The input array must be sorted in ascending order.

**Array (unsorted):** `[42, 7, 25, 10, 18]`  
**Target:** `10`  
**Correct answer:** index 3.

**Binary Search trace (incorrectly applied to unsorted array):**

| Step | Left | Right | Mid | arr[mid] | Decision |
|---|---|---|---|---|---|
| 1 | 0 | 4 | 2 | 25 | 10 < 25 → search left |
| 2 | 0 | 1 | 0 | 42 | 10 < 42 → search left |
| 3 | 0 | -1 | — | — | Left > Right → **NOT FOUND** |

**Result:** Binary Search incorrectly reports that 10 is not in the array, when it is at index 3.

**Conclusion:** Without the sorted-order precondition, Binary Search produces incorrect results. Our `BinarySearch.java` documents this precondition in its JavaDoc. It is the caller's responsibility to sort the array first (e.g., with MergeSort or QuickSort) before invoking binary search.
