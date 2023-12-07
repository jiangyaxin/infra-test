package com.jyx.infra.sharding.sphere.rules;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jyx.infra.datetime.StopWatch;
import com.jyx.infra.exception.AppException;
import com.jyx.infra.exception.ReflectionException;
import com.jyx.infra.log.Logs;
import com.jyx.infra.sharding.sphere.ShardingSphereConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.apache.shardingsphere.mode.manager.ContextManager;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.spring.boot.schema.DatabaseNameSetter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author Archforce
 * @since 2023/12/6 16:37
 */
@ConditionalOnBean(value = {ShardingTableRuleRepository.class},
        name = {ShardingSphereConstants.SHARDING_DATASOURCE_BEAN_NAME})
@Component
@EnableScheduling
@Slf4j
public class ShardingTableRuleUpdater implements EnvironmentAware {

    private final ShardingSphereDataSource shardingSphereDataSource;

    private final ObjectMapper objectMapper;

    private final ShardingTableRuleRepository ruleRepository;

    private Environment environment;

    public ShardingTableRuleUpdater(
            @Qualifier(ShardingSphereConstants.SHARDING_DATASOURCE_BEAN_NAME) DataSource datasource,
            ObjectMapper objectMapper,
            ShardingTableRuleRepository ruleRepository) {
        this.shardingSphereDataSource = (ShardingSphereDataSource) datasource;
        this.objectMapper = objectMapper;
        this.ruleRepository = ruleRepository;
    }

    private static String lastRefreshSuccessRule = "";

    private static final AtomicBoolean running = new AtomicBoolean(false);

    public Collection<RuleConfiguration> refresh(boolean force) {
        try {
            if (!running.compareAndSet(false, true)) {
                Logs.debug(log, "Refresh sharding table rule: last task was running.");
                return new ArrayList<>();
            }
            Logs.info(log, "Refresh sharding table rule start.");

            StopWatch stopWatch = StopWatch.ofTask("Load");
            List<? extends ShardingTableRule> newRuleList = ruleRepository.load();
            stopWatch.stop();

            String currentRule = objectMapper.writer().writeValueAsString(newRuleList);
            if (force || Objects.equals(currentRule, lastRefreshSuccessRule)) {
                Logs.debug(log, "Refresh sharding table rule exit: rule has not change.");
                return new ArrayList<>();
            }

            String databaseName = DatabaseNameSetter.getDatabaseName(environment);

            stopWatch.start("Refresh");
            ContextManager contextManager = getContextManager();
            Collection<RuleConfiguration> ruleConfigCollection = contextManager.getMetaDataContexts()
                    .getMetaData().getDatabase(databaseName)
                    .getRuleMetaData()
                    .getConfigurations();

            swapTableRuleConfig(ruleConfigCollection, newRuleList);

            contextManager.alterRuleConfiguration(databaseName, ruleConfigCollection);
            stopWatch.stop();

            lastRefreshSuccessRule = currentRule;
            Logs.info(log, "Refresh sharding table rule success,{} \n" +
                            "RuleConfiguration:\n" +
                            "{} \n",
                    stopWatch.prettyPrint(),
                    objectMapper.writer().writeValueAsString(ruleConfigCollection));
            return ruleConfigCollection;
        } catch (Exception e) {
            throw new AppException("Refresh sharding table rule error.", e);
        } finally {
            running.set(false);
        }
    }

    public void swapTableRuleConfig(Collection<RuleConfiguration> ruleConfigCollection, List<? extends ShardingTableRule> newRuleList) {
        List<ShardingTableRuleConfiguration> newTableRuleConfigList = newRuleList.stream()
                .map(ShardingTableRuleTransformer::toRuleConfiguration)
                .collect(Collectors.toCollection(() -> new ArrayList<>(newRuleList.size())));

        for (RuleConfiguration configuration : ruleConfigCollection) {
            if (!(configuration instanceof ShardingRuleConfiguration)) {
                continue;
            }
            ShardingRuleConfiguration ruleConfig = (ShardingRuleConfiguration) configuration;
            ruleConfig.setTables(newTableRuleConfigList);
            break;
        }
    }

    private ContextManager getContextManager() {
        Field contextManagerField = ReflectionUtils.findField(ShardingSphereDataSource.class, null, ContextManager.class);
        if (contextManagerField == null) {
            throw ReflectionException.of("ShardingSphereDataSource cannot find field ContextManager");
        }
        ReflectionUtils.makeAccessible(contextManagerField);
        Object value = ReflectionUtils.getField(contextManagerField, shardingSphereDataSource);
        if (value == null) {
            throw ReflectionException.of("ShardingSphereDataSource cannot get ContextManager");
        }
        ContextManager contextManager = (ContextManager) value;
        return contextManager;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
