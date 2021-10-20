package com.jyx.experience.entity.id;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.time.Instant;
import java.time.LocalDateTime;

import static com.jyx.experience.datetime.DateTimeConstant.DATETIME_FORMATTER;
import static com.jyx.experience.datetime.DateTimeConstant.ZONE_DEFAULT;

/**
 * 雪花算法ID
 *
 *   +------+----------------------+----------------+--------------+-----------+
 *   | sign |     delta seconds    | data center id | work node id | sequence  |
 *   +------+----------------------+----------------+--------------+-----------+
 *     1bit          31bit               6bit            12bit         14bit
 *
 * 1.第一位 占用1bit，其值始终是0，没有实际作用。
 * 2.时间戳 占用31bit，精确到秒，总共可以容纳约68年的时间。
 * 3.数据中心id 占用6bit，最多可以64个数据中心。
 * 4.工作机器id 占用12bit，每个数据中心最多可以有4096个节点。
 * 4.序列号 占用14bit，每个节点每秒0开始不断累加，最多可以累加到16384
 *
 * 同一秒的ID数量 = 64 X 4096  X 16384
 *
 * @author JYX
 * @since 2021/10/19 16:04
 */
@Slf4j
public class DefaultSnowflakeIdGenerator implements IdGenerator<Long> {

    public final static LocalDateTime START_LOCAL_DATE_TIME = LocalDateTime.of(2021,10,19,0,0,0);

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
    long startSecond = START_LOCAL_DATE_TIME.atZone(ZONE_DEFAULT).toEpochSecond();

    SnowflakeIdFormatter fmt;

    public DefaultSnowflakeIdGenerator(SnowflakeIdFormatter fmt, int dataCenterId, int workerId){
        fmt.validate(dataCenterId, workerId);

        this.dataCenterId = dataCenterId;
        this.workerId = workerId;
        this.fmt = fmt;

        log.info("Initialized bits({}) for dataCenterId:{} workerID:{}", fmt,dataCenterId,workerId);
    }

    @Override
    public Long getId() {
        return next();
    }

    private synchronized Long next() {
        long currentSecond = getCurrentSecond();

        // 当前时间不能小于上一秒时间
        Assert.isTrue(currentSecond >= lastSecond,String.format("Clock moved backwards. Refusing for %s seconds", lastSecond - currentSecond));

        // 同一秒内增大序列号
        if (currentSecond == lastSecond) {
            sequence = (sequence + 1) & fmt.maxSequence;
            // 同一秒的序列数已经达到最大，只能等待下一秒
            if(sequence == 0L){
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
        long currentSecond = LocalDateTime.now().atZone(ZONE_DEFAULT).toInstant().getEpochSecond();
        Assert.isTrue(currentSecond - startSecond <= fmt.maxTimestamp,
                String.format("Timestamp bits is exhausted. Refusing id generate. Now: %s",currentSecond));
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
    public String parseId(Long  id) {
        int sequence = fmt.sequenceOf(id);
        int workerId = fmt.workerIdOf(id);
        int dataCenterId = fmt.dataCenterIdOf(id);
        long timestampOffset = fmt.timestampOffsetOf(id);

        LocalDateTime thatTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(startSecond + timestampOffset), ZONE_DEFAULT);
        String thatTimeStr = thatTime.format(DATETIME_FORMATTER);

        return String.format("{\"id\":\"%s\",\"timestamp\":\"%s\",\"dataCenterId\":\"%s\",\"workerId\":\"%s\",\"sequence\":\"%s\"}",
                id, thatTimeStr,dataCenterId, workerId, sequence);
    }
}
