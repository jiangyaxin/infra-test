package com.jyx.feature.test.jdk.reflection;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.function.BinaryOperator;
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
                                    BigDecimal fee = base.getCommFee().add(incr.getCommFee());
                                    base.setCommFee(fee);
                                    return base;
                                }
                        )
                ));
    }
}
