package com.jyx.feature.test.jpa.domain.entity;

import com.jyx.infra.jpa.id.Identity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
@NamedEntityGraph(name = "fetchChannelList", attributeNodes = {
        @NamedAttributeNode("channelList")
})
public class LightGroup extends Identity{

    private Integer number;

    private Integer type;

    private Integer direction;

    private Integer flowDirection;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "light_group_id")
    private List<Channel> channelList;
}
