package com.jyx.feature.test.jdk.reflection;

import org.junit.jupiter.api.Test;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * @author Archforce
 * @since 2023/1/20
 */
public class FieldTest {


    @Test
    public void modifierTest() {
        HashMap<String, Field> fieldMap = new HashMap<>();
        ReflectionUtils.doWithFields(FlowNorm.class,
                field -> fieldMap.put(field.getName(), field),
                field -> {
                    int mod= field.getModifiers();
                    if (Modifier.isStatic(mod) && Modifier.isFinal(mod)) {
                        if (field.getType() == String.class) {
                            return true;
                        } else {
                            return false;
                        }
                    }else {
                        return true;
                    }
                });

        System.out.print(fieldMap);
    }
}
