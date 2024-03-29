package com.jyx.feature.test.mybatis.plus.resource.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import com.jyx.infra.mybatis.plus.query.PageRequest;

/**
 * @author CodeGenerator
 * @since 2023-12-05 15:17:09
 */
@Getter
@Setter
@ApiModel(value = "阶段")
public class StageRequest extends PageRequest {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("阶段名称")
    private String name;

    @ApiModelProperty("创建人")
    private Long createdBy;

    @ApiModelProperty("创建日期")
    private String createdDate;

    @ApiModelProperty("修改人")
    private Long lastModifiedBy;

    @ApiModelProperty("修改日期")
    private String lastModifiedDate;

}
