package com.jyx.feature.test.jpa.domain.event;

import com.jyx.feature.test.jpa.domain.entity.LightGroup;
import com.jyx.infra.event.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author JYX
 * @since 2021/11/9 11:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LightGroupSaveEvent extends Event {

    private LightGroup lightGroup;
}
