package com.jyx.infra.context;

import lombok.Data;

/**
 * @author Archforce
 * @since 2023/2/27 13:37
 */
@Data
public class PoolContext {

    public boolean enabledIoPool = false;

    public boolean enabledCalculatePool = false;
}
