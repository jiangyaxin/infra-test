package com.jyx.feature.test.mybatis.domain.entity;

import com.jyx.feature.test.mybatis.domain.entity.value.Direction;
import com.jyx.feature.test.mybatis.domain.entity.value.FlowDirection;
import com.jyx.feature.test.mybatis.domain.entity.value.LightGroupType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author jiangyaxin
 * @since 2021/10/20 17:31
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LightGroup {

    private Long id;

    private Integer number;

    private LightGroupType type;

    private Direction direction;

    private FlowDirection flowDirection;

    private Long createdBy;

    private Date createdDate;

    private Long lastModifiedBy;

    private Date lastModifiedDate;

    private Long version;

}
