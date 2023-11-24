package com.jyx.infra.jpa.domain.interceptor;

import org.hibernate.EmptyInterceptor;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author jiangyaxin
 * @since 2021/11/7 20:09
 */
public class NotUpdateNullInterceptor extends EmptyInterceptor {

    @Override
    public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        int[] results = null;
        int count = 0;
        int span = propertyNames.length;

        for (int i = 0; i < span; i++) {
            // 如果字段是null，默认不插入或者更新
            final boolean dirty =
                    null != currentState[i]
                            &&
                            !Objects.deepEquals(currentState[i], previousState[i]);
            if (dirty) {
                if (results == null) {
                    results = new int[span];
                }
                results[count++] = i;
            }
        }

        // 数据完全一致，返回空数组确保不会执行默认的逻辑
        if (count == 0) {
            return new int[0];
        } else {
            return ArrayHelper.trim(results, count);
        }
    }
}