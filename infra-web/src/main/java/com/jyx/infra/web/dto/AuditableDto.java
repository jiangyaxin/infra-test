package com.jyx.infra.web.dto;

import com.jyx.infra.web.validation.groups.Modify;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author asa
 * @since 2021/11/6 22:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuditableDto extends IdentityDto {

    private Long createdBy;

    private Date createdDate;

    private Long lastModifiedBy;

    private Date lastModifiedDate;

    @NotNull(message = "Version is required.", groups = Modify.class)
    @Min(value = 0, message = "Version min is 0.", groups = Modify.class)
    private Long version;
}
