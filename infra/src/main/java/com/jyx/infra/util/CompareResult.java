package com.jyx.infra.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author Archforce
 * @since 2023/10/5 13:56
 */
public class CompareResult<K, L, R> {

    private final Map<K, L> onlyOnLeft;
    private final Map<K, R> onlyOnRight;

    private final Map<K, CompareValue<L, R>> equal;
    private final Map<K, CompareValue<L, R>> notEqual;

    public CompareResult(Map<K, L> onlyOnLeft, Map<K, R> onlyOnRight,
                         Map<K, CompareValue<L, R>> equal, Map<K, CompareValue<L, R>> notEqual) {
        this.onlyOnLeft = Collections.unmodifiableMap(onlyOnLeft);
        this.onlyOnRight = Collections.unmodifiableMap(onlyOnRight);
        this.equal = Collections.unmodifiableMap(equal);
        this.notEqual = Collections.unmodifiableMap(notEqual);
    }

    public Map<K, L> getOnlyOnLeft() {
        return onlyOnLeft;
    }

    public Map<K, R> getOnlyOnRight() {
        return onlyOnRight;
    }

    public Map<K, CompareValue<L, R>> getEqual() {
        return equal;
    }

    public Map<K, CompareValue<L, R>> getNotEqual() {
        return notEqual;
    }

    public Collection<L> getOnlyOnLeftValues() {
        return onlyOnLeft.values();
    }

    public Collection<R> getOnlyOnRightValues() {
        return onlyOnRight.values();
    }

    public Collection<CompareValue<L, R>> getEqualValues() {
        return equal.values();
    }

    public Collection<CompareValue<L, R>> getNotEqualValues() {
        return notEqual.values();
    }
}
