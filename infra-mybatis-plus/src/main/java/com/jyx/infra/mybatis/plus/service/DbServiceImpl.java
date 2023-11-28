package com.jyx.infra.mybatis.plus.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jyx.infra.mybatis.plus.DbHolder;
import com.jyx.infra.mybatis.plus.mapper.BizBaseMapper;
import com.jyx.infra.mybatis.plus.metadata.ColumnInfo;
import com.jyx.infra.spring.context.SpringContextHolder;
import com.jyx.infra.spring.jdbc.Insert;
import com.jyx.infra.spring.jdbc.JdbcExecutor;
import com.jyx.infra.spring.jdbc.Query;
import com.jyx.infra.spring.jdbc.properties.InsertProperties;
import com.jyx.infra.spring.jdbc.properties.JdbcProperties;
import com.jyx.infra.spring.jdbc.properties.QueryProperties;
import com.jyx.infra.spring.jdbc.reader.IdentityResultSetExtractPostProcessor;
import com.jyx.infra.spring.jdbc.reader.ResultSetExtractPostProcessor;
import com.jyx.infra.util.ConstructorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;

/**
 * @author jiangyaxin
 * @since 2023/10/25 15:21
 */
@Slf4j
public class DbServiceImpl<M extends BizBaseMapper<T>, T> extends ServiceImpl<M, T> implements DbService<T> {

    private final Class<T> clazz = (Class<T>) ResolvableType.forClass(this.getClass()).getSuperType().getGeneric(1).resolve();

    private DbHolder dbHolder;

    @Autowired
    private JdbcExecutor jdbcExecutor;

    @Autowired
    private JdbcProperties jdbcProperties;

    private void validate() {
        init();

        if (jdbcExecutor == null) {
            throw new UnsupportedOperationException("JdbcExecutor must be required.");
        }
        if (dbHolder == null) {
            throw new UnsupportedOperationException("DbHolder must be required.");
        }
        if (jdbcProperties == null) {
            throw new UnsupportedOperationException("JdbcProperties must be required.");
        }
    }

    private void init() {
        if (dbHolder == null) {
            synchronized (this) {
                if (dbHolder == null) {
                    dbHolder = SpringContextHolder.getBean(DbHolder.class);
                }
            }
        }
    }

    @Override
    public void truncate() {
        validate();

        JdbcTemplate jdbcTemplate = dbHolder.jdbcTemplate(clazz);
        String tableName = dbHolder.tableName(clazz);

        jdbcExecutor.truncate(jdbcTemplate, tableName);
    }

    @Override
    public List<T> batchQueryAll(Wrapper<T> queryWrapper) {
        List<T> result = batchQueryAll(queryWrapper, IdentityResultSetExtractPostProcessor.INSTANCE);
        return result;
    }

    @Override
    public <OUT> List<OUT> batchQueryAll(Wrapper<T> queryWrapper, ResultSetExtractPostProcessor<T, OUT> instancePostProcessor) {
        validate();

        JdbcTemplate jdbcTemplate = dbHolder.jdbcTemplate(clazz);
        String tableName = dbHolder.tableName(clazz);

        String where = WrapperHelper.where(queryWrapper);
        Object[] args = WrapperHelper.args(queryWrapper);
        Query query = new Query(tableName, where, args);

        Constructor<T> mostArgConstructor = ConstructorUtil.findMostArgConstructor(clazz);
        QueryProperties queryProperties = jdbcProperties.getQuery();

        List<OUT> result = jdbcExecutor.batchQueryAllAndProcess(jdbcTemplate, query,
                mostArgConstructor, instancePostProcessor,
                queryProperties.getTaskSizeOfEachWorker(), queryProperties.getOnceBatchSizeOfEachWorker());
        return result;
    }

    @Override
    public int batchInsert(List<T> dataList) {
        validate();

        JdbcTemplate jdbcTemplate = dbHolder.jdbcTemplate(clazz);
        String tableName = dbHolder.tableName(clazz);
        List<ColumnInfo> columnInfoList = dbHolder.columnInfo(clazz);

        String[] columns = new String[columnInfoList.size()];
        int[] columnTypes = new int[columnInfoList.size()];
        Field[] fields = new Field[columnInfoList.size()];
        columnInfoList.sort(Comparator.comparingInt(ColumnInfo::getOrder));
        for (int i = 0; i < columnInfoList.size(); i++) {
            ColumnInfo columnInfo = columnInfoList.get(i);
            fields[i] = columnInfo.getField();
            columnTypes[i] = columnInfo.getJdbcType().TYPE_CODE;
            columns[i] = columnInfo.getColumn();
        }
        Insert insert = new Insert(tableName, columns, columnTypes);
        InsertProperties insertProperties = jdbcProperties.getInsert();

        int rowsAffected = jdbcExecutor.batchInsert(jdbcTemplate,
                dataList, fields,
                insert,
                insertProperties.getTaskSizeOfEachWorker(), insertProperties.getOnceBatchSizeOfEachWorker());
        return rowsAffected;
    }
}
