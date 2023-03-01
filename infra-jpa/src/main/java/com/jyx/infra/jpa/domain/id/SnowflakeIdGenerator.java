package com.jyx.infra.jpa.domain.id;

import com.jyx.infra.id.IdAllocator;
import lombok.extern.slf4j.Slf4j;

import static com.jyx.infra.spring.context.AppConstant.ID_ALLOCATOR;

/**
 * @author JYX
 * @since 2021/10/20 16:34
 */
@Slf4j
public class SnowflakeIdGenerator extends AbstractLazyIdGenerator<Long> {

    public static final String STRATEGY = "com.jyx.infra.jpa.domain.id.SnowflakeIdGenerator";

    @Override
    protected IdAllocator<Long> initGenerator() {
        return ID_ALLOCATOR;
    }
}
