package com.jyx.infra.id;

/**
 * @author jiangyaxin
 * @since 2021/10/20 16:36
 */
public interface SnowflakeIdAllocator<T> extends IdAllocator<T> {

    long getWorkerId();

    long getDataCenterId();

    @Override
    default void close() {
    }
}
