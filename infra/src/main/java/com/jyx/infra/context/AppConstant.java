package com.jyx.infra.context;

import static com.jyx.infra.context.SpringPropertyKey.SPRING_APPLICATION_NAME;

/**
 * @author JYX
 * @since 2021/11/5 15:25
 */
public interface AppConstant {

    String MODULE = SpringContextHolder.getYmlProperty(SPRING_APPLICATION_NAME);
}
