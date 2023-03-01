package com.jyx.infra.spring.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import java.lang.reflect.ParameterizedType;

/**
 * @author asa
 * @since 2021/11/6 17:50
 */
public abstract class EnumJsonSupport<E extends Enum<E> & EnumI<Code>, Code> implements Converter<Code, E> {

    @SuppressWarnings("unchecked")
    protected final Class<E> enumClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @SuppressWarnings("unchecked")
    protected final Class<Code> codeClass = (Class<Code>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];

    @Override
    public E convert(Code value) {
        return EnumI.getEnumByCode(enumClass, value);
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
