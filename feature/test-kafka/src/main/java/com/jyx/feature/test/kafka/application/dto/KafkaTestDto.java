package com.jyx.feature.test.kafka.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author JYX
 * @since 2021/11/15 19:14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KafkaTestDto {

    private String message;
}
