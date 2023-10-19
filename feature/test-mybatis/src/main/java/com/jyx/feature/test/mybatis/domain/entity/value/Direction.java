package com.jyx.feature.test.mybatis.domain.entity.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jyx.infra.spring.converter.EnumI;
import com.jyx.infra.spring.converter.EnumJsonConverter;

/**
 * 方向
 *
 * @author asa
 * @since 2021/11/6 15:47
 */
@JsonDeserialize(converter = Direction.DirectionConverter.class)
public enum Direction implements EnumI<Integer> {
    /**
     * 东
     */
    EAST(1, "东"),
    /**
     * 西
     */
    WEST(2, "西"),
    /**
     * 南
     */
    SOUTH(3, "南"),
    /**
     * 北
     */
    NORTH(4, "北"),
    /**
     * 东北
     */
    EAST_NORTH(5, "东北"),
    /**
     * 西北
     */
    WEST_NORTH(6, "西北"),
    /**
     * 东南
     */
    EAST_SOUTH(7, "东南"),
    /**
     * 西南
     */
    WEST_SOUTH(8, "西南");


    private final Integer code;

    private final String name;

    Direction(Integer code, String name) {
        this.code = code;
        this.name = name;
    }


    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    public static class DirectionConverter extends EnumJsonConverter<Direction, Integer> {

    }
}
