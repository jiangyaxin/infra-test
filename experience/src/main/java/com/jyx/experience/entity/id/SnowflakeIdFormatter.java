package com.jyx.experience.entity.id;

import org.springframework.util.Assert;

/**
 * @author JYX
 * @since 2021/10/19 16:42
 */
public class SnowflakeIdFormatter {

    /**
     * 使用约68年，64*4096个节点，单节点每秒16384个id
     */
    public static final SnowflakeIdFormatter DEFAULT_FORMATTER = SnowflakeIdFormatter.of(31, 6, 12, 14);

    /**
     * 每一部分的位数
     */
    int timestampBits;
    int dataCenterBits;
    int workerBits;
    int sequenceBits;

    /**
     * 每一部分最大值
     */
    long maxDataCenterId;
    long maxWorkerId;
    long maxSequence;
    long maxTimestamp;

    /**
     * 每一部分向左的位移
     */
    long workerShift;
    long dataCenterShift;
    long timestampShift;

    /**
     *  每一部分的掩码
     */
    long sequenceMask;
    long workerMask;
    long dataCenterMask;
    long timestampMask;

    public static SnowflakeIdFormatter of(int timestampBits, int dataCenterBits, int workerBits, int sequenceBits) {
        return new SnowflakeIdFormatter(timestampBits, dataCenterBits, workerBits, sequenceBits);
    }

    /**
     * 求最大值
     * 假如 timestampBits = 4  -1L = 1111111111111111111111111111111111111111111111111111111111111111
     * 则 -1L << timestampBits = 1111111111111111111111111111111111111111111111111111111111110000
     * ^ 异或
     * -1L ^ (-1L << timestampBits) = 2^4 = 8
     * ~(-1L << sequenceBits) = 2^4 = 8
     */
    private SnowflakeIdFormatter(int timestampBits, int dataCenterBits, int workerBits, int sequenceBits) {
        Assert.isTrue(1+timestampBits+dataCenterBits+workerBits+sequenceBits == 64,"Sum of timestampBits, dataCenterBits,workerBits and sequenceBits requires to be 63.");

        this.sequenceBits = sequenceBits;
        this.workerBits = workerBits;
        this.dataCenterBits = dataCenterBits;
        this.timestampBits = timestampBits;


        maxSequence = ~(-1L << sequenceBits);
        maxWorkerId = ~(-1L << workerBits);
        maxDataCenterId = ~(-1L << dataCenterBits);
        maxTimestamp = ~(-1L << timestampBits);

        workerShift = sequenceBits;
        dataCenterShift = sequenceBits + workerBits;
        timestampShift = dataCenterShift + dataCenterBits;

        sequenceMask = maxSequence;
        workerMask = maxWorkerId << workerShift;
        dataCenterMask = maxDataCenterId << dataCenterShift;
        timestampMask = maxTimestamp << timestampShift;
    }

    public long toId(long timestampOffset, long dataCenterId, long workerId, long sequence) {
        return timestampOffset << timestampShift | dataCenterId << dataCenterShift | workerId << workerShift | sequence;
    }

    public int sequenceOf(long id) {
        return (int) (id & this.sequenceMask);
    }

    public int workerIdOf(long id) {
        return (int) (id & this.workerMask) >> this.workerShift;
    }

    public int dataCenterIdOf(long id) {
        return (int) (id & this.dataCenterMask) >> this.dataCenterShift;
    }

    public long timestampOffsetOf(long id) {
        return (id & this.timestampMask) >> this.timestampShift;
    }

    public void validate(long dataCenterId, long workerId) {
        Assert.isTrue(dataCenterId >= 0 && dataCenterId <= this.maxDataCenterId,String.format("data center id:max[%s],current[%s]",maxDataCenterId,dataCenterId));
        Assert.isTrue(workerId >= 0 && workerId <= this.maxWorkerId,String.format("worker id:max[%s],current[%s]",maxWorkerId,workerId));
    }

    @Override
    public String toString() {
        return String.format("1,timestampBits=%s,maxDataCenterId=%s,maxWorkerId=%s,maxSequence=%s"
                ,timestampBits
                ,maxDataCenterId
                ,maxWorkerId
                ,maxSequence
        );
    }
}
