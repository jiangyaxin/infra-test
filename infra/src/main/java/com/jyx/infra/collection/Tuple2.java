package com.jyx.infra.collection;

/**
 * Tuple 2 class
 *
 * @param <T1> the 1st element type
 * @param <T2> the 2nd element type
 * @author JYX
 * @since 2021/11/5 17:05
 */
public class Tuple2<T1, T2> {
    T1 first;
    T2 second;

    /**
     * Can't be created directly, always use Tuple.of(a,b) to initialize it;
     *
     * @param first  the 1st element
     * @param second the 2nd element
     */
    Tuple2(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public T1 getFirst() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "(" + first + "," + second + ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((first == null) ? 0 : first.hashCode());
        result = prime * result + ((second == null) ? 0 : second.hashCode());
        return result;
    }

    @SuppressWarnings("rawtypes")
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
        Tuple2 other = (Tuple2) obj;
        if (first == null) {
            if (other.first != null) {
                return false;
            }
        } else if (!first.equals(other.first)) {
            return false;
        }
        if (second == null) {
            return other.second == null;
        } else {
            return second.equals(other.second);
        }
    }
}
