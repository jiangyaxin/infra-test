package com.jyx.feature.test.jpa.domain.entity.listener;

import com.jyx.feature.test.jpa.domain.event.LightGroupSaveEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author jiangyaxin
 * @since 2021/11/9 11:53
 */
@Slf4j
@Component
public class LightGroupDomainListener {

    @Async
    @TransactionalEventListener
    public void handleUserDeactivatedEvent(LightGroupSaveEvent event) {
        log.info("收到领域事件event={}",event);
    }
}
