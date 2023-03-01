package com.jyx.infra.spring.event;

import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.util.Date;

import static com.jyx.infra.spring.context.AppConstant.ID_ALLOCATOR;

/**
 * @author JYX
 * @since 2021/11/18 10:34
 */
@Getter
@ToString(callSuper = true)
public abstract class LocalApplicationEvent extends ApplicationEvent {

    private Long id;

    private Date publishDate;

    public LocalApplicationEvent(Object source) {
        super(source);
        init();
    }

    private void init(){
        this.id = ID_ALLOCATOR.getId();
        this.publishDate = new Date();
    }
}
