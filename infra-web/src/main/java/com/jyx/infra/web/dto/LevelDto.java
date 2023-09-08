package com.jyx.infra.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Pattern;

/**
 * @author Archforce
 * @since 2023/9/8 14:42
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LevelDto {


    @Pattern(message = "level设置错误:[TRACE|DEBUG|INFO|WARN|ERROR|OFF]", regexp = "^TRACE|DEBUG|INFO|WARN|ERROR|OFF$")
    private String level;

    private String packagePath = "root";
}
