import java.util.*;
import java.util.function.Consumer;


public class CSArrayList<E> implements Collection<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private E[] elements;
    private int size = 0;
    protected transient int modCount = 0; // structural modification count for iterators

    @SuppressWarnings("unchecked")
    public CSArrayList(int initialCapacity) {
        if (initialCapacity < 0) throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        elements = (E[]) new Object[Math.max(DEFAULT_CAPACITY, initialCapacity)];
    }

    public CSArrayList() {
        this(DEFAULT_CAPACITY);
    }

    private void ensureCapacityInternal(int minCapacity) {
        if (minCapacity - elements.length > 0) grow(minCapacity);
    }

    @SuppressWarnings("unchecked")
    private void grow(int minCapacity) {
        int oldCapacity = elements.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1); // 1.5x
        if (newCapacity - minCapacity < 0) newCapacity = minCapacity;
        if (newCapacity < 0) newCapacity = Integer.MAX_VALUE;
        E[] newArr = (E[]) new Object[newCapacity];
        System.arraycopy(elements, 0, newArr, 0, size);
        elements = newArr;
    }

    @Override
    public boolean add(E e) {
        ensureCapacityInternal(size + 1);
        elements[size++] = e;
        modCount++;
        return true;
    }

    public void add(int index, E element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        ensureCapacityInternal(size + 1);
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
        modCount++;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], o)) {
                fastRemove(i);
                return true;
            }
        }
        return false;
    }

    public E remove(int index) {
        rangeCheck(index);
        E old = elements[index];
        fastRemove(index);
        return old;
    }

    private void fastRemove(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0) System.arraycopy(elements, index + 1, elements, index, numMoved);
        elements[--size] = null;
        modCount++;
    }

    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) if (elements[i] == null) return i;
        } else {
            for (int i = 0; i < size; i++) if (o.equals(elements[i])) return i;
        }
        return -1;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    public E get(int index) {
        rangeCheck(index);
        return elements[index];
    }

    public E set(int index, E element) {
        rangeCheck(index);
        E old = elements[index];
        elements[index] = element;
        return old;
    }

    private void rangeCheck(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public Iterator<E> iterator() { return new Itr(); }

    private class Itr implements Iterator<E> {
        int cursor = 0;
        int lastRet = -1;
        int expectedModCount = modCount;

        private void checkForComodification() {
            if (expectedModCount != modCount) throw new ConcurrentModificationException();
        }

        @Override
        public boolean hasNext() {
            checkForComodification();
            return cursor != size;
        }

        @Override
        public E next() {
            checkForComodification();
            int i = cursor;
            if (i >= size) throw new NoSuchElementException();
            cursor = i + 1;
            lastRet = i;
            return elements[i];
        }

        @Override
        public void remove() {
            if (lastRet < 0) throw new IllegalStateException();
            checkForComodification();
            CSArrayList.this.remove(lastRet);
            cursor = lastRet;
            lastRet = -1;
            expectedModCount = modCount;
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            while (cursor < size) {
                checkForComodification();
                action.accept(elements[cursor++]);
            }
        }
    }

    @Override public Object[] toArray() { return Arrays.copyOf(elements, size, Object[].class); }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) return (T[]) Arrays.copyOf(elements, size, a.getClass());
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override public boolean containsAll(Collection<?> c) {
        for (Object e : c) if (!contains(e)) return false;
        return true;
    }

    @Override public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);
        for (Object o : a) elements[size++] = (E) o;
        if (numNew > 0) { modCount++; return true; }
        return false;
    }

    @Override public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) { it.remove(); modified = true; }
        }
        return modified;
    }

    @Override public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) { it.remove(); modified = true; }
        }
        return modified;
    }

    @Override public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null;
        size = 0;
        modCount++;
    }

    // Optional operations not implemented for brevity
    @Override public boolean removeIf(java.util.function.Predicate<? super E> filter) { throw new UnsupportedOperationException(); }
    @Override public Spliterator<E> spliterator() { return Spliterators.spliterator(elements, 0, size, 0); }
    @Override public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Collection)) return false;
        Collection<?> c = (Collection<?>) o;
        if (c.size() != size) return false;
        Iterator<?> it = c.iterator();
        for (int i = 0; i < size; i++) {
            if (!Objects.equals(elements[i], it.next())) return false;
        }
        return true;
    }
    @Override public int hashCode() {
        int h = 1;
        for (int i = 0; i < size; i++) h = 31*h + (elements[i]==null?0:elements[i].hashCode());
        return h;
    }
}
