package com.jyx.feature.test.kafka.application.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @author jiangyaxin
 * @since 2021/11/15 19:15
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final Source source;

    public void sendMessage(final String message) {
        log.info("Send message: {}", message);
        MessageChannel messageChannel = source.output();
        messageChannel.send(MessageBuilder
                .withPayload(message)
                .build());
    }
}
