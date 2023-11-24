package com.jyx.feature.test.jpa.application.dto;

import com.jyx.feature.test.jpa.domain.entity.value.Direction;
import com.jyx.feature.test.jpa.domain.entity.value.FlowDirection;
import com.jyx.feature.test.jpa.domain.entity.value.LightGroupType;
import com.jyx.infra.web.dto.AuditableDto;
import com.jyx.infra.web.validation.groups.Create;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author jiangyaxin
 * @since 2021/11/6 20:47
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LightGroupDto extends AuditableDto {

    @Min(value = 1,message = "灯组编号需大于0",groups = {Create.class})
    @NotNull(message = "灯组编号不能为空",groups = {Create.class})
    private Integer number;

    @NotNull(message = "灯组类型不能为空",groups = {Create.class})
    private LightGroupType type;

    @NotNull(message = "方向不能为空",groups = {Create.class})
    private Direction direction;

    @NotNull(message = "流向不能为空",groups = {Create.class})
    private FlowDirection flowDirection;

    @NotEmpty(message = "通道不能为空",groups = {Create.class})
    @Valid
    private List<ChannelDto> channelList;
}
