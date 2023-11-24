package com.jyx.feature.test.jdk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author jiangyaxin
 * @since 2023/1/20
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Flow {

    private static final long serialVersionUID = 42L;

    public static final String ORDER_ID = "orderId";

    protected String orderId;

    protected String branchId;


}
