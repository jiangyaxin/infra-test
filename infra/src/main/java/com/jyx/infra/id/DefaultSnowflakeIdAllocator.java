package com.jyx.infra.id;

import com.jyx.infra.asserts.Asserts;
import com.jyx.infra.datetime.DateTimeConstant;
import com.jyx.infra.log.Logs;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * 雪花算法ID
 * <p>
 * +------+----------------------+----------------+--------------+-----------+
 * | sign |     delta seconds    | data center id | work node id | sequence  |
 * +------+----------------------+----------------+--------------+-----------+
 * 1bit          31bit               6bit            12bit         14bit
 * <p>
 * 1.第一位 占用1bit，其值始终是0，没有实际作用。
 * 2.时间戳 占用31bit，精确到秒，总共可以容纳约68年的时间。
 * 3.数据中心id 占用6bit，最多可以64个数据中心。
 * 4.工作机器id 占用12bit，每个数据中心最多可以有4096个节点。
 * 4.序列号 占用14bit，每个节点每秒0开始不断累加，最多可以累加到16384
 * <p>
 * 同一秒的ID数量 = 64 X 4096  X 16384
 *
 * @author JYX
 * @since 2021/10/19 16:04
 */
@Slf4j
public class DefaultSnowflakeIdAllocator implements SnowflakeIdAllocator<Long> {

    public final static LocalDateTime START_LOCAL_DATE_TIME = LocalDateTime.of(2021, 10, 19, 0, 0, 0);

    /**
     * 数据中心
     */
    long dataCenterId;
    /**
     * 工作机器标识
     */
    long workerId;
    /**
     * 序列号
     */
    long sequence = 0L;
    /**
     * 上一次时间戳
     */
    long lastSecond = -1L;

    /**
     * 启始时间戳
     * 2021-10-19
     */
    long startSecond = START_LOCAL_DATE_TIME.atZone(DateTimeConstant.ZoneOffsets.DEFAULT_ZONE).toEpochSecond();

    SnowflakeIdFormatter fmt;

    public DefaultSnowflakeIdAllocator(SnowflakeIdFormatter fmt, int dataCenterId, int workerId) {
        fmt.validate(dataCenterId, workerId);

        this.dataCenterId = dataCenterId;
        this.workerId = workerId;
        this.fmt = fmt;

        Logs.info(log, "{} Initialized bits({}) for dataCenterId:{} workerID:{}", this.getClass().getSimpleName(), fmt, dataCenterId, workerId);
    }

    @Override
    public Long getId() {
        return next();
    }

    private synchronized Long next() {
        long currentSecond = getCurrentSecond();

        // 当前时间不能小于上一秒时间
        long finalCurrentSecond = currentSecond;
        Asserts.isTrue(currentSecond >= lastSecond, () -> String.format("Clock moved backwards. Refusing for %s seconds", lastSecond - finalCurrentSecond));

        // 同一秒内增大序列号
        if (currentSecond == lastSecond) {
            sequence = (sequence + 1) & fmt.maxSequence;
            // 同一秒的序列数已经达到最大，只能等待下一秒
            if (sequence == 0L) {
                currentSecond = getNextSecond(lastSecond);
            }
            // 不是同一秒序列号从0开始
        } else {
            sequence = 0L;
        }

        lastSecond = currentSecond;

        return fmt.toId(currentSecond - startSecond, dataCenterId, workerId, sequence);
    }

    /**
     * Get current second
     */
    private long getCurrentSecond() {
        long currentSecond = System.currentTimeMillis() / 1000;
        Asserts.isTrue(currentSecond - startSecond <= fmt.maxTimestamp,
                () -> String.format("Timestamp bits is exhausted. Refusing id generate. Now: %s", currentSecond));
        return currentSecond;
    }

    /**
     * Get next millisecond
     */
    private long getNextSecond(long lastTimestamp) {
        long timestamp = getCurrentSecond();
        while (timestamp <= lastTimestamp) {
            timestamp = getCurrentSecond();
        }

        return timestamp;
    }

    @Override
    public String parseId(Long id) {
        int sequence = fmt.sequenceOf(id);
        int workerId = fmt.workerIdOf(id);
        int dataCenterId = fmt.dataCenterIdOf(id);
        long timestampOffset = fmt.timestampOffsetOf(id);

        LocalDateTime thatTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(startSecond + timestampOffset), DateTimeConstant.ZoneOffsets.DEFAULT_ZONE);
        String thatTimeStr = thatTime.format(DateTimeConstant.DateTimeFormatters.DEFAULT_DATETIME_FORMATTER);

        return String.format("{\"id\":\"%s\",\"timestamp\":\"%s\",\"dataCenterId\":\"%s\",\"workerId\":\"%s\",\"sequence\":\"%s\"}",
                id, thatTimeStr, dataCenterId, workerId, sequence);
    }

    @Override
    public long getWorkerId() {
        return workerId;
    }

    @Override
    public long getDataCenterId() {
        return dataCenterId;
    }

}
