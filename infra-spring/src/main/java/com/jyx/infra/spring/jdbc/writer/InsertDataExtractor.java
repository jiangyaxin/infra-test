package com.jyx.infra.spring.jdbc.writer;

import java.util.List;

/**
 * @author jiangyaxin
 * @since 2023/11/27 14:59
 */
public interface InsertDataExtractor<OUT> {

    <T> OUT extract(List<T> dataList);

}
