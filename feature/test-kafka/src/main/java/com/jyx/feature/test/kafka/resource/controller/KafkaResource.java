package com.jyx.feature.test.kafka.resource.controller;

import com.jyx.feature.test.kafka.application.integration.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * @author JYX
 * @since 2021/11/15 19:22
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/kafka")
public class KafkaResource {

    private final KafkaProducer kafkaProducer;

    @PostMapping("/message")
    public void message(@RequestBody @NotBlank(message = "消息不能为空") String message){
        kafkaProducer.sendMessage(message);
    }
}
