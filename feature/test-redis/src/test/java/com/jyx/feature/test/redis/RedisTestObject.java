package com.jyx.feature.test.redis;

import lombok.*;

/**
 * @author jiangyaxin
 * @since 2021/11/22 16:16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RedisTestObject {

    private Long id;

    private String message;

}
