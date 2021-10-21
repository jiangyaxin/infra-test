package com.jyx.feature.test.jpa.domain.entity;

import com.jyx.infra.jpa.id.Identity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * @author JYX
 * @since 2021/10/20 17:31
 */

@Data
@Builder
@ToString
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

    @OneToMany(fetch = FetchType.EAGER,cascade = {CascadeType.REMOVE,CascadeType.PERSIST})
    @JoinColumn(name = "light_group_id")
    private List<Channel> channelList;

}
