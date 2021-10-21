package com.jyx.infra.jpa.id;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

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
    @GenericGenerator(name = "id.gen", strategy = "com.jyx.infra.jpa.id.SnowflakeIdGenerator")
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)){
            return false;
        }
        Identity identity = (Identity) o;
        return id != null && Objects.equals(id, identity.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
