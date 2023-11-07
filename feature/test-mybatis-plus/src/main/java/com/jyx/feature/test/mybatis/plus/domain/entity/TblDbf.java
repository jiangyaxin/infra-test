package com.jyx.feature.test.mybatis.plus.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * @author CodeGenerator
 * @since 2023-11-07 18:18:34
 */
@Getter
@Setter
@TableName("tbl_dbf")
public class TblDbf implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    @TableField("name")
    private String name;

    @TableField("qty")
    private BigDecimal qty;

    @TableField("cjsj")
    private Date cjsj;

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String QTY = "qty";

    public static final String CJSJ = "cjsj";
}
