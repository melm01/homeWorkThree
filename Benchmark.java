import java.util.*;

public class Benchmark {
    private static final int[] SIZES = {100_000, 250_000, 500_000, 1_000_000};
    private static final int RANDOM_OPS = 100_000;

    public static void main(String[] args) {
        System.out.printf("%-12s %-12s %-12s %-12s%n", "N", "Impl", "Append(ms)", "RandomGet(ms)");
        for (int n : SIZES) benchForN(n);
    }

    private static void benchForN(int n) {
        long appendCS = measureAppendCS(n);
        long randomCS = measureRandomGetCS(n);
        long appendJ = measureAppendJ(n);
        long randomJ = measureRandomGetJ(n);

        System.out.printf("%-12d %-12s %-12d %-12d%n", n, "CSArrayList", appendCS, randomCS);
        System.out.printf("%-12d %-12s %-12d %-12d%n", n, "ArrayList", appendJ, randomJ);
    }

    private static long measureAppendCS(int n) {
        CSArrayList<Integer> list = new CSArrayList<>();
        long s = System.nanoTime();
        for (int i = 0; i < n; i++) list.add(i);
        return (System.nanoTime() - s) / 1_000_000;
    }

    private static long measureRandomGetCS(int n) {
        CSArrayList<Integer> list = new CSArrayList<>();
        for (int i = 0; i < n; i++) list.add(i);
        Random r = new Random(42);
        long s = System.nanoTime();
        for (int i = 0; i < RANDOM_OPS; i++) list.get(r.nextInt(n));
        return (System.nanoTime() - s) / 1_000_000;
    }

    private static long measureAppendJ(int n) {
        ArrayList<Integer> list = new ArrayList<>();
        long s = System.nanoTime();
        for (int i = 0; i < n; i++) list.add(i);
        return (System.nanoTime() - s) / 1_000_000;
    }

    private static long measureRandomGetJ(int n) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) list.add(i);
        Random r = new Random(42);
        long s = System.nanoTime();
        for (int i = 0; i < RANDOM_OPS; i++) list.get(r.nextInt(n));
        return (System.nanoTime() - s) / 1_000_000;
    }
}
