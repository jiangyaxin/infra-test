package com.jyx.feature.test.bus.kafka.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangyaxin
 * @since 2022/6/26 10:17
 */
@RestController
@RequiredArgsConstructor
public class SentinelController {

    private final SentinelService sentinelService;

    @RequestMapping("/sentinel")
    public String sentinelTest(){
        return sentinelService.sentinelTest();
    }
}
