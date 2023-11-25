package com.jyx.infra.spring.jdbc;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Archforce
 * @since 2023/11/25 9:30
 */
@Getter
@AllArgsConstructor
public class Query {

    private String tableName;

    private String select = "";

    private String where = "";

    private Object[] args = new Object[0];

    public Query(String tableName) {
        this.tableName = tableName;
    }

    public Query(String tableName, String where, Object[] args) {
        this.tableName = tableName;
        this.where = where;
        this.args = args;
    }
}
