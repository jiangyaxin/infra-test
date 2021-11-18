package com.jyx.infra.event;

import lombok.Getter;

import java.util.Date;

import static com.jyx.infra.context.AppConstant.ID_ALLOCATOR;

/**
 * @author JYX
 * @since 2021/11/9 9:28
 */
@Getter
public abstract class GuavaEvent{

    private Long id;

    private Date publishDate;

    public GuavaEvent(){
        init();
    }

    private void init(){
        this.id = ID_ALLOCATOR.getId();
        this.publishDate = new Date();
    }
}
