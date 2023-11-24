package com.jyx.feature.test.bus.kafka.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author jiangyaxin
 * @since 2021/11/19 10:45
 */
@Slf4j
@Component
public class TestBusListener {

    /**
     * 断点可能会报
     * Commit cannot be completed since the group has already rebalanced and assigned the partitions to another member.
     * This means that the time between subsequent calls to poll() was longer than the configured max.poll.interval.ms, which typically implies that the poll loop is spending too much time message processing.
     * You can address this either by increasing the session timeout or by reducing the maximum size of batches returned in poll() with max.poll.records.
     * poll调用间隔超过了max.poll.interval.ms的值,这通常表示poll循环中的消息处理花费了太长的时间。
     * 解决方案有两个：1. 增加session.timeout.ms值; 2. 减少max.poll.records值.
     * @param event
     */
    @EventListener
    public void testBusEvent(TestBusEvent event){
        log.info("Event is {}.",event);
    }
}
