package com.jyx.feature.test.disruptor.resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Archforce
 * @since 2023/9/8 15:03
 */
@Slf4j
@RestController
public class TestDynamicLogBackLevelController {

    @GetMapping("/print")
    public void printLog() {
        log.trace("See me.");
    }
}
