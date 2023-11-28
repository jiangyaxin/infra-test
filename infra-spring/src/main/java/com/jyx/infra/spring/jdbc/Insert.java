package com.jyx.infra.spring.jdbc;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jiangyaxin
 * @since 2023/11/25 9:30
 */
@Getter
@AllArgsConstructor
public class Insert {

    private String tableName;

    private String[] columns;

    private int[] columnTypes;
}
