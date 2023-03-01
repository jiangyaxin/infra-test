package com.jyx.infra.collection;

/**
 * Tuple Utilities
 *
 * @author JYX
 * @since 2021/11/5 17:04
 */
public class Tuple {

    /**
     * Tuple 2 Factory
     *
     * @param <T1>   the 1st element type
     * @param <T2>   the 2nd element type
     * @param first  the 1st element
     * @param second the 2nd element
     * @return Tuple 2
     */
    public static <T1, T2> Tuple2<T1, T2> of(T1 first, T2 second) {
        return new Tuple2<>(first, second);
    }
}
