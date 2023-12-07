package com.jyx.feature.test.mybatis.plus.resource.response;

import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author CodeGenerator
 * @since 2023-12-07 15:37:59
 */
@Getter
@Setter
@ApiModel(value = "分片路由配置表")
public class ShardingTableRuleResponse {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("逻辑表名")
    private String logicTable;

    @ApiModelProperty("物理表名，逗号分割")
    private String actualDataNodes;

    @ApiModelProperty("分库策略类型")
    private String databaseShardingStrategyType;

    @ApiModelProperty("分库策略参数，JSON字符串")
    private String databaseShardingStrategyParam;

    @ApiModelProperty("分表策略类型")
    private String tableShardingStrategyType;

    @ApiModelProperty("分表策略参数，JSON字符串")
    private String tableShardingStrategyParam;

    @ApiModelProperty("主键生成策略参数，JSON字符串")
    private String keyGenerateStrategyParam;

    @ApiModelProperty("审计策略参数，JSON字符串")
    private String auditStrategyParam;

    @ApiModelProperty("创建时间")
    private Date createdDate;

    @ApiModelProperty("修改时间")
    private Date lastModifiedDate;

}
