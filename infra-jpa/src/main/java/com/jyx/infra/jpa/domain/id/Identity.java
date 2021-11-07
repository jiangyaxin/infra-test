package com.jyx.infra.jpa.domain.id;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author JYX
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
    @GenericGenerator(name = "id.gen", strategy = "com.jyx.infra.jpa.domain.id.SnowflakeIdGenerator")
    private Long id;
}
