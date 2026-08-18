# Ghana Localisation Note
## ECG Dumsor Response Optimizer — Group 15, Codebility v2.0

---

## 1. Context: The Dumsor Problem

"Dumsor" (Twi for "off-on") is the chronic power outage crisis that has affected Ghana since at least 2012. The Electricity Company of Ghana (ECG) is the state-owned utility responsible for electricity distribution across Accra, Kumasi, and surrounding regions. During Dumsor, rolling blackouts could last 12–24 hours across residential, commercial, and industrial zones, severely impacting economic productivity and daily life.

This project models the fault dispatch and repair workflow for ECG's **Accra / Legon distribution zone**, which supplies power to the University of Ghana campus, Legon residential estates, East Legon, Adenta, and surrounding communities.

---

## 2. Local Dataset Description

### Locations (50 nodes)
ECG substations, transformer stations, and customer service centers across:
- **University of Ghana, Legon** — main campus, hostels, faculties
- **East Legon** — residential high-density zones
- **Adenta** — suburban grid zones
- **Madina** — mixed commercial/residential nodes
- **Haatso** — industrial feeder grid
- **Achimota** — upstream distribution hub

Coordinates are expressed in decimal-degree approximations of the Accra metropolitan bounding box (lat ≈ 5.55–5.70°N, lon ≈ -0.28–-0.15°W).

### Roads / Feeder Lines (100 edges)
Road connections and underground feeder lines between substations and service centers. Weights represent estimated travel time in minutes for an ECG repair crew vehicle.

### Repair Crews / Resources (30 units)
30 ECG emergency repair crews and response units classified by type:
- **Substation Crew** — handles transformer and switching station faults
- **Overhead Line Crew** — repairs aerial feeder lines
- **Underground Cable Crew** — handles underground cable faults
- **Emergency Response** — rapid mobilisation for critical outages

### Service Requests (300 records)
Simulated ECG outage and fault reports across the Legon/Accra grid with urgency levels 1–5 (5 = Critical outage affecting hospital or major substation; 1 = routine scheduled maintenance).

---

## 3. Team Parameters (Index-Derived)

All team-specific algorithm parameters are derived from the index number of group member Michelle Nana Abena Asantewaa Sarfo (22396802):

| Parameter | Derivation | Value |
|---|---|---|
| **Urgency Weight** | 1.0 + (digit_sum(22396802) % 5) × 0.2 = 1.0 + (32 % 5) × 0.2 = 1.0 + 0.4 | **1.4** |
| **Road Penalty** | 1.0 + (last_2_digits % 10) × 0.1 = 1.0 + (02 % 10) × 0.1 | **1.2** |
| **Hash Capacity** | next_prime(100 + (22396802 % 50)) = next_prime(102) | **103** |
| **Hash Seed** | last 4 digits of index | **6802** |

These parameters are used directly in `TeamParameters.java` and referenced in the hash table and dispatch priority computations.

---

## 4. DSA-to-Application Integration

| Data Structure | ECG Application |
|---|---|
| DynamicArray | Stores locations and service requests loaded from CSV |
| LinkedList | Maintains ordered crew dispatch log / audit trail |
| Stack | Undo/redo operator action history |
| FIFO Queue | First-come-first-served service request queue |
| Circular Queue | Fixed-slot dispatch rotation (bounded dispatcher slots) |
| Deque | Mixed urgent/normal request management (urgent faults jump queue with addFirst) |
| Priority Queue (Min-Heap) | Dispatches faults by urgency — critical outages served first |
| BST | Indexes faults by urgency for ordered search and retrieval |
| Red-Black Tree | Self-balancing fault index — prevents BST degeneration under sorted inserts |
| B-Tree | Represents database indexing concept for large fault table |
| HashTable | Fast O(1) average lookup of crew by name and location by ID |
| CustomMap | Key-Value map for location_id → vertex_id graph mapping |
| CustomSet | Visited node set during BFS/DFS graph traversal |
| DisjointSet | Cycle detection in Kruskal MST for feeder line minimum connection network |
| Graph | ECG Legon distribution network as weighted adjacency list + matrix |
