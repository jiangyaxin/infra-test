package com.jyx.client.consumer.resources;

import com.jyx.client.consumer.application.service.OperateInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangyaxin
 * @since 2022/5/15 21:24
 */
@RestController
@RequestMapping("/v1/instance")
@RequiredArgsConstructor
public class InstanceResource {

    private final OperateInstanceService operateInstanceService;

    @RequestMapping("/requestNodeHostName")
    public String requestNodeHostName() {
        return operateInstanceService.getRequestNodeHostName();
    }
}
