package com.jyx.infra.spring.jdbc.reader;

import com.jyx.infra.util.CheckResult;

/**
 * @author jiangyaxin
 * @since 2023/11/23 9:46
 */
public class IdentityResultSetExtractPostProcessor implements ResultSetExtractPostProcessor<Object, Object> {

    public final static ResultSetExtractPostProcessor INSTANCE = new IdentityResultSetExtractPostProcessor();

    private IdentityResultSetExtractPostProcessor() {
    }

    @Override
    public CheckResult canProcess(Object t) {
        return CheckResult.success("IdentityResultSetExtractPostProcessor");
    }

    @Override
    public Object process(Object t) {
        return t;
    }
}
