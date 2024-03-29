package com.jyx.infra.id.buffer;

import java.util.List;

/**
 * @author jiangyaxin
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
