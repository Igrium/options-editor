package com.igrium.options_editor.util;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

/**
 * A read-only wrapper over a list that filters its elements based on a
 * predicate, returning empty optionals when the predicate returns false.
 */
public class ConditionalList<T> extends AbstractList<Optional<T>> {

    @FunctionalInterface
    public static interface ListPredicate<T> {
        boolean test(T item, int index);
    }

    private final List<T> base;
    private final ListPredicate<T> predicate;

    private static final String ERROR_MESSAGE = "ConditionalList is read-only";

    public ConditionalList(List<T> base, ListPredicate<T> predicate) {
        this.base = base;
        this.predicate = predicate;
    }

    @Override
    public int size() {
        return base.size();
    }

    @Override
    public boolean isEmpty() {
        return base.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof Optional opt && opt.isPresent()) {
            return base.contains(opt.get());
        } else {
            return false;
        }
    }

    @Override
    public Iterator<Optional<T>> iterator() {
        return new ConditionalListIterator(base.listIterator());
    }

    @Override
    public boolean add(Optional<T> e) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (var item : c) {
            if (!contains(item)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Optional<T>> c) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Optional<T>> c) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public Optional<T> get(int index) {
        return testValue(base.get(index), index);
    }

    @Override
    public Optional<T> set(int index, Optional<T> element) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public void add(int index, Optional<T> element) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public Optional<T> remove(int index) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public int indexOf(Object o) {
        if (o instanceof Optional opt && opt.isPresent()) {
            return base.indexOf(opt.get());
        } else {
            return -1;
        }
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o instanceof Optional opt && opt.isPresent()) {
            return base.lastIndexOf(opt.get());
        } else {
            return -1;
        }
    }

    @Override
    public ListIterator<Optional<T>> listIterator() {
        return new ConditionalListIterator(base.listIterator());
    }

    @Override
    public ListIterator<Optional<T>> listIterator(int index) {
        return new ConditionalListIterator(base.listIterator(index));
    }

    @Override
    public List<Optional<T>> subList(int fromIndex, int toIndex) {
        return new ConditionalList<>(base.subList(fromIndex, toIndex), predicate);
    }

    private Optional<T> testValue(T val, int index) {
        return predicate.test(val, index) ? Optional.of(val) : Optional.empty();
    }

    private class ConditionalListIterator implements ListIterator<Optional<T>> {
        final ListIterator<T> base;

        ConditionalListIterator(ListIterator<T> base) {
            this.base = base;
        }

        @Override
        public boolean hasNext() {
            return base.hasNext();
        }

        @Override
        public Optional<T> next() {
            int index = base.nextIndex();
            T val = base.next();
            return testValue(val, index);
        }

        @Override
        public boolean hasPrevious() {
            return base.hasPrevious();
        }

        @Override
        public Optional<T> previous() {
            int index = base.previousIndex();
            T val = base.previous();
            return testValue(val, index);
        }

        @Override
        public int nextIndex() {
            return base.nextIndex();
        }

        @Override
        public int previousIndex() {
            return base.previousIndex();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException(ERROR_MESSAGE);
        }

        @Override
        public void set(Optional<T> e) {
            throw new UnsupportedOperationException(ERROR_MESSAGE);
        }

        @Override
        public void add(Optional<T> e) {
            throw new UnsupportedOperationException(ERROR_MESSAGE);
        }
    }
    
    // private class ConditionalIterator implements Iterator<Optional<T>> {
    //     final Iterator<T> base;
    //     int index = 0;

    //     ConditionalIterator(Iterator<T> base) {
    //         this.base = base;
    //     }

    //     @Override
    //     public boolean hasNext() {
    //         return base.hasNext();
    //     }

    //     @Override
    //     public Optional<T> next() {
    //         return base.next();
    //     }
    // }
}
