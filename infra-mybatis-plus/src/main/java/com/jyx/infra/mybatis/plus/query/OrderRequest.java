package com.jyx.infra.mybatis.plus.query;

import lombok.Data;

/**
 * @author Archforce
 * @since 2023/10/30 14:05
 */
@Data
public class OrderRequest {

    interface Constants {
        String ASC = "+";
        String DESC = "-";
    }

    @QueryIgnore
    private String[] orderBy;
}
