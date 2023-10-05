package com.jyx.infra.thread;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Archforce
 * @since 2023/10/5 10:53
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class FutureResult<T> {

    private T data;

    private Throwable throwable;

    public boolean exceptionally() {
        return throwable != null;
    }
}
