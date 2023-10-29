package com.jyx.infra.mybatis.plus;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.jyx.infra.collection.Maps;
import com.jyx.infra.exception.AppException;
import com.jyx.infra.mybatis.plus.service.DbService;
import com.jyx.infra.mybatis.plus.service.DbServiceImpl;
import org.springframework.aop.framework.Advised;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @author Archforce
 * @since 2023/10/25 15:31
 */
@Component
@DependsOn({"dataSource"})
public class DbHolder {

    private final BiMap<String, Class<?>> tableNameToClassMap;

    private final Map<Class<?>, DbService<?>> classToDbServiceMap;

    private final Map<Class<?>, DataSource> classToDatasourceMap;

    private final DynamicRoutingDataSource dynamicRoutingDataSource;

    public DbHolder(DataSource dataSource, List<DbService<?>> dbServiceList) {
        if (!(dataSource instanceof DynamicRoutingDataSource)) {
            throw AppException.of(String.format("Cannot init DbHolder,DataSource is not DynamicRoutingDataSource."));
        }
        this.dynamicRoutingDataSource = (DynamicRoutingDataSource) dataSource;

        int dbServiceSize = dbServiceList.size();
        this.tableNameToClassMap = HashBiMap.create(dbServiceSize);
        this.classToDbServiceMap = Maps.newHashMap(dbServiceSize);
        this.classToDatasourceMap = Maps.newHashMap(dbServiceSize);

        dbServiceList.forEach(this::bindDbService);
    }

    public String tableName(Class<?> clazz) {
        String tableName = tableNameToClassMap.inverse().get(clazz);
        if (tableName == null) {
            throw AppException.of(String.format("%s not associated with DbService.", clazz.getName()));
        }
        return tableName;
    }

    public Class<?> clazz(String tableName) {
        Class<?> clazz = tableNameToClassMap.get(tableName);
        if (clazz == null) {
            throw AppException.of(String.format("%s not associated with DbService.", clazz.getName()));
        }
        return clazz;
    }

    public DataSource datasource(Class<?> clazz) {
        DataSource datasource = classToDatasourceMap.get(clazz);
        if (datasource == null) {
            throw AppException.of(String.format("%s not associated with DbService.", clazz.getName()));
        }
        return datasource;
    }

    public <T> DbService<T> dbService(Class<T> clazz) {
        DbService<T> dbService = (DbService<T>) classToDbServiceMap.get(clazz);
        if (dbService == null) {
            throw AppException.of(String.format("%s not associated with DbService.", clazz.getName()));
        }
        return dbService;
    }

    public DynamicRoutingDataSource getDynamicRoutingDataSource() {
        return dynamicRoutingDataSource;
    }

    private void bindDbService(DbService<?> dbService) {
        Class<?> noProxyDbServiceClass = ((Advised) dbService).getTargetSource().getTargetClass();
        Class<?> entityClass = dbService.getEntityClass();
        TableName tableNameAnnotation = entityClass.getAnnotation(TableName.class);
        if (tableNameAnnotation == null) {
            throw AppException.of(String.format("%s miss TableName annotation,associated with %s .",
                    entityClass.getName(), noProxyDbServiceClass.getName()));
        }
        this.tableNameToClassMap.put(tableNameAnnotation.value(), entityClass);
        this.classToDbServiceMap.put(entityClass, dbService);

        ResolvableType superType = ResolvableType.forClass(noProxyDbServiceClass).getSuperType();
        if (superType == ResolvableType.NONE || superType.getRawClass() != DbServiceImpl.class) {
            throw AppException.of(String.format("%s not extends %s",
                    noProxyDbServiceClass.getName(), DbServiceImpl.class.getName()));
        }

        Class<?> mapperClass = superType.getGeneric(0).getRawClass();
        DS dsAnnotation = mapperClass.getAnnotation(DS.class);
        if (dsAnnotation == null) {
            throw AppException.of(String.format("%s miss DS annotation.", mapperClass.getName()));
        }
        String datasourceStr = dsAnnotation.value();
        DataSource dataSource = dynamicRoutingDataSource.getDataSources().get(datasourceStr);
        if (dataSource == null) {
            throw AppException.of(String.format("%s datasource not exist,%s config error DS annotation.", datasourceStr, mapperClass.getName()));
        }
        this.classToDatasourceMap.put(entityClass, dataSource);
    }
}
