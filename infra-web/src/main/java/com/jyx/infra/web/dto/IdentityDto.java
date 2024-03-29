package com.jyx.infra.web.dto;

import com.jyx.infra.web.validation.groups.Modify;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

/**
 * @author jiangyaxin
 * @since 2021/11/6 22:32
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class IdentityDto {

    @NotNull(message = "Id is required.", groups = Modify.class)
    private Long id;
}
