package com.jyx.feature.test.disruptor.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author jiangyaxin
 * @since 2023/1/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Flow {

    private static final long serialVersionUID = 42L;

    public static final String ORDER_ID = "orderId";

    protected String orderId;

    protected String branchId;


}
