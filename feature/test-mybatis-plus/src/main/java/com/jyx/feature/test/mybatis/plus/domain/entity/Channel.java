package com.jyx.feature.test.mybatis.plus.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author jiangyaxin
 * @since 2021/10/20 17:29
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@TableName("tbl_channel")
public class Channel {

    private Long id;

    private Integer number;

    private Long createdBy;

    private Date createdDate;

    private Long lastModifiedBy;

    private Date lastModifiedDate;

    private Long version;
}
