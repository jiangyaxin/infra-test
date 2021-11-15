package com.jyx.feature.test.kafka.application.integration;

import com.jyx.feature.test.kafka.application.dto.KafkaTestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

/**
 * @author JYX
 * @since 2021/11/15 19:12
 */
@Slf4j
@Component
public class KafkaConsumer {

    @StreamListener(Sink.INPUT)
    public void input(KafkaTestDto kafkaTestDto) {
        log.info("接收到消息{}",kafkaTestDto);
    }
}
