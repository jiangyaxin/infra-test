package com.jyx.feature.test.jpa.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jyx.infra.jpa.domain.audit.Auditable;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author JYX
 * @since 2021/10/20 17:29
 */
@Getter
@DynamicUpdate
@SuperBuilder
@ToString(exclude = {"lightGroup"},callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_channel")
public class Channel extends Auditable {

    private Integer number;

    @ManyToOne
    @JsonIgnore
    private LightGroup lightGroup;
}
