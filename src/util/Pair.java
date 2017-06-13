package util;

import java.util.*;

/**
 * A simple ordered pair.
 * @param <X> The type of the first component.
 * @param <Y> The type of the second component.
 * @author cryingshadow
 */
public class Pair<X, Y> implements Map.Entry<X, Y> {

    /**
     * @param pair Some pair with the same type for both components.
     * @return A pair with the first and the second component of the specified pair being exchanged.
     */
    public static <Z> Pair<Z, Z> flip(final Pair<Z, Z> pair) {
        return new Pair<Z, Z>(pair.getValue(), pair.getKey());
    }

    /**
     * The first component.
     */
    private final X x;

    /**
     * The second component.
     */
    private final Y y;

    /**
     * @param key The first component.
     * @param value The second component.
     */
    public Pair(final X key, final Y value) {
        this.x = key;
        this.y = value;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Pair<?, ?> other = (Pair<?, ?>)obj;
        if (this.x == null) {
            if (other.x != null) {
                return false;
            }
        } else if (!this.x.equals(other.x)) {
            return false;
        }
        if (this.y == null) {
            if (other.y != null) {
                return false;
            }
        } else if (!this.y.equals(other.y)) {
            return false;
        }
        return true;
    }

    @Override
    public X getKey() {
        return this.x;
    }

    @Override
    public Y getValue() {
        return this.y;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.x == null) ? 0 : this.x.hashCode());
        result = prime * result + ((this.y == null) ? 0 : this.y.hashCode());
        return result;
    }

    @Override
    public Y setValue(final Y value) {
        throw new UnsupportedOperationException("Immutable pairs cannot be modified!");
    }

    @Override
    public String toString() {
        final StringBuilder res = new StringBuilder();
        res.append("(");
        res.append(this.x == null ? "null" : this.x.toString());
        res.append(",");
        res.append(this.y == null ? "null" : this.y.toString());
        res.append(")");
        return res.toString();
    }

}
