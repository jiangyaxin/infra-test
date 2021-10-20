package com.jyx.infra.jpa.id;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author JYX
 * @since 2021/10/20 17:24
 */
@Data
@MappedSuperclass
public class Identity {

    @Id
    @GeneratedValue(generator = "id.gen")
    @GenericGenerator(name = "id.gen", strategy = "com.jyx.infra.jpa.id.SnowflakeIdGenerator")
    private Long id;

}
