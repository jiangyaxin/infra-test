package com.jyx.feature.test.jpa;

import com.jyx.feature.test.jpa.domain.service.LightGroupService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.util.Set;

/**
 * @author jiangyaxin
 * @since 2023/10/11 17:36
 */
@Slf4j
public class ReflectionTest {

    @Test
    public void packageScan() {
        Reflections reflections = new Reflections("com.jyx");
        Set<Class<? extends LightGroupService>> subTypesOf = reflections.getSubTypesOf(LightGroupService.class);
        System.out.println(1);
    }

    @Test
    public void springPackageScan() {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "/com/jyx/**/*.class";
        Resource[] resources = getResources(resourcePatternResolver, pattern);
        MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        for (Resource resource : resources) {
            MetadataReader reader = getMetadataReader(readerFactory, resource);
            String classname = reader.getClassMetadata().getClassName();
            try {
                Class<?> clazz = Class.forName(classname);
                log.info(clazz.getName());
            } catch (Throwable e) {
                log.error("", e);
            }
        }

    }

    private Resource[] getResources(ResourcePatternResolver resourcePatternResolver, String pattern) {
        try {
            return resourcePatternResolver.getResources(pattern);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private MetadataReader getMetadataReader(MetadataReaderFactory readerFactory, Resource resource) {
        try {
            return readerFactory.getMetadataReader(resource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
