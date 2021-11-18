package com.jyx.infra.bus.context;

import com.jyx.infra.bus.event.GlobalApplicationEvent;
import com.jyx.infra.context.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author JYX
 * @since 2021/11/18 10:50
 */
@Slf4j
@Component
public class BusContextHolder extends SpringContextHolder {

    public static  <E extends GlobalApplicationEvent> void publishGlobalEvent(E event){
        applicationEventPublisher.publishEvent(event);
        if(log.isDebugEnabled()){
            log.debug("Post global application event is {}:{}",event.getGlobalId(),event);
        }

    }

}
