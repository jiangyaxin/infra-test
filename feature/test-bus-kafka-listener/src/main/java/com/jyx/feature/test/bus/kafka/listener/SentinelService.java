package com.jyx.feature.test.bus.kafka.listener;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Service;

/**
 * @author jiangyaxin
 * @since 2022/6/26 9:47
 */
@Service
public class SentinelService {

    public String sentinelTest(){
        return "111";
    }
}
