package com.jyx.client.consumer.application.integration.service.impl;

import com.jyx.client.consumer.application.integration.feign.InstanceFeign;
import com.jyx.client.consumer.application.integration.service.InstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author jiangyaxin
 * @since 2022/5/15 21:20
 */
@Service
@RequiredArgsConstructor
public class InstanceServiceImpl implements InstanceService {

    private final InstanceFeign instanceFeign;

    @Override
    public String getRequestNodeHostName() {
        return instanceFeign.hostName();
    }
}
