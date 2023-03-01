package com.jyx.infra.spring.id;

import com.jyx.infra.id.SnowflakeIdAllocator;
import org.springframework.beans.factory.DisposableBean;

/**
 * @author Archforce
 * @since 2023/3/1 20:47
 */
public class SnowflakeIdAllocatorWrapper<T> implements DisposableBean, SnowflakeIdAllocator<T> {

    private SnowflakeIdAllocator<T> allocator;

    public SnowflakeIdAllocatorWrapper(SnowflakeIdAllocator<T> allocator) {
        this.allocator = allocator;
    }

    @Override
    public T getId() {
        return allocator.getId();
    }

    @Override
    public String parseId(T id) {
        return allocator.parseId(id);
    }

    @Override
    public long getWorkerId() {
        return allocator.getWorkerId();
    }

    @Override
    public long getDataCenterId() {
        return allocator.getDataCenterId();
    }

    @Override
    public void destroy() {
        allocator.close();
    }
}
