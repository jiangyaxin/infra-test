package com.jyx.feature.test.jpa.domain.entity;

import com.jyx.infra.jpa.id.Identity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * @author JYX
 * @since 2021/10/20 17:31
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_light_group")
public class LightGroup extends Identity{

    private Integer number;

    private Integer type;

    private Integer direction;

    private Integer flowDirection;

    @OneToMany
    @JoinColumn(name = "light_group_id")
    private List<Channel> channelList;
}
