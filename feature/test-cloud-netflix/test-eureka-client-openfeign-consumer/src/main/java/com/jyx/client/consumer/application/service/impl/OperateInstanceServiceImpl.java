package com.jyx.client.consumer.application.service.impl;

import com.jyx.client.consumer.application.integration.service.InstanceService;
import com.jyx.client.consumer.application.service.OperateInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author jiangyaxin
 * @since 2022/5/15 21:23
 */
@Service
@RequiredArgsConstructor
public class OperateInstanceServiceImpl implements OperateInstanceService {

    private final InstanceService instanceService;

    @Override
    public String getRequestNodeHostName() {
        return instanceService.getRequestNodeHostName();
    }

}