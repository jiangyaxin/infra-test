package com.jyx.feature.test.jpa.application.dto;

import com.jyx.infra.web.dto.AuditableDto;
import com.jyx.infra.web.validation.groups.Create;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author jiangyaxin
 * @since 2021/11/6 21:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDto extends AuditableDto {

    @Min(value = 1,message = "灯组通道编号需大于0",groups = {Create.class})
    @NotNull(message = "灯组通道编号不能为空",groups = {Create.class})
    private Integer number;

}
