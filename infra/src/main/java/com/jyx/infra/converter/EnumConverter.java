package com.jyx.infra.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import javax.persistence.AttributeConverter;
import java.lang.reflect.ParameterizedType;

/**
 * @author asa
 * @since 2021/11/6 17:50
 */
@javax.persistence.Converter
public abstract class EnumConverter<E extends Enum<E> & EnumI<Code>,Code> implements AttributeConverter<E, Code>, Converter<Code,E> {

    @SuppressWarnings("unchecked")
    private final Class<E> enumClass = (Class<E>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @SuppressWarnings("unchecked")
    private final Class<Code> codeClass = (Class<Code>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];

    @Override
    public Code convertToDatabaseColumn(E attribute) {
        return attribute.getCode();
    }

    @Override
    public E convertToEntityAttribute(Code dbData) {
        return EnumI.getEnumByCode(enumClass,dbData);
    }

    @Override
    public E convert(Code value) {
        return EnumI.getEnumByCode(enumClass,value);
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructType(codeClass);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructType(enumClass);
    }
}
