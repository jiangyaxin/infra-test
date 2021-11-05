package com.jyx.feature.test.jpa.domain.entity;

import com.jyx.feature.test.jpa.domain.entity.listener.LightGroupListener;
import com.jyx.infra.jpa.domain.audit.Auditable;
import com.jyx.infra.jpa.domain.id.Identity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * @author JYX
 * @since 2021/10/20 17:31
 */

@Data
@Builder
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_light_group")
@NamedEntityGraph(name = "fetchChannelList", attributeNodes = {
        @NamedAttributeNode("channelList")
})
@EntityListeners(LightGroupListener.class)
public class LightGroup extends Auditable {

    private Integer number;

    private Integer type;

    private Integer direction;

    private Integer flowDirection;

    @OneToMany(fetch = FetchType.EAGER,cascade = {CascadeType.REMOVE,CascadeType.PERSIST})
    @JoinColumn(name = "light_group_id")
    private List<Channel> channelList;

}
