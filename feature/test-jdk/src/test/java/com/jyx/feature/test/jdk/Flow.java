package com.jyx.feature.test.jdk;

import lombok.Data;

/**
 * @author Archforce
 * @since 2023/1/20
 */
@Data
public class Flow {

    private static final long serialVersionUID = 42L;

    public static final String ORDER_ID = "orderId";

    protected String orderId;

    protected String branchId;
}
