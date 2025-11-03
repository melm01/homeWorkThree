# CSArrayList Lab Report

## Implementation summary
- Implemented `CSArrayList<E>` providing core list operations plus fail-fast iterators using `modCount`.
- Growth strategy mimics `ArrayList` (1.5x) to ensure amortized O(1) append.

## Complexity summary
- `add(E)`: Amortized O(1), worst-case O(n) on grow.
- `get(int)`: O(1).
- `indexOf(Object)`: O(n).
- `remove(int)`: O(n) due to shifting.
- `remove(Object)`: O(n) search + shift.
- Iterator operations: O(1) each; concurrent modifications detected in O(1).

## Tests included
- Edge index cases (0, size-1, size for add(index)).
- Multiple resizes (append 100k items).
- Searches with duplicates and nulls.
- remove(Object) present/absent.
- Fail-fast iterator test that ensures `ConcurrentModificationException` is thrown.

## Benchmarks
Run `Benchmark.java` to compare append and random-get performance for `CSArrayList` and `java.util.ArrayList` at N = 100k, 250k, 500k, 1M. Results will vary by machine; expect similar behavior: ArrayList slightly faster in microbenchmarks but same complexity trends.

## How to run
1. Compile:
```
javac -cp .:junit-platform-console-standalone-1.9.2.jar *.java
```
2. Run tests:
```
java -jar junit-platform-console-standalone-1.9.2.jar --scan-classpath
```
3. Run benchmark:
```
java Benchmark
```
