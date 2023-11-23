package com.jyx.infra.mybatis.plus.jdbc.common;

import com.jyx.infra.util.CheckResult;
import com.jyx.infra.util.ConstructorUtil;

import java.lang.reflect.Constructor;

/**
 * @author Archforce
 * @since 2023/11/23 9:46
 */
public class InstancePostProcessorResultSet<T, OUT> implements ResultSetExtractPostProcessor<Object[], OUT> {

    private final Constructor<T> constructor;

    private ResultSetExtractPostProcessor<T, OUT> nextPostProcessor;

    private final boolean clearInstanceAfterPost;

    public InstancePostProcessorResultSet(Constructor<T> constructor) {
        this.constructor = constructor;
        this.nextPostProcessor = IdentityResultSetExtractPostProcessor.INSTANCE;
        this.clearInstanceAfterPost = false;
    }

    public InstancePostProcessorResultSet(Constructor<T> constructor, ResultSetExtractPostProcessor<T, OUT> nextPostProcessor,boolean clearInstanceAfterPost) {
        this.constructor = constructor;
        this.nextPostProcessor = nextPostProcessor;
        this.clearInstanceAfterPost = clearInstanceAfterPost;
    }

    @Override
    public CheckResult canProcess(Object[] objects) {
        CheckResult checkResult = ConstructorUtil.newInstancePreCheck(constructor, objects);
        if (!checkResult.isSuccess()
                || nextPostProcessor == IdentityResultSetExtractPostProcessor.INSTANCE) {
            return checkResult;
        }
        T instance = ConstructorUtil.newInstance(constructor, objects);
        return nextPostProcessor.canProcess(instance);
    }

    @Override
    public OUT process(Object[] objects) {
        T instance = ConstructorUtil.newInstance(constructor, objects);
        try {
            return nextPostProcessor.process(instance);
        } catch (Exception e) {
            throw e;
        } finally {
            if (clearInstanceAfterPost) {
                instance = null;
            }
        }
    }
}
