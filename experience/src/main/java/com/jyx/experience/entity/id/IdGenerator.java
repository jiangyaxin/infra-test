package com.jyx.experience.entity.id;

/**
 * @author JYX
 * @since 2021/10/19 16:01
 */
public interface IdGenerator<T> {

    /**
     * 生成下一个ID
     * @return identifier
     */
    T getId();

    /**
     * 解析ID
     * @param id id
     * @return id json str
     */
    String parseId(T id);

}
