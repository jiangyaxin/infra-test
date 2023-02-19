package com.jyx.feature.test.disruptor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Archforce
 * @since 2022/12/1 9:57
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Truck<T> {

    private T data;

    public void load(T data) {
        this.data = data;
    }

    public void clear() {
        this.data = null;
    }
}
