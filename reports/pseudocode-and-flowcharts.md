# Pseudocode & Flowcharts
## Group 15 — Codebility v2.0 | ECG Dumsor Response Optimizer

---

## Pseudocode 1: Priority Dispatch — Heap-Based Fault Assignment

```
PROCEDURE DispatchFaults(faultQueue: PriorityQueue, crews: List<Crew>)
  PRECONDITION: faultQueue is non-empty; at least one crew is available
  POSTCONDITION: all faults either assigned or flagged as pending-crew

  WHILE faultQueue is not empty DO
    fault ← faultQueue.extractMin()         // O(log n): extracts highest-urgency fault

    availableCrew ← null
    FOR EACH crew IN crews DO
      IF crew.isAvailable() AND crew.type matches fault.category THEN
        availableCrew ← crew
        BREAK

    IF availableCrew ≠ null THEN
      fault.assignedCrew ← availableCrew.name
      fault.status ← "Dispatched"
      availableCrew.activeJobs ← availableCrew.activeJobs + 1
    ELSE
      fault.status ← "Pending — No Crew Available"
      log fault to audit stack

  RETURN dispatched count, pending count

Complexity: O(F log F + F × C) where F = faults, C = crews
```

---

## Pseudocode 2: Dijkstra — Shortest Route from Dispatch Hub to Fault Location

```
PROCEDURE Dijkstra(graph: Graph, source: int) → Result
  PRECONDITION: All edge weights ≥ 0 (negative weights → IllegalArgumentException)
  POSTCONDITION: dist[v] = shortest distance from source to every vertex v

  n ← graph.vertexCount
  dist[0..n-1] ← +∞
  pred[0..n-1] ← -1
  visited[0..n-1] ← false
  dist[source] ← 0

  pq ← PriorityQueue()
  pq.insert((source, 0.0))

  WHILE pq is not empty DO
    (u, d) ← pq.extractMin()
    IF visited[u] THEN CONTINUE
    visited[u] ← true

    FOR EACH edge (u → v, weight) in graph.getNeighbors(u) DO
      IF weight < 0 THEN THROW IllegalArgumentException
      IF dist[u] + weight < dist[v] THEN
        dist[v] ← dist[u] + weight
        pred[v] ← u
        pq.insert((v, dist[v]))

  RETURN Result(dist, pred)

PATH RECONSTRUCTION:
  path ← []
  at ← target
  WHILE at ≠ -1 DO
    path.prepend(at)
    at ← pred[at]
  RETURN path

Complexity: O((V + E) log V) time, O(V) space
```

---

## Pseudocode 3: Kruskal — Minimum Feeder Line Spanning Network

```
PROCEDURE Kruskal(graph: Graph) → MST
  PRECONDITION: graph is connected and undirected
  POSTCONDITION: returns minimum spanning tree as edge list

  edges ← graph.getAllEdges()
  edges.sort() by weight ascending               // O(E log E)

  ds ← DisjointSet(graph.vertexCount)
  mst ← []
  totalCost ← 0

  FOR EACH edge (u, v, weight) IN edges DO
    IF ds.find(u) ≠ ds.find(v) THEN            // Different components: no cycle
      ds.union(u, v)                            // Merge components: Union by Rank
      mst.append((u, v, weight))
      totalCost ← totalCost + weight

    IF mst.size = graph.vertexCount - 1 THEN BREAK  // MST complete

  IF mst.size < graph.vertexCount - 1 THEN
    THROW "Graph is disconnected — MST not possible"

  RETURN MST{edges=mst, totalCost=totalCost}

Complexity: O(E log E) time (dominated by sort), O(V) space for DisjointSet
```

---

## Pseudocode 4: B-Tree Indexing — Fault Record Lookup

```
PROCEDURE BTree.search(node, key) → value or null
  PRECONDITION: node is root or valid internal/leaf node
  
  i ← 0
  WHILE i < node.keys.size AND key > node.keys[i] DO
    i ← i + 1

  IF i < node.keys.size AND key = node.keys[i] THEN
    RETURN node.values[i]       // Found at this node

  IF node.isLeaf THEN
    RETURN null                  // Not present in tree

  RETURN BTree.search(node.children[i], key)  // Recurse into correct child

PROCEDURE BTree.insert(key, value):
  IF root is full THEN
    oldRoot ← root
    root ← new Node(leaf=false)
    root.children.add(oldRoot)
    root.splitChild(0, oldRoot)   // Split and promote median key
  root.insertNonFull(key)

Complexity: O(log_m n) per search/insert where m = minimum degree, n = keys
```

---

## Pseudocode 5: Stack-Backed Audit Undo Log

```
PROCEDURE AuditLog.push(action: AuditEvent)
  PRECONDITION: action ≠ null
  stack.push(action)
  persistToDatabase(action)

PROCEDURE AuditLog.undo() → AuditEvent
  PRECONDITION: stack is not empty
  action ← stack.pop()
  reverseEffect(action)
  RETURN action

PROCEDURE AuditLog.printHistory()
  temp ← new Stack()
  WHILE stack is not empty DO
    event ← stack.pop()
    print(event)
    temp.push(event)
  WHILE temp is not empty DO        // Restore original order
    stack.push(temp.pop())

PROCEDURE reverseEffect(action):
  SWITCH action.type:
    CASE "ASSIGN_CREW":   undo crew assignment, set fault back to "Pending"
    CASE "CLOSE_FAULT":   reopen fault, set status back to "In Progress"
    CASE "ADD_FAULT":     delete fault record from database
    DEFAULT:              log warning — unknown action type

Complexity: push O(1), pop/undo O(1), printHistory O(n)
```
