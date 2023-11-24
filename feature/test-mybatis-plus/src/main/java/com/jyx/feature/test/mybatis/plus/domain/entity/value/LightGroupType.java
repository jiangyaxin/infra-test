package com.jyx.feature.test.mybatis.plus.domain.entity.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jyx.infra.spring.converter.EnumI;
import com.jyx.infra.spring.converter.EnumJsonConverter;

/**
 * @author jiangyaxin
 * @since 2021/11/6 19:51
 */
@JsonDeserialize(converter = LightGroupType.LightGroupTypeConverter.class)
public enum LightGroupType implements EnumI<Integer> {

    /**
     * 机动车
     */
    VEHICLE(1, "机动车"),
    /**
     * 非机动车
     */
    NON_VEHICLE(2, "非机动车"),
    /**
     * 行人
     */
    PEDESTRIAN(3, "行人"),
    /**
     * 车道
     */
    LANE(4, "车道"),
    /**
     * 可变交通标志
     */
    VARIABLE_TRAFFIC_SIGN(5, "可变交通标志"),
    /**
     * 公交专用
     */
    BUS_DEDICATED(6, "公交专用"),
    /**
     * 有轨电车专用
     */
    TRAM_DEDICATED(7, "有轨电车专用"),
    /**
     * 特殊灯组
     */
    SPECIAL(8, "特殊灯组"),
    /**
     * 跟随通道(环)
     */
    FOLLOW_CHANNEL(9, "跟随通道(环)");


    private final Integer code;

    private final String name;

    LightGroupType(Integer code, String name) {
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

    public static class LightGroupTypeConverter extends EnumJsonConverter<LightGroupType, Integer> {

    }
}
