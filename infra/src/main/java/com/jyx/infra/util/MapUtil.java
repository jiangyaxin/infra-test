package com.jyx.infra.util;

import com.jyx.infra.asserts.Asserts;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * @author asa
 * @since 2021/11/5 23:33
 */
public class MapUtil {

    public static int evaluateMapSize(int demandSize) {
        return (int) (demandSize / 0.75 + 1);
    }

    public static <K, V> Map<K, V> newHashMap(int demandSize) {
        int mapSize = evaluateMapSize(demandSize);
        return new HashMap<>(mapSize);
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder<>();
    }

    public static <K, L, R> CompareResult<K, L, R> match(Map<K, L> leftMap, Map<K, R> rightMap) {
        return compare(leftMap, rightMap, (left, right) -> true);
    }

    public static <K, L, R> CompareResult<K, L, R> compare(Map<K, L> leftMap, Map<K, R> rightMap) {
        return compare(leftMap, rightMap, Objects::equals);
    }

    public static <K, L, R> CompareResult<K, L, R> compare(Map<K, L> leftMap, Map<K, R> rightMap, BiPredicate<L, R> valueCompareFunction) {
        Asserts.notNull(leftMap, () -> "Left subset is null.");
        Asserts.notNull(rightMap, () -> "Right subset is null.");
        Asserts.notNull(valueCompareFunction, () -> "CompareFunction is null.");

        if (leftMap.size() <= rightMap.size()) {
            return leftIterateCompare(leftMap, rightMap, valueCompareFunction);
        } else {
            return rightIterateCompare(leftMap, rightMap, valueCompareFunction);
        }
    }

    private static <K, L, R> CompareResult<K, L, R> leftIterateCompare(Map<K, L> leftMap, Map<K, R> rightMap, BiPredicate<L, R> valueCompareFunction) {
        Map<K, L> onlyOnLeft = newHashMap(leftMap.size());
        Map<K, R> onlyOnRight = new HashMap<>(rightMap);
        Map<K, CompareValue<L, R>> equal = newHashMap(leftMap.size());
        Map<K, CompareValue<L, R>> notEqual = newHashMap(leftMap.size());

        for (Map.Entry<K, L> entry : leftMap.entrySet()) {
            K leftKey = entry.getKey();
            L leftValue = entry.getValue();
            if (rightMap.containsKey(leftKey)) {
                R rightValue = onlyOnRight.remove(leftKey);
                if (valueCompareFunction.test(leftValue, rightValue)) {
                    equal.put(leftKey, new CompareValue<>(leftValue, rightValue));
                } else {
                    notEqual.put(leftKey, new CompareValue<>(leftValue, rightValue));
                }
            } else {
                onlyOnLeft.put(leftKey, leftValue);
            }
        }

        return new CompareResult<>(onlyOnLeft, onlyOnRight, equal, notEqual);
    }

    private static <K, L, R> CompareResult<K, L, R> rightIterateCompare(Map<K, L> leftMap, Map<K, R> rightMap, BiPredicate<L, R> valueCompareFunction) {
        Map<K, L> onlyOnLeft = new HashMap<>(leftMap);
        Map<K, R> onlyOnRight = newHashMap(rightMap.size());
        Map<K, CompareValue<L, R>> equal = newHashMap(leftMap.size());
        Map<K, CompareValue<L, R>> notEqual = newHashMap(leftMap.size());

        for (Map.Entry<K, R> entry : rightMap.entrySet()) {
            K rightKey = entry.getKey();
            R rightValue = entry.getValue();
            if (leftMap.containsKey(rightKey)) {
                L leftValue = onlyOnLeft.remove(rightKey);
                if (valueCompareFunction.test(leftValue, rightValue)) {
                    equal.put(rightKey, new CompareValue<>(leftValue, rightValue));
                } else {
                    notEqual.put(rightKey, new CompareValue<>(leftValue, rightValue));
                }
            } else {
                onlyOnRight.put(rightKey, rightValue);
            }
        }

        return new CompareResult<>(onlyOnLeft, onlyOnRight, equal, notEqual);
    }

    public static class Builder<K, V> {
        private final Map<K, V> builderMap = new HashMap<>();

        public Builder<K, V> put(K k, V v) {
            builderMap.put(k, v);
            return this;
        }

        public Builder<K, V> putAll(Map<K, V> map) {
            builderMap.putAll(map);
            return this;
        }

        public HashMap<K, V> build() {
            return new HashMap<>(builderMap);
        }
    }
}
