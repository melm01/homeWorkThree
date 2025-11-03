import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class CSArrayListLabTests {

    @Test
    public void edgeIndexCases() {
        CSArrayList<Integer> list = new CSArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(2));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(3));
        list.add(3, 4);
        assertEquals(4, list.get(3));
    }

    @Test
    public void multipleResizes() {
        CSArrayList<Integer> list = new CSArrayList<>(1);
        int N = 100_000;
        for (int i = 0; i < N; i++) list.add(i);
        assertEquals(N, list.size());
        assertEquals(0, list.get(0));
        assertEquals(N-1, list.get(N-1));
    }

    @Test
    public void searchesWithDuplicatesAndNulls() {
        CSArrayList<String> list = new CSArrayList<>();
        list.add("x");
        list.add(null);
        list.add("x");
        list.add("y");
        assertEquals(0, list.indexOf("x"));
        assertEquals(1, list.indexOf(null));
        assertTrue(list.contains("y"));
        assertTrue(list.remove("x"));
        assertEquals(1, list.indexOf("x")); // second "x" now at index 1
    }

    @Test
    public void removeObjectBehaviorPresentAbsent() {
        CSArrayList<String> list = new CSArrayList<>();
        list.add("a");
        list.add("b");
        assertTrue(list.remove("a"));
        assertFalse(list.remove("z"));
        assertEquals(1, list.size());
    }

    @Test
    public void failFastIterator() {
        CSArrayList<Integer> list = new CSArrayList<>();
        list.add(1);
        list.add(2);
        Iterator<Integer> it = list.iterator();
        list.add(3);
        assertThrows(ConcurrentModificationException.class, () -> it.hasNext());
    }
}
