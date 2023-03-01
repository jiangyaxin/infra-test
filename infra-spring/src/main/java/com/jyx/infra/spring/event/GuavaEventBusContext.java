package com.jyx.infra.spring.event;

import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author JYX
 * @since 2021/11/9 15:10
 */
@Slf4j
public class GuavaEventBusContext {

    private final static EventBus EVENT_BUS = new EventBus();

    private final static Map<Long, GuavaEventListener> EVENT_LISTENER_MAP = Maps.newConcurrentMap();

    public static GuavaEventListener registerListener(GuavaEventListener guavaEventListener) {
        EVENT_BUS.register(guavaEventListener);
        EVENT_LISTENER_MAP.put(guavaEventListener.getId(), guavaEventListener);
        if (log.isDebugEnabled()) {
            log.debug("Register event listener is {}", guavaEventListener.getId());
        }
        return guavaEventListener;
    }

    public static GuavaEventListener unregisterListener(GuavaEventListener guavaEventListener) {
        EVENT_BUS.unregister(guavaEventListener);
        EVENT_LISTENER_MAP.remove(guavaEventListener.getId());
        if (log.isDebugEnabled()) {
            log.debug("Unregister event listener is {}", guavaEventListener.getId());
        }
        return guavaEventListener;
    }

    public static GuavaEvent postEvent(GuavaEvent event) {
        EVENT_BUS.post(event);
        if (log.isDebugEnabled()) {
            log.debug("Post event is {}", event.getId());
        }
        return event;
    }
}
