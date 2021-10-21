package com.jyx.feature.test.jpa.domain.entity;

import com.jyx.infra.jpa.id.Identity;
import lombok.*;

import javax.persistence.*;

/**
 * @author JYX
 * @since 2021/10/20 17:29
 */
@Data
@Builder
@ToString(exclude = {"lightGroup"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_channel")
public class Channel extends Identity {

    private Integer number;

    @ManyToOne
    private LightGroup lightGroup;
}
