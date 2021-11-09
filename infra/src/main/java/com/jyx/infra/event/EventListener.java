package com.jyx.infra.event;

import lombok.Getter;

import static com.jyx.infra.context.AppConstant.ID_ALLOCATOR;

/**
 * @author JYX
 * @since 2021/11/9 15:13
 */
@Getter
public abstract class EventListener {

    private Long id;

    public EventListener(){
        init();
    }

    private void init(){
        this.id = ID_ALLOCATOR.getId();
    }
}
