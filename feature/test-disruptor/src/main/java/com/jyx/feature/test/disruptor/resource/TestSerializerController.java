package com.jyx.feature.test.disruptor.resource;

import com.jyx.feature.test.disruptor.serializable.SerializerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jiangyaxin
 * @since 2023/9/8 15:03
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TestSerializerController {

    private final SerializerService serializerService;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(8);

    @GetMapping("/serialize")
    public void serialize() {
        for (int i = 0; i < 10000; i++) {
            executorService.submit(() -> serializerService.kryoNioSerializerTest());
        }

//        executorService.submit(() -> serializerService.kryoNioSerializerTest());
    }
}
