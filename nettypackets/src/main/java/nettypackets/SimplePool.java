package nettypackets;

/*
 * A class that implements a simple pool for generic objects
 */
public class SimplePool<T> {
    private Object[] pool;
    private int size;
    private int next;

    public SimplePool(int size) {
        this.size = size;
        pool = new Object[size];
        next = 0;
    }

    @SuppressWarnings("unchecked")
    public synchronized T get() {
        if (next < size) {
            return (T)pool[next++];
        } else {
            return null;
        }
    }

    public synchronized void put(T obj) {
        if (next > 0) {
            pool[--next] = obj;
        }
    }
}