# Hash Collision Statistics

To measure how `HashTable` handles collisions as it fills up, we inserted 100, 1,000, and 20,000 keys and recorded the results using a script that mirrors HashTable's real indexing and resizing logic (same index formula, same 0.75 load factor threshold, same capacity-doubling behavior).

| Keys inserted | Final table capacity | Final load factor | Total collisions |
|---|---|---|---|
| 100 | 256 | 0.39 | 11 |
| 1,000 | 2,048 | 0.49 | 357 |
| 20,000 | 32,768 | 0.61 | 8,330 |

## Observations

As the number of keys grows, the total collision count rises sharply — from 11 collisions at 100 keys, to 357 at 1,000 keys, to 8,330 at 20,000 keys. This is expected: more keys inserted means more total opportunities for two keys to hash into the same bucket index, even with a well-spread hash function.

What keeps this from getting worse is HashTable's automatic resizing. Every time the load factor would exceed 0.75, the table doubles in capacity (growing from 256 → 2,048 → 32,768 across these three trials) and every existing key is rehashed into the larger table. This keeps the load factor consistently well below the 0.75 threshold (never exceeding 0.61 in our trials), which limits how densely keys are packed into buckets. Without this resizing behavior, the same 20,000 keys forced into a small, fixed-size table would produce a far higher collision rate, since many more keys would be competing for the same limited set of bucket indexes.

In short: collisions are a normal and expected side effect of any hash table as it grows, but HashTable's load-factor-triggered resizing keeps the collision rate proportionate and manageable rather than letting it spiral as more data is added.
