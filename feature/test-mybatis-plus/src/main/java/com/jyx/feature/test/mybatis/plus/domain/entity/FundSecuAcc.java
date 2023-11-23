package com.jyx.feature.test.mybatis.plus.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.*;

/**
 * @author CodeGenerator
 * @since 2023-11-23 13:24:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("fund_secu_acc")
public class FundSecuAcc implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId("id")
    private Long id;

    @TableField("branch_id")
    private String branchId;

    /**
     * 资金账号
     */
    @TableField("fund_acc_id")
    private String fundAccId;

    /**
     * 币种
     */
    @TableField("currency")
    private String currency;

    /**
     * 证券账户号码
     */
    @TableField("secu_acc_id")
    private String secuAccId;

    /**
     * 市场代码
     */
    @TableField("market")
    private String market;

    /**
     * 客户号
     */
    @TableField("cust_id")
    private String custId;

    /**
     * 证券账户类别
     */
    @TableField("secu_acc_type")
    private String secuAccType;

    /**
     * 资金账户类别
     */
    @TableField("fund_acc_type")
    private String fundAccType;

    /**
     * 佣金分组组号
     */
    @TableField("fee_group_id")
    private String feeGroupId;

    /**
     * 交易柜台号
     */
    @TableField("trade_sys")
    private String tradeSys;

    /**
     * 分区号/节点号
     */
    @TableField("partition_no")
    private String partitionNo;

    /**
     * 交易单元
     */
    @TableField("trade_pbu")
    private String tradePbu;

    /**
     * 托管单元
     */
    @TableField("clear_pbu")
    private String clearPbu;

    /**
     * 证券账户状态
     */
    @TableField("secu_acc_status")
    private String secuAccStatus;

    /**
     * 一码通账户号码
     */
    @TableField("one_code_id")
    private String oneCodeId;

    /**
     * 实时清算标志
     */
    @TableField("rt_clear_flag")
    private String rtClearFlag;

    /**
     * 委托方式
     */
    @TableField("entrust_way")
    private String entrustWay;

    /**
     * 是否检查资金
     */
    @TableField("fund_check_flag")
    private String fundCheckFlag;

    /**
     * 是否检查股份
     */
    @TableField("secu_check_flag")
    private String secuCheckFlag;

    /**
     * 是否黑名单客户
     */
    @TableField("blacklist_flag")
    private String blacklistFlag;

    /**
     * 是否风险客户
     */
    @TableField("risky_flag")
    private String riskyFlag;

    /**
     * 是否限制交易客户
     */
    @TableField("trade_forbid_flag")
    private String tradeForbidFlag;

    /**
     * 是否限售股客户
     */
    @TableField("secu_forbid_flag")
    private String secuForbidFlag;

    /**
     * 是否上市公司董监高客户
     */
    @TableField("senior_flag")
    private String seniorFlag;

    /**
     * 录入人员
     */
    @TableField("user_add")
    private Long userAdd;

    /**
     * 复核人员
     */
    @TableField("user_check")
    private Long userCheck;

    /**
     * 业务权限
     */
    @TableField("auth_biz")
    private String authBiz;

    /**
     * 账户权限
     */
    @TableField("auth_acc")
    private String authAcc;

    /**
     * 备注
     */
    @TableField("comment")
    private String comment;

    /**
     * 持仓单元
     */
    @TableField("acc_unit")
    private String accUnit;

    /**
     * 所属资产单元
     */
    @TableField("asset_unit")
    private String assetUnit;

    /**
     * 是否记账单元
     */
    @TableField("default_acc_unit")
    private String defaultAccUnit;

    /**
     * 数据源id
     */
    @TableField("source_id")
    private Long sourceId;

    /**
     * 执行器名
     */
    @TableField("executor")
    private String executor;

    /**
     * 分片键
     */
    @TableField("sharding")
    private Integer sharding;

    /**
     * 创建时间
     */
    @TableField("time_add")
    private Date timeAdd;

    /**
     * 最后更新时间
     */
    @TableField("time_mod")
    private Date timeMod;

    public static final String ID = "id";

    public static final String BRANCH_ID = "branch_id";

    public static final String FUND_ACC_ID = "fund_acc_id";

    public static final String CURRENCY = "currency";

    public static final String SECU_ACC_ID = "secu_acc_id";

    public static final String MARKET = "market";

    public static final String CUST_ID = "cust_id";

    public static final String SECU_ACC_TYPE = "secu_acc_type";

    public static final String FUND_ACC_TYPE = "fund_acc_type";

    public static final String FEE_GROUP_ID = "fee_group_id";

    public static final String TRADE_SYS = "trade_sys";

    public static final String PARTITION_NO = "partition_no";

    public static final String TRADE_PBU = "trade_pbu";

    public static final String CLEAR_PBU = "clear_pbu";

    public static final String SECU_ACC_STATUS = "secu_acc_status";

    public static final String ONE_CODE_ID = "one_code_id";

    public static final String RT_CLEAR_FLAG = "rt_clear_flag";

    public static final String ENTRUST_WAY = "entrust_way";

    public static final String FUND_CHECK_FLAG = "fund_check_flag";

    public static final String SECU_CHECK_FLAG = "secu_check_flag";

    public static final String BLACKLIST_FLAG = "blacklist_flag";

    public static final String RISKY_FLAG = "risky_flag";

    public static final String TRADE_FORBID_FLAG = "trade_forbid_flag";

    public static final String SECU_FORBID_FLAG = "secu_forbid_flag";

    public static final String SENIOR_FLAG = "senior_flag";

    public static final String USER_ADD = "user_add";

    public static final String USER_CHECK = "user_check";

    public static final String AUTH_BIZ = "auth_biz";

    public static final String AUTH_ACC = "auth_acc";

    public static final String COMMENT = "comment";

    public static final String ACC_UNIT = "acc_unit";

    public static final String ASSET_UNIT = "asset_unit";

    public static final String DEFAULT_ACC_UNIT = "default_acc_unit";

    public static final String SOURCE_ID = "source_id";

    public static final String EXECUTOR = "executor";

    public static final String SHARDING = "sharding";

    public static final String TIME_ADD = "time_add";

    public static final String TIME_MOD = "time_mod";
}
