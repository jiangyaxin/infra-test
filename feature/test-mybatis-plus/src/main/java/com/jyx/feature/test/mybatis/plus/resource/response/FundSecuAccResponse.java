package com.jyx.feature.test.mybatis.plus.resource.response;

import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author CodeGenerator
 * @since 2023-11-23 13:24:31
 */
@Getter
@Setter
@ApiModel(value = "资金证券账户对应表")
public class FundSecuAccResponse {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("")
    private String branchId;

    @ApiModelProperty("资金账号")
    private String fundAccId;

    @ApiModelProperty("币种")
    private String currency;

    @ApiModelProperty("证券账户号码")
    private String secuAccId;

    @ApiModelProperty("市场代码")
    private String market;

    @ApiModelProperty("客户号")
    private String custId;

    @ApiModelProperty("证券账户类别")
    private String secuAccType;

    @ApiModelProperty("资金账户类别")
    private String fundAccType;

    @ApiModelProperty("佣金分组组号")
    private String feeGroupId;

    @ApiModelProperty("交易柜台号")
    private String tradeSys;

    @ApiModelProperty("分区号/节点号")
    private String partitionNo;

    @ApiModelProperty("交易单元")
    private String tradePbu;

    @ApiModelProperty("托管单元")
    private String clearPbu;

    @ApiModelProperty("证券账户状态")
    private String secuAccStatus;

    @ApiModelProperty("一码通账户号码")
    private String oneCodeId;

    @ApiModelProperty("实时清算标志")
    private String rtClearFlag;

    @ApiModelProperty("委托方式")
    private String entrustWay;

    @ApiModelProperty("是否检查资金")
    private String fundCheckFlag;

    @ApiModelProperty("是否检查股份")
    private String secuCheckFlag;

    @ApiModelProperty("是否黑名单客户")
    private String blacklistFlag;

    @ApiModelProperty("是否风险客户")
    private String riskyFlag;

    @ApiModelProperty("是否限制交易客户")
    private String tradeForbidFlag;

    @ApiModelProperty("是否限售股客户")
    private String secuForbidFlag;

    @ApiModelProperty("是否上市公司董监高客户")
    private String seniorFlag;

    @ApiModelProperty("录入人员")
    private Long userAdd;

    @ApiModelProperty("复核人员")
    private Long userCheck;

    @ApiModelProperty("业务权限")
    private String authBiz;

    @ApiModelProperty("账户权限")
    private String authAcc;

    @ApiModelProperty("备注")
    private String comment;

    @ApiModelProperty("持仓单元")
    private String accUnit;

    @ApiModelProperty("所属资产单元")
    private String assetUnit;

    @ApiModelProperty("是否记账单元")
    private String defaultAccUnit;

    @ApiModelProperty("数据源id")
    private Long sourceId;

    @ApiModelProperty("执行器名")
    private String executor;

    @ApiModelProperty("分片键")
    private Integer sharding;

    @ApiModelProperty("创建时间")
    private Date timeAdd;

    @ApiModelProperty("最后更新时间")
    private Date timeMod;

}
