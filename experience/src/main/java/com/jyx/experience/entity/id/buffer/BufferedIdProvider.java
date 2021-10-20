package com.jyx.experience.entity.id.buffer;

import java.util.List;

/**
 * @author JYX
 * @since 2021/10/19 22:02
 */
@FunctionalInterface
public interface BufferedIdProvider {

    /**
     * 获取一秒内的所有ID
     *
     * @param momentInSecond momentInSecond
     * @return 一秒内所有id
     */
    List<Long> provide(long momentInSecond);

}
