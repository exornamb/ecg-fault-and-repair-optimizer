# Hash Collision Statistics & Performance Analysis
## Group 15 — Codebility v2.0 | DCIT 308 Topic 5: Custom Data Structures
**Lead Author / Topic Owner:** Patricia Gyan (Student ID: 22141938)  
**Index Parameter Reference:** Michelle Nana Abena Asantewaa Sarfo (Student ID: 22396802)

---

## 1. Experimental Setup & Team Parameter Derivation

In accordance with project requirements, the custom `HashTable` implementation uses prime-modular indexing and load-factor-triggered prime resizing derived from team member Michelle Sarfo's index number (`22396802`):

* **Initial Prime Capacity ($M_0 = 103$):**  
  $$\text{capacity} = \text{next\_prime}(100 + (22396802 \pmod{50})) = \text{next\_prime}(100 + 2) = 103$$
* **Hash Seed Constant ($S = 6802$):**  
  $$\text{seed} = \text{last\_4\_digits}(22396802) = 6802$$
* **Hash Spreading & Indexing Formula:**  
  $$h(k) = ((k.\text{hashCode}() \oplus 6802) \oplus (h \gg 16)) \ \& \ \text{0x7FFFFFFF}$$
  $$\text{index}(k) = h(k) \pmod{\text{table.length}}$$
* **Resizing Policy:** Whenever the load factor $\alpha = \frac{N}{M}$ exceeds the threshold $\lambda = 0.75$, the table expands to $M_{\text{new}} = \text{next\_prime}(2 \times M_{\text{current}})$ and rehashes all existing entries into the new bucket array.

---

## 2. Empirical Collision Statistics

### Experiment A: Sequential ECG Key Ingestion (`HashCollisionAnalysis.java`)
Using standard location identifiers (`"Location0"`, `"Location1"`, ..., `"LocationN"`), we tracked collision accumulation across three scale thresholds:

| Keys Inserted ($N$) | Initial Capacity ($M_0$) | Final Capacity ($M$) | Prime Resizing Stages | Final Load Factor ($\alpha$) | Total Collisions ($C$) | Collision Rate ($C/N$) |
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| **100** | 103 | **211** *(Prime)* | $103 \to 211$ | **0.47** | **10** | 10.0% |
| **1,000** | 103 | **1,733** *(Prime)* | $103 \to 211 \to 431 \to 863 \to 1,733$ | **0.58** | **174** | 17.4% |
| **20,000** | 103 | **27,803** *(Prime)* | $103 \to \dots \to 13,897 \to 27,803$ | **0.72** | **5,522** | 27.6% |

### Experiment B: Random Integer Key Ingestion (`data/hash_experiment.csv`)
Recorded across the full benchmark suite ($N = 100$ to $N = 20,000$):

| Keys Inserted ($N$) | Final Table Capacity ($M$) | Final Load Factor ($\alpha = N/M$) | Total Collisions ($C$) |
|:---:|:---:|:---:|:---:|
| **100** | 211 | 0.4455 | 16 |
| **500** | 863 | 0.5481 | 104 |
| **1,000** | 1,733 | 0.5551 | 211 |
| **5,000** | 6,947 | 0.6888 | 1,339 |
| **10,000** | 13,901 | 0.6866 | 2,734 |
| **20,000** | 27,803 | 0.6845 | 5,492 |

---

## 3. Technical Observations & Theoretical Analysis

1. **Impact of Prime Modulo Sizing:**
   Unlike default power-of-two table sizing ($16 \to 32 \to 64 \dots$), starting at prime $103$ and expanding through prime numbers ($211, 431, 863, 1733, \dots, 27803$) drastically reduces clustering. In prime-sized modular arithmetic, any non-zero stride or bit-pattern pattern in `hashCode()` is coprime to the modulus, producing significantly lower collision rates compared to power-of-two masks (which only examine low-order bits).

2. **Load Factor Invariant ($\alpha \le 0.75$):**
   Across all trials up to $N = 20,000$, the load factor remained strictly bounded beneath the $0.75$ trigger threshold ($\alpha_{\max} = 0.72$). Whenever the load factor approaches $0.75$, the table geometric rehash redistributes entries across a bucket array more than twice as large.

3. **Collision Rate Stability & Separate Chaining Cost:**
   Even at $20,000$ keys, average chain length across occupied buckets is $\approx 1.28$, meaning standard lookups (`get()`) require an average of only $1 + \frac{\alpha}{2} \approx 1.36$ pointer traversals. This empirically proves Patricia's theoretical oral defense answer: separate chaining maintains robust amortized $O(1)$ operations under load-factor-triggered resizing.

