package com.jyx.infra.spring.event;

import com.jyx.infra.spring.context.AppConstant;
import lombok.Getter;

import java.util.Date;

/**
 * @author jiangyaxin
 * @since 2021/11/9 9:28
 */
@Getter
public abstract class GuavaEvent {

    private Long id;

    private Date publishDate;

    public GuavaEvent() {
        init();
    }

    private void init() {
        this.id = AppConstant.ID_ALLOCATOR.getId();
        this.publishDate = new Date();
    }
}
