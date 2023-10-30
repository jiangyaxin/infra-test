package com.jyx.infra.mybatis.plus.query;

/**
 * @author Archforce
 * @since 2023/10/30 14:48
 */
public enum BetweenType {

    /**
     * 用于字符串、数字等比较，如 1,5 转换为 >=1 & <=5
     */
    DEFAULT,

    /**
     * 用于查询某一天，如 2023-10-30 转换为  >= 2023-10-30 00:00:00 & <= 2023-10-30 23:59:59
     */
    SINGLE_DATE,

    /**
     * 用于跨日期查询，如 2023-10-30,2023-11-30 转换为 >= 2023-10-30 00:00:00 & <= 2023-11-30 23:59:59
     */
    MULTI_DATE,
}
