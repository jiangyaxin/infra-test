package com.jyx.infra.sharding.sphere.rules;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Archforce
 * @since 2023/12/6 16:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShardingTableRule {

    /**
     * 主键
     */
    private Long id;

    /**
     * 逻辑表名
     */
    private String logicTable;

    /**
     * 物理表名，逗号分割
     */
    private String actualDataNodes;

    /**
     * 分库策略类型
     */
    private String databaseShardingStrategyType;

    /**
     * 分库策略参数，JSON字符串
     */
    private String databaseShardingStrategyParam;

    /**
     * 分表策略类型
     */
    private String tableShardingStrategyType;

    /**
     * 分表策略参数，JSON字符串
     */
    private String tableShardingStrategyParam;


    /**
     * 主键生成策略参数，JSON字符串
     */
    private String keyGenerateStrategyParam;

    /**
     * 审计策略参数，JSON字符串
     */
    private String auditStrategyParam;

    /**
     * 创建时间
     */
    private Date createdDate;

    /**
     * 修改时间
     */
    private Date lastModifiedDate;
}
