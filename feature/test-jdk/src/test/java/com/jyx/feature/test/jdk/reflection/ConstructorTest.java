package com.jyx.feature.test.jdk.reflection;

import com.jyx.feature.test.jdk.Flow;
import com.jyx.infra.util.CheckResult;
import com.jyx.infra.util.ConstructorUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author jiangyaxin
 * @since 2023/12/6 17:24
 */
@Slf4j
public class ConstructorTest {

    @Test
    public void paramTest() {
        Constructor<Flow> mostArgConstructor = ConstructorUtil.findMostArgConstructor(Flow.class);

        CheckResult checkResult = ConstructorUtil.newInstancePreCheck(mostArgConstructor, new Object[2]);

        log.error(checkResult.getMessage());
        assertThat(checkResult.isSuccess())
                .as("paramTest")
                .isEqualTo(false);
    }
}
