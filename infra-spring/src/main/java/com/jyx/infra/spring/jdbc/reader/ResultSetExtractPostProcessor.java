package com.jyx.infra.spring.jdbc.reader;

import com.jyx.infra.util.CheckResult;

/**
 * @author jiangyaxin
 * @since 2023/11/23 9:43
 */
public interface ResultSetExtractPostProcessor<IN,OUT> {

    CheckResult canProcess(IN in);

    OUT process(IN in);
}
