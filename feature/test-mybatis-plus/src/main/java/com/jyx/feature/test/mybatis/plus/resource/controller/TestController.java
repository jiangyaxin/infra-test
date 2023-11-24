package com.jyx.feature.test.mybatis.plus.resource.controller;

import com.jyx.feature.test.mybatis.plus.domain.entity.*;
import com.jyx.feature.test.mybatis.plus.pipeline.LogPipeline;
import com.jyx.feature.test.mybatis.plus.repository.repo1.mapper.ChannelMapper;
import com.jyx.feature.test.mybatis.plus.repository.repo1.service.ChannelService;
import com.jyx.feature.test.mybatis.plus.repository.repo1.service.StageService;
import com.jyx.feature.test.mybatis.plus.repository.repo1.service.TblDbfService;
import com.jyx.feature.test.mybatis.plus.repository.repo2.mapper.LightGroupMapper;
import com.jyx.feature.test.mybatis.plus.repository.repo2.service.LightGroupService;
import com.jyx.infra.datetime.StopWatch;
import com.jyx.infra.dbf.DbfException;
import com.jyx.infra.dbf.DbfField;
import com.jyx.infra.dbf.DbfFieldTypeEnum;
import com.jyx.infra.dbf.DbfWriter;
import com.jyx.infra.mybatis.plus.DbHolder;
import com.jyx.infra.mybatis.plus.metadata.ColumnInfo;
import com.jyx.infra.spring.jdbc.JdbcExecutor;
import com.jyx.infra.spring.jdbc.JdbcProperties;
import com.jyx.infra.spring.jdbc.mysql.MySqlMultiThreadJdbcExecutor;
import com.jyx.infra.spring.jdbc.mysql.MySqlReactJdbcExecutor;
import com.jyx.infra.spring.jdbc.reader.ResultSetExtractPostProcessor;
import com.jyx.infra.spring.pipeline.PipelineHolder;
import com.jyx.infra.util.CheckResult;
import com.jyx.infra.util.ConstructorUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author jiangyaxin
 * @since 2023/10/25 15:34
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/test")
public class TestController {

    private final DbHolder dbHolder;

    private final ChannelMapper channelMapper;

    private final LightGroupMapper lightGroupMapper;

    private final ChannelService channelService;

    private final LightGroupService lightGroupService;

    private final StageService stageService;

    private final TblDbfService tblDbfService;

    private final PipelineHolder pipelineHolder;

    private final JdbcProperties jdbcProperties;

    private final JdbcExecutor mySqlMultiThreadJdbcExecutor = new MySqlMultiThreadJdbcExecutor();

    private final JdbcExecutor mySqlReactJdbcExecutor = new MySqlReactJdbcExecutor();


    @ApiOperation(value = "测试接口")
    @GetMapping
    public void dataSourceTest() {
        Channel channel = channelMapper.selectById(2L);
        LightGroup lightGroup = lightGroupMapper.selectById(1L);
        Channel channelServiceById = channelService.getById(2L);
        LightGroup lightGroupServiceById = lightGroupService.getById(1L);
        Stage stageServiceById = stageService.getById(3L);

        List<ColumnInfo> columnInfoList = dbHolder.columnInfo(Stage.class);
        log.info("------------------");
    }

    @ApiOperation(value = "测试接口2")
    @GetMapping("/react")
    public void oneThreadTest() {
        LongAdder count = new LongAdder();
        ResultSetExtractPostProcessor<FundSecuAcc, Void> postProcessor = new ResultSetExtractPostProcessor<>() {
            @Override
            public CheckResult canProcess(FundSecuAcc fundSecuAcc) {
                return CheckResult.success("");
            }

            @Override
            public Void process(FundSecuAcc fundSecuAcc) {
                count.add(1);
                return null;
            }
        };


        JdbcTemplate jdbcTemplate = dbHolder.jdbcTemplate(FundSecuAcc.class);
        String tableName = dbHolder.tableName(FundSecuAcc.class);
        Constructor<FundSecuAcc> mostArgConstructor = ConstructorUtil.findMostArgConstructor(FundSecuAcc.class);
        StopWatch stopWatch = StopWatch.of();
        mySqlReactJdbcExecutor.batchQueryAllAndProcess(jdbcTemplate,
                tableName, "", "", new Object[0], mostArgConstructor,
                postProcessor,
                jdbcProperties.getTaskSize(), jdbcProperties.getBatchSize());
        stopWatch.stop();
        log.error("{}=============={}", count.sum(), stopWatch.prettyPrint());
    }

    @ApiOperation(value = "测试接口3")
    @GetMapping("/multiThread")
    public void asyncTest() {
        LongAdder count = new LongAdder();
        ResultSetExtractPostProcessor<FundSecuAcc, Void> postProcessor = new ResultSetExtractPostProcessor<>() {
            @Override
            public CheckResult canProcess(FundSecuAcc fundSecuAcc) {
                return CheckResult.success("");
            }

            @Override
            public Void process(FundSecuAcc fundSecuAcc) {
                count.add(1);
                return null;
            }
        };

        JdbcTemplate jdbcTemplate = dbHolder.jdbcTemplate(FundSecuAcc.class);
        String tableName = dbHolder.tableName(FundSecuAcc.class);
        Constructor<FundSecuAcc> mostArgConstructor = ConstructorUtil.findMostArgConstructor(FundSecuAcc.class);
        StopWatch stopWatch = StopWatch.of();
        mySqlMultiThreadJdbcExecutor.batchQueryAllAndProcess(jdbcTemplate,
                tableName, "", "", new Object[0], mostArgConstructor,
                postProcessor,
                jdbcProperties.getTaskSize(), jdbcProperties.getBatchSize());
        stopWatch.stop();
        log.error("{}=============={}", count.sum(), stopWatch.prettyPrint());
    }

    @ApiOperation(value = "Pipeline测试接口")
    @GetMapping("/pipeline/{logId}")
    public void pipelineTest(@PathVariable Integer logId) {
        CompletableFuture<Void> submit = pipelineHolder.getPipeline(LogPipeline.class).submit(logId);
        submit.join();
        log.info("执行完成");
    }

    @ApiOperation(value = "DBF测试接口")
    @GetMapping("/dbf")
    public void dbfTest() {
        List<ColumnInfo> columnInfoList = dbHolder.columnInfo(TblDbf.class);

        List<DbfField> dbfFields = new ArrayList<>(columnInfoList.size());
        int offset = 1;
        for (ColumnInfo columnInfo : columnInfoList) {
            JdbcType jdbcType = columnInfo.getJdbcType();
            switch (jdbcType) {
                case BIT:
                case TINYINT:
                case SMALLINT:
                case INTEGER:
                case BIGINT:
                    DbfField dbfField = new DbfField(
                            columnInfo.getOrder(),
                            columnInfo.getField().getName(),
                            DbfFieldTypeEnum.INTEGER,
                            columnInfo.getLength(),
                            columnInfo.getScale(),
                            offset);
                    dbfFields.add(dbfField);
                    offset += columnInfo.getLength();
                    break;
                case NUMERIC:
                case DECIMAL:
                    dbfField = new DbfField(
                            columnInfo.getOrder(),
                            columnInfo.getField().getName(),
                            DbfFieldTypeEnum.NUMERIC,
                            columnInfo.getLength(),
                            columnInfo.getScale(),
                            offset);
                    dbfFields.add(dbfField);
                    offset += columnInfo.getLength();
                    break;
                case CHAR:
                case VARCHAR:
                    dbfField = new DbfField(
                            columnInfo.getOrder(),
                            columnInfo.getField().getName(),
                            DbfFieldTypeEnum.CHARACTER,
                            columnInfo.getLength(),
                            columnInfo.getScale(),
                            offset);
                    dbfFields.add(dbfField);
                    offset += columnInfo.getLength();
                    break;
                case TIMESTAMP:
                case DATE:
                    dbfField = new DbfField(
                            columnInfo.getOrder(),
                            columnInfo.getField().getName(),
                            DbfFieldTypeEnum.DATE,
                            columnInfo.getLength(),
                            columnInfo.getScale(),
                            offset);
                    dbfFields.add(dbfField);
                    offset += columnInfo.getLength();
                    break;
                default:
                    throw DbfException.of("Not support");
            }
        }

        List<TblDbf> list = tblDbfService.list();

        try (DbfWriter dbfWriter = new DbfWriter("D:\\application\\test.dbf", () -> dbfFields)) {
            Map<String, Object> map = new HashMap<>();

            for (TblDbf tblDbf : list) {
                for (ColumnInfo columnInfo : columnInfoList) {
                    Field field = columnInfo.getField();
                    field.setAccessible(true);
                    JdbcType jdbcType = columnInfo.getJdbcType();
                    Object object = field.get(tblDbf);
                    map.put(field.getName(), jdbcType == JdbcType.BIGINT ? Integer.parseInt(String.valueOf(object)) : object);
                }
                dbfWriter.write(map);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
