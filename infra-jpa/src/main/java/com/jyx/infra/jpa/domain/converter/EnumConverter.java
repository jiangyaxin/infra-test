package com.jyx.infra.jpa.domain.converter;

import com.jyx.infra.spring.converter.EnumI;
import com.jyx.infra.spring.converter.EnumJsonConverter;

import javax.persistence.AttributeConverter;

/**
 * @author jiangyaxin
 * @since 2021/11/6 17:50
 */
public abstract class EnumConverter<E extends Enum<E> & EnumI<Code>, Code> extends EnumJsonConverter<E, Code> implements AttributeConverter<E, Code> {

    @Override
    public Code convertToDatabaseColumn(E attribute) {
        return attribute.getCode();
    }

    @Override
    public E convertToEntityAttribute(Code dbData) {
        return EnumI.getEnumByCode(enumClass, dbData);
    }
}
