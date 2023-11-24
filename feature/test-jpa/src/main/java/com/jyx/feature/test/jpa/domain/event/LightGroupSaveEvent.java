package com.jyx.feature.test.jpa.domain.event;

import com.jyx.feature.test.jpa.domain.entity.LightGroup;
import com.jyx.infra.spring.event.GuavaEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiangyaxin
 * @since 2021/11/9 11:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LightGroupSaveEvent extends GuavaEvent {

    private LightGroup lightGroup;
}
