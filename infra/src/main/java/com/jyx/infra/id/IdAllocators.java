package com.jyx.infra.id;

/**
 * @author jiangyaxin
 * @since 2021/10/20 17:02
 */
public class IdAllocators {

    public static SnowflakeIdAllocator<Long> ofDefaultSnowflake(int dataCenterId, int workerId) {
        return ofDefaultSnowflake(SnowflakeIdFormatter.DEFAULT_FORMATTER, dataCenterId, workerId);
    }

    public static SnowflakeIdAllocator<Long> ofCachedSnowflake(int dataCenterId, int workerId) {
        return ofCachedSnowflake(SnowflakeIdFormatter.DEFAULT_FORMATTER, dataCenterId, workerId);
    }

    public static DefaultSnowflakeIdAllocator ofDefaultSnowflake(SnowflakeIdFormatter fmt, int dataCenterId, int workerId) {
        return new DefaultSnowflakeIdAllocator(fmt, dataCenterId, workerId);
    }

    public static CachedSnowflakeIdAllocator ofCachedSnowflake(SnowflakeIdFormatter fmt, int dataCenterId, int workerId) {
        return new CachedSnowflakeIdAllocator(fmt, dataCenterId, workerId);
    }
}
