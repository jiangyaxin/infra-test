package com.jyx.feature.test.mybatis.plus.resource.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jyx.feature.test.mybatis.plus.domain.entity.*;
import com.jyx.feature.test.mybatis.plus.pipeline.LogPipeline;
import com.jyx.feature.test.mybatis.plus.repository.repo1.mapper.ChannelMapper;
import com.jyx.feature.test.mybatis.plus.repository.repo1.service.ChannelService;
import com.jyx.feature.test.mybatis.plus.repository.repo1.service.FundSecuAccService;
import com.jyx.feature.test.mybatis.plus.repository.repo1.service.StageService;
import com.jyx.feature.test.mybatis.plus.repository.repo1.service.TblDbfService;
import com.jyx.feature.test.mybatis.plus.repository.repo2.mapper.LightGroupMapper;
import com.jyx.feature.test.mybatis.plus.repository.repo2.service.LightGroupService;
import com.jyx.infra.constant.RuntimeConstant;
import com.jyx.infra.datetime.StopWatch;
import com.jyx.infra.dbf.DbfException;
import com.jyx.infra.dbf.DbfField;
import com.jyx.infra.dbf.DbfFieldTypeEnum;
import com.jyx.infra.dbf.DbfWriter;
import com.jyx.infra.mybatis.plus.DbHolder;
import com.jyx.infra.mybatis.plus.metadata.ColumnInfo;
import com.jyx.infra.spring.jdbc.reader.ResultSetExtractPostProcessor;
import com.jyx.infra.spring.pipeline.PipelineHolder;
import com.jyx.infra.thread.ThreadPoolExecutors;
import com.jyx.infra.util.CheckResult;
import com.jyx.infra.web.excel.WebExcelTransporter;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
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

    private final FundSecuAccService fundSecuAccService;

    private final TblDbfService tblDbfService;

    private final PipelineHolder pipelineHolder;


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

    @ApiOperation(value = "测试接口")
    @GetMapping("/excelExport")
    public void excelExportTest(HttpServletResponse response) {
        ThreadPoolExecutor threadPool = ThreadPoolExecutors.getOrCreateThreadPool("Test-Pool", RuntimeConstant.PROCESSORS * 2, RuntimeConstant.PROCESSORS * 2, 1024);
        StopWatch stopWatch = StopWatch.ofTask("Query");
        LambdaQueryWrapper<Stage> query = new QueryWrapper<Stage>().lambda()
                .lt(Stage::getId, 5000);
        List<Stage> stageList = stageService.batchQueryAll(query);
        stopWatch.stop();
        stopWatch.start("Excel");
        WebExcelTransporter.export(response, "stage", stageList, Stage.class, null, threadPool);
        stopWatch.stop();
        log.info("{}------------{}", stageList.size(), stopWatch.prettyPrint());
    }

    @ApiOperation(value = "测试接口")
    @GetMapping("/db")
    public void dbServiceTest() {
        StopWatch stopWatch = StopWatch.of();
        LambdaQueryWrapper<Stage> query = new QueryWrapper<Stage>().lambda()
                .gt(Stage::getId, 50304);
        List<Stage> stageList = stageService.batchQueryAll(query);
        stopWatch.stop();
        log.info("{}------------{}", stageList.size(), stopWatch.prettyPrint());
    }

    @ApiOperation(value = "测试接口")
    @GetMapping("/db2000")
    public void dbService2000Test() {
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

        StopWatch stopWatch = StopWatch.of();
        List<Void> objectList = fundSecuAccService.batchQueryAll(null, postProcessor);
        stopWatch.stop();
        log.info("{}------------{}", count.sum(), stopWatch.prettyPrint());
    }

    @ApiOperation(value = "测试接口")
    @GetMapping("/dbInsert")
    public void dbInsertTest() {
        int size = 1000_0000;
        List<Stage> dataList = new ArrayList<>(size);
        for (long i = 0; i < size; i++) {
            Stage stage = new Stage(i, "阶段名称", i, new Date(), i, new Date());
            dataList.add(stage);
        }
        stageService.truncate();

        StopWatch stopWatch = StopWatch.of();
        int rowsAffected = stageService.batchInsert(dataList);
        stopWatch.stop();
        log.info("{}------------{}", rowsAffected, stopWatch.prettyPrint());
    }

//    @ApiOperation(value = "测试接口2")
//    @GetMapping("/react")
//    public void reactTest() {
//        LongAdder count = new LongAdder();
//        ResultSetExtractPostProcessor<FundSecuAcc, Void> postProcessor = new ResultSetExtractPostProcessor<>() {
//            @Override
//            public CheckResult canProcess(FundSecuAcc fundSecuAcc) {
//                return CheckResult.success("");
//            }
//
//            @Override
//            public Void process(FundSecuAcc fundSecuAcc) {
//                count.add(1);
//                return null;
//            }
//        };
//
//
//        JdbcTemplate jdbcTemplate = dbHolder.jdbcTemplate(FundSecuAcc.class);
//        String tableName = dbHolder.tableName(FundSecuAcc.class);
//        Constructor<FundSecuAcc> mostArgConstructor = ConstructorUtil.findMostArgConstructor(FundSecuAcc.class);
//
//        Query query = new Query(tableName);
//        StopWatch stopWatch = StopWatch.of();
//        mySqlReactJdbcExecutor.batchQueryAllAndProcess(jdbcTemplate, query,
//                mostArgConstructor, postProcessor,
//                jdbcProperties.getTaskSizeOfEachWorker(), jdbcProperties.getOnceBatchSizeOfEachWorker());
//        stopWatch.stop();
//        log.error("{}=============={}", count.sum(), stopWatch.prettyPrint());
//    }
//
//    @ApiOperation(value = "测试接口4")
//    @GetMapping("/reactInsert")
//    public void reactInsertTest() {
//        JdbcTemplate jdbcTemplate = dbHolder.jdbcTemplate(Stage.class);
//        String tableName = dbHolder.tableName(Stage.class);
//
//        List<ColumnInfo> columnInfoList = dbHolder.columnInfo(Stage.class);
//
//        mySqlReactJdbcExecutor.truncate(jdbcTemplate, tableName);
//
//        int size = 1000_0000;
//        List<Stage> dataList = new ArrayList<>(size);
//        for (long i = 0; i < size; i++) {
//            Stage stage = new Stage(i, "阶段名称", i, new Date(), i, new Date());
//            dataList.add(stage);
//        }
//
//        String[] columns = new String[columnInfoList.size()];
//        int[] columnTypes = new int[columnInfoList.size()];
//        Field[] fields = new Field[columnInfoList.size()];
//        columnInfoList.sort(Comparator.comparingInt(ColumnInfo::getOrder));
//        for (int i = 0; i < columnInfoList.size(); i++) {
//            ColumnInfo columnInfo = columnInfoList.get(i);
//            fields[i] = columnInfo.getField();
//            columnTypes[i] = columnInfo.getJdbcType().TYPE_CODE;
//            columns[i] = columnInfo.getColumn();
//        }
//        Insert insert = new Insert(tableName, columns, columnTypes);
//
//
//        StopWatch stopWatch = StopWatch.ofTask("任务一");
//        BatchInsertResult batchInsertResult = mySqlReactJdbcExecutor.batchInsertAsync(jdbcTemplate,
//                dataList, fields,
//                insert,
//                5000, 500);
//        stopWatch.stop();
//        stopWatch.start("任务二");
//        int rowsAffected = batchInsertResult.getResult();
//        stopWatch.stop();
//
//        log.error("{}=============={}", rowsAffected, stopWatch.prettyPrint());
//    }

//    @ApiOperation(value = "测试接口3")
//    @GetMapping("/multiThread")
//    public void asyncTest() {
//        LongAdder count = new LongAdder();
//        ResultSetExtractPostProcessor<FundSecuAcc, Void> postProcessor = new ResultSetExtractPostProcessor<>() {
//            @Override
//            public CheckResult canProcess(FundSecuAcc fundSecuAcc) {
//                return CheckResult.success("");
//            }
//
//            @Override
//            public Void process(FundSecuAcc fundSecuAcc) {
//                count.add(1);
//                return null;
//            }
//        };
//
//        JdbcTemplate jdbcTemplate = dbHolder.jdbcTemplate(FundSecuAcc.class);
//        String tableName = dbHolder.tableName(FundSecuAcc.class);
//        Constructor<FundSecuAcc> mostArgConstructor = ConstructorUtil.findMostArgConstructor(FundSecuAcc.class);
//        Query query = new Query(tableName, "", new Object[0]);
//        StopWatch stopWatch = StopWatch.of();
//        mySqlMultiThreadJdbcExecutor.batchQueryAllAndProcess(jdbcTemplate, query,
//                mostArgConstructor, postProcessor,
//                jdbcProperties.getTaskSizeOfEachWorker(), jdbcProperties.getOnceBatchSizeOfEachWorker());
//        stopWatch.stop();
//        log.error("{}=============={}", count.sum(), stopWatch.prettyPrint());
//    }

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
