package com.jyx.infra.jpa.id;

import com.jyx.infra.context.SpringContextHolder;
import com.jyx.infra.id.IdAllocator;
import com.jyx.infra.id.SnowflakeIdAllocator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author JYX
 * @since 2021/10/20 16:34
 */
@Slf4j
public class SnowflakeIdGenerator extends AbstractLazyIdGenerator<Long>{

    @Override
    protected IdAllocator<Long> initGenerator() {
        return SpringContextHolder.getBean(SnowflakeIdAllocator.class);
    }
}
