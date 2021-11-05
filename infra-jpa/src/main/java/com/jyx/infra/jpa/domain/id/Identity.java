package com.jyx.infra.jpa.domain.id;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author JYX
 * @since 2021/10/20 17:24
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@MappedSuperclass
public class Identity {

    @Id
    @GeneratedValue(generator = "id.gen")
    @GenericGenerator(name = "id.gen", strategy = "com.jyx.infra.jpa.domain.id.SnowflakeIdGenerator")
    private Long id;

}
