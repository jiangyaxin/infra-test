package com.jyx.infra.id;

import static com.jyx.infra.id.SnowflakeIdFormatter.DEFAULT_FORMATTER;

/**
 * @author JYX
 * @since 2021/10/20 17:02
 */
public class IdAllocators {

    public static SnowflakeIdAllocator<Long> ofDefaultSnowflake(int dataCenterId, int workerId) {
        return ofDefaultSnowflake(DEFAULT_FORMATTER, dataCenterId, workerId);
    }

    public static SnowflakeIdAllocator<Long> ofCachedSnowflake(int dataCenterId, int workerId) {
        return ofCachedSnowflake(DEFAULT_FORMATTER, dataCenterId, workerId);
    }

    public static DefaultSnowflakeIdAllocator ofDefaultSnowflake(SnowflakeIdFormatter fmt, int dataCenterId, int workerId) {
        return new DefaultSnowflakeIdAllocator(fmt, dataCenterId, workerId);
    }

    public static CachedSnowflakeIdAllocator ofCachedSnowflake(SnowflakeIdFormatter fmt, int dataCenterId, int workerId) {
        return new CachedSnowflakeIdAllocator(fmt, dataCenterId, workerId);
    }
}
