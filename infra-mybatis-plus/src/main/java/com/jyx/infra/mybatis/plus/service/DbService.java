package com.jyx.infra.mybatis.plus.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jyx.infra.spring.jdbc.reader.ResultSetExtractPostProcessor;

import java.util.List;

/**
 * @author jiangyaxin
 * @since 2023/10/25 15:19
 */
public interface DbService<T> extends IService<T> {

    void truncate();

    List<T> batchQueryAll(Wrapper<T> queryWrapper);

    <OUT> List<OUT> batchQueryAll(Wrapper<T> queryWrapper, ResultSetExtractPostProcessor<T, OUT> instancePostProcessor);

    int batchInsert(List<T> dataList);
}
