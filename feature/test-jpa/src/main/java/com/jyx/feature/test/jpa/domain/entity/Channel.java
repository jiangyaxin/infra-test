package com.jyx.feature.test.jpa.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jyx.infra.jpa.domain.root.AggregateRoot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author jiangyaxin
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
public class Channel extends AggregateRoot {

    private Integer number;

    @ManyToOne
    @JsonIgnore
    private LightGroup lightGroup;
}
