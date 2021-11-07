package com.jyx.feature.test.jpa.domain.entity;

import com.jyx.feature.test.jpa.domain.entity.listener.LightGroupListener;
import com.jyx.feature.test.jpa.domain.entity.value.Direction;
import com.jyx.feature.test.jpa.domain.entity.value.FlowDirection;
import com.jyx.feature.test.jpa.domain.entity.value.LightGroupType;
import com.jyx.infra.jpa.domain.audit.Auditable;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

/**
 * @author JYX
 * @since 2021/10/20 17:31
 */
@Getter
@SuperBuilder
@DynamicUpdate
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

    private LightGroupType type;

    private Direction direction;

    private FlowDirection flowDirection;

    @OneToMany(fetch = FetchType.EAGER,cascade = {CascadeType.REMOVE,CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "light_group_id")
    private List<Channel> channelList;

}
