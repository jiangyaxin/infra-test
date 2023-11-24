package com.jyx.infra.spring.event;

import com.jyx.infra.spring.context.AppConstant;
import lombok.Getter;

/**
 * @author jiangyaxin
 * @since 2021/11/9 15:13
 */
@Getter
public abstract class GuavaEventListener {

    private Long id;

    public GuavaEventListener() {
        init();
    }

    private void init() {
        this.id = AppConstant.ID_ALLOCATOR.getId();
    }
}
