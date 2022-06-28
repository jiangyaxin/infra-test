package com.jyx.infra.context;

import lombok.Data;

/**
 * @author JYX
 * @since 2021/10/20 16:48
 */
@Data
public class ClusterContext {

    private Integer dataCenterId = 1;

    private Integer workerId = 1;
}
