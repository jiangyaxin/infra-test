package com.jyx.feature.test.jpa.domain.entity;

import com.jyx.infra.jpa.id.Identity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author JYX
 * @since 2021/10/20 17:29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_channel")
public class Channel extends Identity {

    private Integer number;

    @ManyToOne(cascade=CascadeType.ALL)
    private LightGroup lightGroup;
}
