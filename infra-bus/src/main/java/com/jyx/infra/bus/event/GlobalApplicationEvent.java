package com.jyx.infra.bus.event;

import lombok.Getter;
import lombok.ToString;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

import java.util.Date;

import static com.jyx.infra.context.AppConstant.ID_ALLOCATOR;

/**
 * @author JYX
 * @since 2021/11/18 10:44
 */
@Getter
@ToString(callSuper = true)
public abstract class GlobalApplicationEvent extends RemoteApplicationEvent {

    private Long globalId;

    private Date publishDate;

    /**
     * 序列化使用
     */
    public GlobalApplicationEvent() {
        init();
    }

    public GlobalApplicationEvent(Object source, String originService, String destinationService) {
        super(source, originService, destinationService);
        init();
    }

    public GlobalApplicationEvent(Object source, String originService) {
        super(source, originService);
        init();
    }

    private void init(){
        this.globalId = ID_ALLOCATOR.getId();
        this.publishDate = new Date();
    }
}
