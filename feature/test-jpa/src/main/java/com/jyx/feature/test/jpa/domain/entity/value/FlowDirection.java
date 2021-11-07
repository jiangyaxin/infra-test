package com.jyx.feature.test.jpa.domain.entity.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jyx.infra.converter.EnumConverter;
import com.jyx.infra.converter.EnumI;

import javax.persistence.Converter;

/**
 * @author asa
 * @since 2021/11/6 19:51
 */
@JsonDeserialize(converter = FlowDirection.FlowDirectionConverter.class)
public enum FlowDirection implements EnumI<Integer> {

    /**
     * 左转
     */
    LEFT(1,"左转"),
    /**
     * 直行
     */
    STRAIGHT(2,"直行"),
    /**
     * 右转
     */
    RIGHT(3,"右转"),
    /**
     * 直左
     */
    STRAIGHT_LEFT(4,"直左"),
    /**
     * 直右
     */
    STRAIGHT_RIGHT(5,"直右"),
    /**
     * 左右
     */
    LEFT_RIGHT(6,"左右"),
    /**
     * 左直右
     */
    LEFT_STRAIGHT_RIGHT(7,"左直右"),
    /**
     * 掉头
     */
    TURN_ROUND(8,"掉头"),
    /**
     * 掉头左转
     */
    TURN_ROUND_LEFT(9,"掉头左转"),
    /**
     * 掉头直行
     */
    TURN_ROUND_STRAIGHT(10,"掉头直行"),
    /**
     * 掉头右转
     */
    TURN_ROUND_RIGHT(11,"掉头右转"),
    /**
     * 掉头直左
     */
    TURN_ROUND_STRAIGHT_LEFT(12,"掉头直左"),
    /**
     * 掉头直右
     */
    TURN_ROUND_STRAIGHT_RIGHT(13,"掉头直右"),
    /**
     * 掉头左右
     */
    TURN_ROUND_LEFT_RIGHT(14,"掉头左右"),
    /**
     * 掉头左直右
     */
    TURN_ROUND_LEFT_STRAIGHT_RIGHT(15,"掉头左直右"),
    /**
     * 行人一段过街
     */
    PEDESTRIAN_CROSSING(16,"行人一段过街"),
    /**
     * 行人一次过街
     */
    PEDESTRIAN_ONCE_CROSSING(17,"行人一次过街"),
    /**
     * 行人二次过街
     */
    PEDESTRIAN_TWICE_CROSSING(18,"行人二次过街");


    private final Integer code;

    private final String name;

    FlowDirection(Integer code,String name){
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

    @Converter(autoApply = true)
    public static class FlowDirectionConverter extends EnumConverter<FlowDirection,Integer> {

    }
}
