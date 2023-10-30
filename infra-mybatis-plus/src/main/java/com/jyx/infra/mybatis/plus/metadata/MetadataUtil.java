package com.jyx.infra.mybatis.plus.metadata;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.google.common.base.CaseFormat;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Archforce
 * @since 2023/10/30 20:46
 */
public class MetadataUtil {

    public static <T> Map<String, Field> columnFieldMap(Class<T> clazz) {
        Map<String, Field> columnFieldMap = new HashMap<>();
        ReflectionUtils.doWithFields(clazz,
                field -> {
                    TableField tableField = field.getAnnotation(TableField.class);
                    if (tableField != null) {
                        columnFieldMap.put(tableField.value(), field);
                        return;
                    }
                    TableId tableId = field.getAnnotation(TableId.class);
                    if (tableId != null) {
                        columnFieldMap.put(tableId.value(), field);
                        return;
                    }
                    columnFieldMap.put(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName()), field);
                },
                field -> {
                    TableField tableField = field.getAnnotation(TableField.class);
                    if (tableField != null) {
                        return true;
                    }
                    TableId tableId = field.getAnnotation(TableId.class);
                    if (tableId != null) {
                        return true;
                    }
                    int modifiers = field.getModifiers();
                    return Modifier.isPrivate(modifiers) || Modifier.isProtected(modifiers);
                });
        return columnFieldMap;
    }
}
