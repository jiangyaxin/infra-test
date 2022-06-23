package com.jyx.infra.bus.event;

import lombok.Getter;
import lombok.ToString;
import org.springframework.cloud.bus.event.Destination;
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

    /**
     *
     * @param source payload
     * @param originService 事件发送源
     * 例如：
     *      @Autowired private ServiceMatcher serviceMatcher
     *      serviceMatcher.getServiceId()
     * @param destination 事件目的地
     * 样式：  spring.application.name : port : 随机字符串
     * 例如: bus-kafka:30003:9025e7977392de95a3e70249810f924a
     * 如果广播给所有服务的所有实例，则设置为 **
     * 如果广播给 bus-kafka 服务的所有实例，则设置为 bus-kafka:**
     * 如果广播给 bus-kafka 服务的指定实例，则设置为 bus-kafka:30003:9025e7977392de95a3e70249810f924a
     */
    public GlobalApplicationEvent(Object source, String originService, Destination destination) {
        super(source, originService, destination);
        init();
    }

    /**
     * 广播给所有服务
     * @param source payload
     * @param originService 事件发送源
     */
    public GlobalApplicationEvent(Object source, String originService) {
        super(source, originService,DEFAULT_DESTINATION_FACTORY.getDestination(null));
        init();
    }

    private void init(){
        this.globalId = ID_ALLOCATOR.getId();
        this.publishDate = new Date();
    }
}
