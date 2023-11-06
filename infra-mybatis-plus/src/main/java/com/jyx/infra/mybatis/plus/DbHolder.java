package com.jyx.infra.mybatis.plus;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.jyx.infra.collection.MapUtil;
import com.jyx.infra.exception.AppException;
import com.jyx.infra.log.Logs;
import com.jyx.infra.mybatis.plus.metadata.ColumnInfo;
import com.jyx.infra.mybatis.plus.metadata.MetadataUtil;
import com.jyx.infra.mybatis.plus.service.DbService;
import com.jyx.infra.mybatis.plus.service.DbServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.springframework.aop.framework.Advised;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Archforce
 * @since 2023/10/25 15:31
 */
@Slf4j
@Component
@DependsOn({"dataSource"})
public class DbHolder {

    private final BiMap<String, Class<?>> tableNameToClassMap;

    private final Map<Class<?>, DbService<?>> classToDbServiceMap;

    private final Map<Class<?>, DataSource> classToDatasourceMap;

    private final DynamicRoutingDataSource dynamicRoutingDataSource;

    private final Cache<Class<?>, List<ColumnInfo>> columnInfoCache;

    public DbHolder(DataSource dataSource, List<DbService<?>> dbServiceList) {
        if (!(dataSource instanceof DynamicRoutingDataSource)) {
            throw AppException.of(String.format("Cannot init DbHolder,DataSource is not DynamicRoutingDataSource."));
        }
        this.dynamicRoutingDataSource = (DynamicRoutingDataSource) dataSource;

        int dbServiceSize = dbServiceList.size();
        this.tableNameToClassMap = HashBiMap.create(dbServiceSize);
        this.classToDbServiceMap = MapUtil.newHashMap(dbServiceSize);
        this.classToDatasourceMap = MapUtil.newHashMap(dbServiceSize);
        this.columnInfoCache = CacheBuilder.newBuilder()
                .maximumSize(128)
                .expireAfterAccess(Duration.ofMinutes(5))
                .build();

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

    public DataSource datasource(String datasourceName) {
        DataSource datasource = dynamicRoutingDataSource.getDataSources().get(datasourceName);
        if (datasource == null) {
            throw AppException.of(String.format("Cannot find datasource: .", datasourceName));
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

    public List<ColumnInfo> columnInfo(Class<?> clazz) {
        try {
            return columnInfoCache.get(clazz, () -> loadColumnInfo(clazz));
        } catch (ExecutionException e) {
            throw AppException.of(String.format("%s get column info error.", clazz.getName()));
        }
    }

    public Map<String, ColumnInfo> columnInfoMap(Class<?> clazz) {
        List<ColumnInfo> columnInfoList = columnInfo(clazz);
        Map<String, ColumnInfo> columnInfoMap = columnInfoList.stream()
                .collect(Collectors.toMap(
                        columnInfo -> columnInfo.getColumn().toUpperCase(),
                        Function.identity())
                );
        return columnInfoMap;
    }

    private List<ColumnInfo> loadColumnInfo(Class<?> clazz) {
        DataSource datasource = datasource(clazz);
        String tableName = tableName(clazz);
        Map<String, Field> columnFieldMap = MetadataUtil.columnFieldMap(clazz);

        Connection connection = null;
        try {
            connection = datasource.getConnection();
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            String catalog = connection.getCatalog();

            Set<String> primaryKeySet = new HashSet<>();
            try (ResultSet primaryKeysResultSet = databaseMetaData.getPrimaryKeys(catalog, catalog, tableName)) {
                while (primaryKeysResultSet.next()) {
                    String columnName = primaryKeysResultSet.getString("COLUMN_NAME");
                    primaryKeySet.add(columnName);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Fetch primary key error.", e);
            }

            List<ColumnInfo> columnInfoList = new ArrayList<>();
            try (ResultSet resultSet = databaseMetaData.getColumns(catalog, catalog, tableName, "%")) {
                while (resultSet.next()) {
                    ColumnInfo columnInfo = new ColumnInfo();
                    String column = resultSet.getString("COLUMN_NAME");
                    columnInfo.setColumn(column);
                    columnInfo.setOrder(resultSet.getInt("ORDINAL_POSITION"));
                    columnInfo.setPrimaryKey(primaryKeySet.contains(column));
                    columnInfo.setTypeName(resultSet.getString("TYPE_NAME"));
                    columnInfo.setJdbcType(JdbcType.forCode(resultSet.getInt("DATA_TYPE")));
                    columnInfo.setLength(resultSet.getInt("COLUMN_SIZE"));
                    columnInfo.setScale(resultSet.getInt("DECIMAL_DIGITS"));
                    columnInfo.setRemarks(formatComment(resultSet.getString("REMARKS")));
                    columnInfo.setDefaultValue(resultSet.getString("COLUMN_DEF"));
                    columnInfo.setNullable(resultSet.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
                    columnInfo.setAutoIncrement("YES".equals(resultSet.getString("IS_AUTOINCREMENT")));
                    columnInfo.setField(columnFieldMap.get(column));

                    columnInfoList.add(columnInfo);
                }
            } catch (SQLException e) {
                throw new AppException("Parse column error.", e);
            }
            return List.of(columnInfoList.toArray(new ColumnInfo[0]));
        } catch (Exception ex) {
            throw new AppException(String.format("%s get column info error.", tableName), ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    Logs.error(log, "Close connection Error", e);
                }
            }
        }
    }

    private String formatComment(String comment) {
        return ObjectUtils.isEmpty(comment) ? "" : comment.replaceAll("\r\n", "\t");
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
