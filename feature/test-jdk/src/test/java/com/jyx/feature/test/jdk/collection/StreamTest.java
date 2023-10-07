package com.jyx.feature.test.jdk.collection;

import com.google.common.base.Stopwatch;
import com.jyx.feature.test.jdk.FlowNorm;
import com.jyx.infra.collection.Collections;
import com.jyx.infra.datetime.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author jiangyaxin
 * @since 2023/2/19
 */
@Slf4j
public class StreamTest {

    public static <T> Collector<T, ?, T> reducing(Supplier<T> supplier, BinaryOperator<T> reduceOp) {
        return Collector.of(
                supplier,
                reduceOp::apply,
                reduceOp
        );
    }

    @Test
    public void toMapTest() {
        StopWatch stopWatch = StopWatch.ofIdAndTask("Test","任务一");
        List<FlowNorm> flowList = IntStream.range(0, 10)
                .filter(index -> index < 20)
                .mapToObj(index -> {
                    FlowNorm flowNorm = new FlowNorm();
                    flowNorm.setOrderId(String.valueOf(index));
                    flowNorm.setCommFee(BigDecimal.valueOf(index));
                    return flowNorm;
                })
                .collect(Collectors.toList());
        stopWatch.stop();

        stopWatch.start("任务二");
        Map<String, FlowNorm> map = Collections.toMap(flowList, Collectors.toMap(FlowNorm::getOrderId, Function.identity()));
        stopWatch.stop();

        log.info(stopWatch.prettyPrint());
        log.info("##################");
        log.info(stopWatch.toString());
    }

    @Test
    public void groupByTest() {
        IntStream.range(0, 10)
                .filter(index -> index < 20)
                .mapToObj(index -> {
                    FlowNorm flowNorm = new FlowNorm();
                    flowNorm.setOrderId(String.valueOf(index));
                    flowNorm.setCommFee(BigDecimal.valueOf(index));
                    return flowNorm;
                })
                .collect(Collectors.groupingBy(FlowNorm::getOrderId,
                        reducing(
                                FlowNorm::new,
                                (base, incr) -> {
                                    BigDecimal fee = Optional.ofNullable(base.getCommFee()).orElse(BigDecimal.ZERO).add(incr.getCommFee());
                                    base.setCommFee(fee);
                                    return base;
                                }
                        )
                ));
    }

    @Test
    public void test() {
        String s = new String(new byte[]{0x20, 0x20, 0x20});
        log.info("{}",s.length());
    }
}
