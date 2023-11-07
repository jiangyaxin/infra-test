package com.jyx.feature.test.mybatis.plus.resource.response;

import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author CodeGenerator
 * @since 2023-11-07 18:18:34
 */
@Getter
@Setter
@ApiModel(value = "")
public class TblDbfResponse {

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("")
    private String name;

    @ApiModelProperty("")
    private BigDecimal qty;

    @ApiModelProperty("")
    private Date cjsj;

}
