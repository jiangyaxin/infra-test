package com.jyx.infra.converter;

import com.fasterxml.jackson.annotation.JsonValue;
import com.jyx.infra.exception.BusinessException;

import java.util.EnumSet;
import java.util.Objects;

import static com.jyx.infra.exception.WebMessageCodes.ENUM_CONVERT_ERROR_CODE;

/**
 * @author asa
 * @since 2021/11/6 16:15
 */
public interface EnumI<Code> {

    /**
     * 数据库和前端的值
     * @return code
     */
    @JsonValue
    Code getCode();

    /**
     * 获取详情
     * @return name
     */
    String getName();

    static <E extends Enum<E> & EnumI<Code>,Code> E getEnumByCode(Class<E> enumClass, Code code){
        EnumSet<E> enumSet = EnumSet.allOf(enumClass);
        for(E e : enumSet){
            if(Objects.equals(e.getCode(),code)){
                return e;
            }
        }
        throw BusinessException.of(ENUM_CONVERT_ERROR_CODE,String.format("[%s]没有[code=%s]",enumClass,code));
    }
}
