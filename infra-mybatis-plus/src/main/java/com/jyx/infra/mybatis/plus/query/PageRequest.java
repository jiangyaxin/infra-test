package com.jyx.infra.mybatis.plus.query;

import lombok.Data;

/**
 * @author jiangyaxin
 * @since 2023/10/30 14:07
 */
@Data
public class PageRequest extends OrderRequest {

    @QueryIgnore
    private Integer pageSize;

    @QueryIgnore
    private Integer pageNumber;
}
