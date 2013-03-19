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

    public static <K, V> Pair<K, V>[] merge(K[] a, V[] b) {
    	@SuppressWarnings("unchecked")
		Pair<K, V>[] result = new Pair[a.length]; 
    	if (a.length != b.length) {
    		throw new IndexOutOfBoundsException();
    	}
    	
    	for (int i = 0; i < a.length; i++) {
    		result[i] = new Pair<K, V>(a[i], b[i]);
    	}
    	return result;
    }
        
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Pair<?, ?> other = (Pair<?, ?>) obj;
		if (a == null) {
			if (other.a != null) {
				return false;
			}
		} else if (!a.equals(other.a)) {
			return false;
		}
		if (b == null) {
			if (other.b != null) {
				return false;
			}
		} else if (!b.equals(other.b)) {
			return false;
		}
		return true;
	}

}