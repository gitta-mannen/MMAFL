package util;

/**
 * @author Stugatz
 * Generic pair class that holds two objects of type K and V
 * @param <K> object a
 * @param <V> object b
 */
public class Pair<K, V> {

    private final K a;
    private final V b;

    public static <K, V> Pair<K, V> createPair(K a, V b) {
        return new Pair<K, V>(a, b);
    }

    public Pair(K a, V b) {
        this.a = a;
        this.b = b;
    }

    public K getA() {
        return a;
    }

    public V getB() {
        return b;
    }

}