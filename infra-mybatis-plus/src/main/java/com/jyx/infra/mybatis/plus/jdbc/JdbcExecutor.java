package com.jyx.infra.mybatis.plus.jdbc;

import java.util.List;

/**
 * @author Archforce
 * @since 2023/11/20 13:38
 */
public interface JdbcExecutor {

    <T> void batchInsert(List<T> dataList);

    <T> List<T> batchQueryAll(Class<T> clazz, String where);

}
