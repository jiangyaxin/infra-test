package com.jyx.infra.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Archforce
 * @since 2023/9/18 17:01
 */
public class Collections {

    public static <T> Collector<T, ?, T> reducing(Supplier<T> supplier, BinaryOperator<T> reduceOp) {
        return Collector.of(
                supplier,
                reduceOp::apply,
                reduceOp
        );
    }

    public static <T, K> Map<K, List<T>> groupBy(Collection<T> data, Function<T, K> keyFunction) {
        if (data == null || data.isEmpty()) {
            return new HashMap<>();
        }

        Map<K, List<T>> result = groupBy(data.stream(), data.size(), keyFunction);
        return result;
    }

    public static <T, K> Map<K, List<T>> groupBy(Stream<T> stream, int size, Function<T, K> keyFunction) {
        if (stream == null) {
            return new HashMap<>();
        }

        Map<K, List<T>> result = stream.collect(Collectors.groupingBy(
                keyFunction,
                () -> Maps.newHashMap(size),
                Collectors.toList()
        ));
        return result;
    }

    public static <T, K, U> Map<K, U> toMap(Collection<T> data, Collector<T, ?, Map<K, U>> toMapCollector) {
        if (data == null || data.isEmpty()) {
            return new HashMap<>();
        }

        Map<K, U> result = toMap(data.stream(), data.size(), toMapCollector);
        return result;
    }

    public static <T, K, U> Map<K, U> toMap(Stream<T> stream, int size, Collector<T, ?, Map<K, U>> toMapCollector) {
        if (stream == null) {
            return new HashMap<>();
        }

        Collector<T, Map, Map<K, U>> collector = (Collector<T, Map, Map<K, U>>) toMapCollector;
        Collector<T, ?, Map<K, U>> newCollector = Collector.of(
                () -> Maps.newHashMap(size),
                collector.accumulator(),
                collector.combiner(),
                collector.finisher(),
                collector.characteristics().toArray(new Collector.Characteristics[0])
        );
        Map<K, U> result = stream.collect(newCollector);
        return result;
    }
}
