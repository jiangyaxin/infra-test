package com.jyx.infra.bus.context;

import com.jyx.infra.bus.event.GlobalApplicationEvent;
import com.jyx.infra.log.Logs;
import com.jyx.infra.spring.context.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author jiangyaxin
 * @since 2021/11/18 10:50
 */
@Slf4j
@Component
public class BusContextHolder extends SpringContextHolder {

    /**
     * 先发送到本地,再发送到kafka
     */
    public static <E extends GlobalApplicationEvent> void publishGlobalEvent(E event) {
        applicationEventPublisher.publishEvent(event);
        Logs.debug(log, "Post global application event is {}:{}", event.getGlobalId(), event);
    }

}
