package com.jyx.client.consumer.application.integration.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author jiangyaxin
 * @since 2022/5/15 21:14
 */
@FeignClient(name = "${service.name.producer}",path = "/v1/instance",configuration = {RequestOptionConfiguration.class,RetryConfiguration.class,ErrorDecoderConfiguration.class})
public interface InstanceFeign {

    /**
     * get host name
     * @return hostName
     */
    @GetMapping("/hostName")
    String hostName();
}
