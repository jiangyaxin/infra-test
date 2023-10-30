package com.jyx.infra.mybatis.plus.query;

import lombok.Data;

/**
 * @author Archforce
 * @since 2023/10/30 14:07
 */
@Data
public class PageRequest extends OrderRequest {

    private Integer pageSize;

    private Integer pageNumber;
}
