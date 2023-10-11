package com.jyx.feature.test.jpa;

import com.jyx.feature.test.jpa.domain.service.LightGroupService;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.util.Set;

/**
 * @author Archforce
 * @since 2023/10/11 17:36
 */
public class ReflectionTest {

    @Test
    public void packageScan() {
        Reflections reflections = new Reflections("com.jyx");
        Set<Class<? extends LightGroupService>> subTypesOf = reflections.getSubTypesOf(LightGroupService.class);
        System.out.println(1);
    }
}
