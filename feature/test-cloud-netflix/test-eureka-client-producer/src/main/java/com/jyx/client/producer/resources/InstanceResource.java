package com.jyx.client.producer.resources;

import com.jyx.infra.exception.BusinessException;
import com.jyx.infra.exception.MessageCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangyaxin
 * @since 2022/5/15 19:31
 */
@Slf4j
@RestController
@RequestMapping("/v1/instance")
public class InstanceResource {

    @Value("${eureka.instance.hostname}")
    private String hostName;

    @GetMapping("/hostName")
    public String hostName(){
//        Thread.sleep(5000);
//        return hostName;
        throw BusinessException.of(MessageCode.of(40005,"前端信息"),"后台详细信息");
    }
}
