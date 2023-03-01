package com.jyx.infra.id;

/**
 * @author JYX
 * @since 2021/10/19 16:01
 */
public interface IdAllocator<T> extends AutoCloseable {

    /**
     * 生成下一个ID
     *
     * @return identifier
     */
    T getId();

    /**
     * 解析ID
     *
     * @param id id
     * @return id json str
     */
    String parseId(T id);

}
