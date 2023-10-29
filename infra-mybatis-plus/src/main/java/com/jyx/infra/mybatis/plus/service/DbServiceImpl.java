package com.jyx.infra.mybatis.plus.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jyx.infra.mybatis.plus.mapper.BizBaseMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Archforce
 * @since 2023/10/25 15:21
 */
@Slf4j
public class DbServiceImpl<M extends BizBaseMapper<T>, T> extends ServiceImpl<M, T> implements DbService<T> {

}
