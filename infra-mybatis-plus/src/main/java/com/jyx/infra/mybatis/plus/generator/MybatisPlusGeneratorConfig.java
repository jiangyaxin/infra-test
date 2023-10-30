package com.jyx.infra.mybatis.plus.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.builder.Entity;
import com.baomidou.mybatisplus.generator.config.builder.Mapper;
import com.baomidou.mybatisplus.generator.config.builder.Service;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.jyx.infra.datetime.DateTimeConstant;
import com.jyx.infra.mybatis.plus.DbHolder;
import com.jyx.infra.mybatis.plus.generator.properties.GlobalProperties;
import com.jyx.infra.mybatis.plus.generator.properties.MybatisPlusGeneratorProperties;
import com.jyx.infra.mybatis.plus.generator.properties.PackageProperties;
import com.jyx.infra.mybatis.plus.generator.properties.StrategyProperties;
import com.jyx.infra.mybatis.plus.mapper.BizBaseMapper;
import com.jyx.infra.mybatis.plus.service.DbService;
import com.jyx.infra.mybatis.plus.service.DbServiceImpl;
import com.jyx.infra.util.OsUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author Archforce
 * @since 2023/10/29 12:02
 */
@Configuration
@DependsOn({"dbHolder"})
@ConditionalOnBean(MybatisPlusGeneratorProperties.class)
public class MybatisPlusGeneratorConfig {

    private String PROJECT_ROOT_PATH;

    {
        PROJECT_ROOT_PATH = MybatisPlusGeneratorConfig.class.getResource("/").getPath()
                .replace("/target/classes/", "")
                .replace("/target/test-classes/", "");
        if (OsUtil.isWindows()) {
            if (PROJECT_ROOT_PATH.startsWith("/")) {
                PROJECT_ROOT_PATH = PROJECT_ROOT_PATH.substring(1);
            }
        }
    }


    @Bean
    public FastAutoGenerator fastAutoGenerator(
            DataSourceConfig.Builder dataSourceBuilder,
            MybatisPlusGeneratorProperties mybatisPlusGeneratorProperties) {
        FastAutoGenerator fastAutoGenerator = FastAutoGenerator.create(dataSourceBuilder)
                .globalConfig(builder -> buildGlobalConfig(builder, mybatisPlusGeneratorProperties))
                .packageConfig(builder -> buildPackageConfig(builder, mybatisPlusGeneratorProperties))
                .strategyConfig(builder -> buildStrategyConfig(builder, mybatisPlusGeneratorProperties))
                .templateConfig(builder -> buildTemplateConfig(builder))
                .injectionConfig(builder -> buildInjectionConfig(builder, mybatisPlusGeneratorProperties));
        return fastAutoGenerator;
    }

    @Bean
    public DataSourceConfig.Builder dataSourceBuilder(DbHolder dbHolder, MybatisPlusGeneratorProperties mybatisPlusGeneratorProperties) {
        String datasourceName = mybatisPlusGeneratorProperties.getDatasource().getName();
        DataSource datasource = dbHolder.datasource(datasourceName);

        return new DataSourceConfig.Builder(datasource);
    }

    private void buildGlobalConfig(GlobalConfig.Builder builder, MybatisPlusGeneratorProperties properties) {
        GlobalProperties global = properties.getGlobal();
        builder
                .disableOpenDir()
                .author("CodeGenerator")
                .outputDir(PROJECT_ROOT_PATH + "/src/main/java")
                .commentDate(DateTimeConstant.Patterns.DEFAULT_DATETIME_PATTERN)
                .dateType(DateType.ONLY_DATE);

        if (global.getSwagger()) {
            builder.enableSwagger();
        }
    }

    private void buildPackageConfig(PackageConfig.Builder builder, MybatisPlusGeneratorProperties properties) {
        PackageProperties packageConfig = properties.getPackagePath();
        String datasourceName = properties.getDatasource().getName();

        builder
                .moduleName("")
                .parent(packageConfig.getParent())
                .entity(packageConfig.getEntity())
                .service(packageConfig.getService())
                .serviceImpl(packageConfig.getServiceImpl())
                .mapper(packageConfig.getMapper())
                .pathInfo(Collections.singletonMap(OutputFile.xml, PROJECT_ROOT_PATH + "/src/main/resources/mapper/" + datasourceName));
    }

    private void buildStrategyConfig(StrategyConfig.Builder builder, MybatisPlusGeneratorProperties properties) {
        StrategyProperties strategy = properties.getStrategy();
        String superEntityClass = strategy.getSuperEntityClass();
        boolean fileOverride = strategy.isFileOverride();

        builder.addInclude(strategy.getIncludes());

        Service.Builder serviceBuilder = builder.serviceBuilder();
        serviceBuilder
                .superServiceClass(DbService.class)
                .superServiceImplClass(DbServiceImpl.class)
                .formatServiceFileName("%sService")
                .formatServiceImplFileName("%sServiceImpl");
        if (fileOverride) {
            serviceBuilder.enableFileOverride();
        }


        Entity.Builder entityBuilder = builder.entityBuilder();
        if (fileOverride) {
            entityBuilder.enableFileOverride();
        }
        if (StringUtils.hasLength(superEntityClass)) {
            entityBuilder.superClass(superEntityClass);
        }
        entityBuilder
                .naming(NamingStrategy.underline_to_camel)
                .columnNaming(NamingStrategy.underline_to_camel)
                .enableTableFieldAnnotation()
                .enableLombok()
                .enableColumnConstant();

        Mapper.Builder mapperBuilder = builder.mapperBuilder();
        if (fileOverride) {
            mapperBuilder.enableFileOverride();
        }
        mapperBuilder
                .superClass(BizBaseMapper.class)
                .formatMapperFileName("%sMapper")
                .formatXmlFileName("%sMapper")
                .enableBaseResultMap()
                .enableBaseColumnList()
                .mapperAnnotation(org.apache.ibatis.annotations.Mapper.class);
    }

    private void buildTemplateConfig(TemplateConfig.Builder builder) {
        builder
                .disable(TemplateType.CONTROLLER)
                .service("/templates/service.java")
                .serviceImpl("/templates/serviceImpl.java")
                .entity("/templates/entity.java")
                .mapper("/templates/mapper.java")
                .xml("/templates/mapper.xml");

    }

    private void buildInjectionConfig(InjectionConfig.Builder builder, MybatisPlusGeneratorProperties properties) {
        StrategyProperties strategy = properties.getStrategy();
        boolean fileOverride = strategy.isFileOverride();

        PackageProperties packagePath = properties.getPackagePath();
        String parent = packagePath.getParent();
        String request = packagePath.getRequest();
        String response = packagePath.getResponse();

        String datasourceName = properties.getDatasource().getName();
        Map<String, Object> customMap = new HashMap<>();
        customMap.put("datasourceName", datasourceName);

        List<CustomFile> customFileList = new ArrayList<>();
        // 处理request
        if (StringUtils.hasLength(request)) {
            CustomFile requestFile = buildRequestConfig(request, fileOverride);
            customFileList.add(requestFile);
            customMap.put("requestPackage", parent + "." + request);
        }
        // 处理request
        if (StringUtils.hasLength(response)) {
            CustomFile responseFile = buildResponseConfig(response, fileOverride);
            customFileList.add(responseFile);
            customMap.put("responsePackage", parent + "." + response);
        }

        if (!CollectionUtils.isEmpty(customFileList)) {
            builder.customFile(customFileList);

            builder.beforeOutputFile(buildBeforeOutputFileMap());
        }
        builder.customMap(customMap);
    }

    private CustomFile buildRequestConfig(String request, boolean fileOverride) {
        CustomFile.Builder builder = new CustomFile.Builder()
                .fileName("Request.java")
                .packageName(request)
                .templatePath("/templates/entityRequest.java.vm");

        if (fileOverride) {
            builder.enableFileOverride();
        }

        return builder.build();
    }

    private CustomFile buildResponseConfig(String response, boolean fileOverride) {
        CustomFile.Builder builder = new CustomFile.Builder()
                .fileName("Response.java")
                .packageName(response)
                .templatePath("/templates/entityResponse.java.vm");

        if (fileOverride) {
            builder.enableFileOverride();
        }

        return builder.build();
    }

    private BiConsumer<TableInfo, Map<String, Object>> buildBeforeOutputFileMap() {
        return (tableInfo, customMap) -> {
            List<TableField> fieldList = tableInfo.getFields();

            Set<String> requestImportPackages = new HashSet<>();
            Set<String> responseImportPackages = new HashSet<>();
            fieldList.forEach(field -> {
                IColumnType columnType = field.getColumnType();
                if (null != columnType && null != columnType.getPkg()) {
                    requestImportPackages.add(columnType.getPkg());
                    responseImportPackages.add(columnType.getPkg());
                }
            });
            customMap.put("requestImportPackages", requestImportPackages);
            customMap.put("responseImportPackages", responseImportPackages);
        };
    }

}
