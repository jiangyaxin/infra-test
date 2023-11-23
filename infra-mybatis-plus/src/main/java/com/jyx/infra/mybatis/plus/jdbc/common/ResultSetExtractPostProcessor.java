package com.jyx.infra.mybatis.plus.jdbc.common;

import com.jyx.infra.util.CheckResult;

/**
 * @author Archforce
 * @since 2023/11/23 9:43
 */
public interface ResultSetExtractPostProcessor<IN,OUT> {

    CheckResult canProcess(IN in);

    OUT process(IN in);
}
