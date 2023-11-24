package com.jyx.infra.jpa.domain.id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author jiangyaxin
 * @since 2021/10/20 17:24
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@MappedSuperclass
public class Identity {

    @Id
    @GeneratedValue(generator = "id.gen")
    @GenericGenerator(name = "id.gen", strategy = SnowflakeIdGenerator.STRATEGY)
    private Long id;
}
