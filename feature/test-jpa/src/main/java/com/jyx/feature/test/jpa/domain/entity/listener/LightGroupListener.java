package com.jyx.feature.test.jpa.domain.entity.listener;

import com.jyx.feature.test.jpa.domain.entity.LightGroup;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.PostPersist;

/**
 * @author JYX
 * @since 2021/11/5 13:02
 */
@Slf4j
public class LightGroupListener {

    @PostPersist
    private void postPersist(LightGroup lightGroup){
        log.info("准备新增灯组{}",lightGroup);
    }
}
