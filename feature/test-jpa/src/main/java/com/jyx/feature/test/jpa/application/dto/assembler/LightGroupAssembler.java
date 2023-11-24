package com.jyx.feature.test.jpa.application.dto.assembler;

import com.jyx.feature.test.jpa.application.dto.LightGroupDto;
import com.jyx.feature.test.jpa.domain.entity.LightGroup;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author jiangyaxin
 * @since 2021/11/6 20:56
 */
public class LightGroupAssembler {

    /**
     * dto to entity
     */
    public static LightGroup toEntity(LightGroupDto lightGroupDto){
        return LightGroupMapper.INSTANCE.toEntity(lightGroupDto);
    }

    @Mapper
    interface LightGroupMapper{

        LightGroupMapper INSTANCE = Mappers.getMapper(LightGroupMapper.class);

        LightGroup toEntity(LightGroupDto lightGroupDto);

        LightGroupDto toDto(LightGroup lightGroup);

    }

}
