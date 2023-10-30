package com.jyx.feature.test.mybatis.plus.resource.request;

import com.jyx.infra.mybatis.plus.query.BetweenType;
import com.jyx.infra.mybatis.plus.query.PageRequest;
import com.jyx.infra.mybatis.plus.query.QueryType;
import com.jyx.infra.mybatis.plus.query.QueryTypes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author CodeGenerator
 * @since 2023-10-30 18:23:55
 */
@Getter
@Setter
@ApiModel(value = "阶段")
public class StageRequest extends PageRequest {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("阶段名称")
    @QueryType(value = QueryTypes.LIKE)
    private String name;

    @ApiModelProperty("创建人")
    @QueryType(value = QueryTypes.IN)
    private String createdBy;

    @ApiModelProperty("创建日期")
    @QueryType(value = QueryTypes.BETWEEN, betweenType = BetweenType.SINGLE_DATE)
    private String createdDate;

    @ApiModelProperty("修改人")
    @QueryType(value = QueryTypes.BETWEEN)
    private String lastModifiedBy;

    @ApiModelProperty("修改日期")
    @QueryType(value = QueryTypes.BETWEEN, betweenType = BetweenType.MULTI_DATE)
    private String lastModifiedDate;

}
