package util;

import java.util.*;

/**
 * A union find data structure.
 * @author cryingshadow
 * @param <E> The element type.
 */
public class UnionFind<E> implements Collection<E> {

    /**
     * The representatives for each element in the union find data structure.
     */
    private final Map<E, E> representatives;

    /**
     * Creates an empty union find data structure.
     */
    public UnionFind() {
        this.representatives = new LinkedHashMap<E, E>();
    }

    /**
     * Creates a union find data structure with the specified elements being disjoint.
     * @param c A collection.
     */
    public UnionFind(final Collection<? extends E> c) {
        this();
        for (final E element : c) {
            this.representatives.put(element, element);
        }
    }

    @Override
    public boolean add(final E e) {
        if (this.representatives.containsKey(e)) {
            return false;
        }
        this.representatives.put(e, e);
        return true;
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        boolean res = false;
        for (final E element : c) {
            res |= this.add(element);
        }
        return res;
    }

    @Override
    public void clear() {
        this.representatives.clear();
    }

    @Override
    public boolean contains(final Object o) {
        return this.representatives.containsKey(o);
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return this.representatives.keySet().containsAll(c);
    }

    /**
     * Find with path compression.
     * @param element Some element.
     * @return The representative for the specified element.
     */
    public E find(final E element) {
        if (!this.contains(element)) {
            this.representatives.put(element, element);
            return element;
        }
        E current = element;
        E representative = this.representatives.get(current);
        final List<E> elements = new LinkedList<E>();
        while (!representative.equals(current)) {
            elements.add(current);
            current = representative;
            representative = this.representatives.get(current);
        }
        for (final E e : elements) {
            this.representatives.put(e, representative);
        }
        return representative;
    }

    /**
     * @return The set of classes induced by this union find data structure.
     */
    public Map<E, Set<E>> getClasses() {
        final Map<E, Set<E>> res = new LinkedHashMap<E, Set<E>>();
        for (final E element : this) {
            final E representative = this.find(element);
            if (!res.containsKey(representative)) {
                res.put(representative, new LinkedHashSet<E>());
            }
            res.get(representative).add(element);
        }
        return res;
    }

    @Override
    public boolean isEmpty() {
        return this.representatives.isEmpty();
    }

    /**
     * @param e1 Some element.
     * @param e2 Another element.
     * @return True if both have the same representative.
     */
    public boolean isJoint(final E e1, final E e2) {
        return this.find(e1).equals(this.find(e2));
    }

    @Override
    public Iterator<E> iterator() {
        final Iterator<E> iterator = this.representatives.keySet().iterator();
        return new Iterator<E>() {

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public E next() {
                return iterator.next();
            }

        };
    }

    @Override
    public boolean remove(final Object o) {
        throw new UnsupportedOperationException("Removal from union find data structures is not supported!");
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        throw new UnsupportedOperationException("Removal from union find data structures is not supported!");
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        throw new UnsupportedOperationException("Removal from union find data structures is not supported!");
    }

    @Override
    public int size() {
        return this.representatives.size();
    }

    @Override
    public Object[] toArray() {
        return this.representatives.keySet().toArray();
    }

    @Override
    public <T> T[] toArray(final T[] a) {
        return this.representatives.keySet().toArray(a);
    }

    /**
     * Joins the specified elements by joining their representatives.
     * @param e1 Some element.
     * @param e2 Another element.
     */
    public void union(final E e1, final E e2) {
        this.representatives.put(this.find(e1), this.find(e2));
    }

}
