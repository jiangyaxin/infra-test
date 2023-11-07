package com.jyx.feature.test.mybatis.plus.resource.request;

import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import com.jyx.infra.mybatis.plus.query.PageRequest;

/**
 * @author CodeGenerator
 * @since 2023-11-07 18:18:34
 */
@Getter
@Setter
@ApiModel(value = "")
public class TblDbfRequest extends PageRequest {

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("")
    private String name;

    @ApiModelProperty("")
    private BigDecimal qty;

    @ApiModelProperty("")
    private String cjsj;

}
