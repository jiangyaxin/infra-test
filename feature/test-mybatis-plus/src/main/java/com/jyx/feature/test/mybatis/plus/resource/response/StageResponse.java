package com.jyx.feature.test.mybatis.plus.resource.response;

import java.util.Date;
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
public class StageResponse {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("阶段名称")
    private String name;

    @ApiModelProperty("创建人")
    private Long createdBy;

    @ApiModelProperty("创建日期")
    private Date createdDate;

    @ApiModelProperty("修改人")
    private Long lastModifiedBy;

    @ApiModelProperty("修改日期")
    private Date lastModifiedDate;

}
