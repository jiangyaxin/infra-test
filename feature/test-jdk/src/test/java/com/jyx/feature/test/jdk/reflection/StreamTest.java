package com.jyx.feature.test.jdk.reflection;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author jiangyaxin
 * @since 2023/2/19
 */
@Slf4j
public class StreamTest {

    @Test
    public void collectorTest() {
        Map<String, List<String>> groupByMap = IntStream.range(0, 10)
                .filter(index -> index < 20)
                .mapToObj(index -> String.valueOf(index))
                .collect(Collectors.groupingBy(Function.identity()));
    }
}
