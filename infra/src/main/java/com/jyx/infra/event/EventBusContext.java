package com.jyx.infra.event;

import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author JYX
 * @since 2021/11/9 15:10
 */
@Slf4j
public class EventBusContext {

    private final static EventBus EVENT_BUS = new EventBus();

    private final static Map<Long,EventListener> EVENT_LISTENER_MAP = Maps.newConcurrentMap();

    public static EventListener registerListener(EventListener eventListener){
        if(!log.isDebugEnabled()){
            log.debug("注册EventListener={}",eventListener);
        }
        EVENT_BUS.register(eventListener);
        EVENT_LISTENER_MAP.put(eventListener.getId(),eventListener);
        return eventListener;
    }

    public static EventListener unregisterListener(EventListener eventListener){
        if(!log.isDebugEnabled()){
            log.debug("注销EventListener={}",eventListener);
        }
        EVENT_BUS.unregister(eventListener);
        EVENT_LISTENER_MAP.remove(eventListener.getId());
        return eventListener;
    }

    public static Event postEvent(Event event){
        if(!log.isDebugEnabled()){
            log.debug("发布Event={}",event);
        }
        EVENT_BUS.post(event);
        return event;
    }
}
