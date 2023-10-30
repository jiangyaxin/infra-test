package com.jyx.infra.mybatis.plus.metadata;

import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Field;

/**
 * @author Archforce
 * @since 2023/10/30 19:54
 */
@Data
public class ColumnInfo {

    private Field field;

    private String column;

    private int length;

    private int scale;

    private String remarks;

    private String defaultValue;

    private boolean nullable;

    private JdbcType jdbcType;

    private boolean primaryKey;

    private boolean autoIncrement;

    private String typeName;

}
