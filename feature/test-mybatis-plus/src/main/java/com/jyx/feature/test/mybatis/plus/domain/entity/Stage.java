package com.jyx.feature.test.mybatis.plus.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * @author CodeGenerator
 * @since 2023-10-29 18:33:17
 */
@Getter
@Setter
@TableName("stage")
public class Stage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private Long id;

    /**
     * 阶段名称
     */
    @TableField("name")
    private String name;

    /**
     * 创建人
     */
    @TableField("created_by")
    private Long createdBy;

    /**
     * 创建日期
     */
    @TableField("created_date")
    private Date createdDate;

    /**
     * 修改人
     */
    @TableField("last_modified_by")
    private Long lastModifiedBy;

    /**
     * 修改日期
     */
    @TableField("last_modified_date")
    private Date lastModifiedDate;

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String CREATED_BY = "created_by";

    public static final String CREATED_DATE = "created_date";

    public static final String LAST_MODIFIED_BY = "last_modified_by";

    public static final String LAST_MODIFIED_DATE = "last_modified_date";
}
