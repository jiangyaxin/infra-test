package com.jyx.infra.mybatis.plus.service;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author Archforce
 * @since 2023/11/28 15:14
 */
public class WrapperHelper {

    public static <T> String where(Wrapper<T> query) {
        String where = "";
        if (query != null && !query.isEmptyOfWhere()) {
            where = query.getTargetSql();
        }
        return where;
    }

    public static <T> Object[] args(Wrapper<T> query) {
        if (query == null) {
            return new Object[0];
        }
        List<Map.Entry<String, Object>> paramEntryList = new ArrayList<>(((AbstractWrapper) query).getParamNameValuePairs().entrySet());
        Object[] args = paramEntryList.stream()
                .sorted(Comparator.comparingInt(paramEntry -> Integer.parseInt(paramEntry.getKey().substring(Constants.WRAPPER_PARAM.length()))))
                .map(Map.Entry::getValue)
                .toArray();
        return args;
    }
}
