package com.jyx.infra.spring.pipeline;

import com.jyx.infra.collection.Tuple2;
import com.jyx.infra.collection.Tuples;
import com.jyx.infra.log.Logs;
import com.jyx.infra.pipeline.PipelineException;
import com.jyx.infra.pipeline.PipelineExecuteException;
import com.jyx.infra.pipeline.StageDefinition;
import com.jyx.infra.pipeline.disruptor.StageDefinitionImpl;
import com.jyx.infra.pipeline.disruptor.WaitStrategyProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.bind.DataObjectPropertyName;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jiangyaxin
 * @since 2023/11/3 11:10
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(PipelineConfigProperties.class)
public class PipelineScanner implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final PipelineConfigProperties properties;

    private final PipelineHolder pipelineHolder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Logs.info(log, "Start init pipeline executor.");
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(Pipeline.class);
        Map<String, PipelineProperties> pipelineConfigMap = Optional.ofNullable(properties.getConfig()).orElse(new HashMap<>());

        Set<String> registerPipelineSet = new HashSet<>();
        Set<Map.Entry<String, Object>> beanEntrySet = beanMap.entrySet();
        for (Map.Entry<String, Object> beanEntry : beanEntrySet) {
            String beanName = beanEntry.getKey();
            String dashedFormBeanName = DataObjectPropertyName.toDashedForm(beanName);
            Object bean = beanEntry.getValue();
            Class<?> clazz = bean.getClass();

            Pipeline pipeline = clazz.getAnnotation(Pipeline.class);
            PipelineProperties pipelineProperties = pipelineConfigMap.getOrDefault(beanName, pipelineConfigMap.get(dashedFormBeanName));
            if ((pipelineProperties != null && pipelineProperties.getEnable() != null && !pipelineProperties.getEnable())
                    || !pipeline.enable()) {
                Logs.warn(log, "Pipeline disabled: {}", beanName);
                continue;
            }

            BeanDefinition beanDefinition;
            if (pipelineProperties == null) {
                Tuple2<Class<?>, List<StageDefinition<?>>> stageDefinition = parseStage(bean, new HashMap<>());

                beanDefinition = parsePipeline(bean, beanName, null, stageDefinition);
            } else {
                Map<String, StageProperties> stageConfig = pipelineProperties.getStageConfig();
                Tuple2<Class<?>, List<StageDefinition<?>>> stageDefinition = parseStage(bean, stageConfig);

                beanDefinition = parsePipeline(bean, beanName, pipelineProperties, stageDefinition);
            }
            pipelineHolder.registerPipeline(clazz, beanDefinition);
            registerPipelineSet.add(beanName);
        }
        Logs.info(log, "Registered {} pipelines: {} ", registerPipelineSet.size(), registerPipelineSet);
    }

    public BeanDefinition parsePipeline(Object bean, String beanName, PipelineProperties pipelineProperties, Tuple2<Class<?>, List<StageDefinition<?>>> stageDefinition) {
        Class<?> clazz = bean.getClass();
        Pipeline pipeline = clazz.getAnnotation(Pipeline.class);
        String name = pipeline.name();
        if (!StringUtils.hasText(name)) {
            name = beanName;
        }
        int bufferSize = pipeline.bufferSize();
        WaitStrategy waitStrategyAnnotation = pipeline.waitStrategy();
        long stopTimeout = pipeline.stopTimeout();
        TimeUnit stopTimeUnit = pipeline.stopTimeUnit();
        WaitStrategyProperties waitStrategyProperties = new WaitStrategyProperties(waitStrategyAnnotation.waitMode(), waitStrategyAnnotation.spinTimeout(), waitStrategyAnnotation.timeout(), waitStrategyAnnotation.timeUnit(), waitStrategyAnnotation.fallbackWaitMode());
        if (pipelineProperties != null) {
            bufferSize = pipelineProperties.getBufferSize() > 0 ? pipelineProperties.getBufferSize() : bufferSize;
            stopTimeout = pipelineProperties.getStopTimeout() != null ? pipelineProperties.getStopTimeout() : stopTimeout;
            stopTimeUnit = pipelineProperties.getStopTimeUnit() != null ? pipelineProperties.getStopTimeUnit() : stopTimeUnit;

            WaitStrategyProperties waitStrategyConfig = pipelineProperties.getWaitStrategy();
            if (waitStrategyConfig != null) {
                if (waitStrategyConfig.getWaitMode() != null) {
                    waitStrategyProperties.setWaitMode(waitStrategyConfig.getWaitMode());
                }
                if (waitStrategyConfig.getSpinTimeout() != null) {
                    waitStrategyProperties.setSpinTimeout(waitStrategyConfig.getSpinTimeout());
                }
                if (waitStrategyConfig.getTimeout() != null) {
                    waitStrategyProperties.setTimeout(waitStrategyConfig.getTimeout());
                }
                if (waitStrategyConfig.getTimeout() != null) {
                    waitStrategyProperties.setTimeout(waitStrategyConfig.getTimeout());
                }
                if (waitStrategyConfig.getFallbackWaitMode() != null) {
                    waitStrategyProperties.setFallbackWaitMode(waitStrategyConfig.getFallbackWaitMode());
                }
            }
        }

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SpringPipelineExecutor.class)
                .addConstructorArgValue(name)
                .addConstructorArgValue(bufferSize)
                .addConstructorArgValue(waitStrategyProperties)
                .addConstructorArgValue(stopTimeout)
                .addConstructorArgValue(stopTimeUnit)
                .addConstructorArgValue(stageDefinition.getKey())
                .addConstructorArgValue(stageDefinition.getValue());
        return beanDefinitionBuilder.getBeanDefinition();
    }

    public Tuple2<Class<?>, List<StageDefinition<?>>> parseStage(Object bean, Map<String, StageProperties> stageConfig) {
        Class<?> clazz = bean.getClass();
        stageConfig = Optional.ofNullable(stageConfig).orElse(new HashMap<>());

        List<Method> stageMethodList = fetchStageMethod(clazz);

        checkStageName(stageMethodList, clazz);
        Class<?> dataClass = checkStageParameter(stageMethodList, clazz);

        List<StageDefinition<?>> stageDefinitionList = buildStageDefinition(bean, stageConfig, stageMethodList);

        return Tuples.of(dataClass, stageDefinitionList);
    }

    private List<StageDefinition<?>> buildStageDefinition(Object bean, Map<String, StageProperties> stageConfig, List<Method> stageMethodList) {
        List<StageDefinition<?>> stageDefinitionList = new LinkedList<>();
        stageMethodList.sort(Comparator.comparingInt(method -> method.getAnnotation(Stage.class).order()));
        for (Method method : stageMethodList) {
            Stage stageAnnotation = method.getAnnotation(Stage.class);
            String stageName = stageAnnotation.name();
            if (!StringUtils.hasText(stageName)) {
                stageName = method.getName();
            }
            int parallel = stageAnnotation.parallel();

            String dashedFormStageName = DataObjectPropertyName.toDashedForm(stageName);
            StageProperties stageProperties = stageConfig.getOrDefault(stageName, stageConfig.get(dashedFormStageName));
            if (stageProperties != null) {
                parallel = stageProperties.getParallel() > 0 ? stageProperties.getParallel() : parallel;
            }

            StageDefinition<?> stageDefinition = new StageDefinitionImpl<>(stageName, parallel, event -> {
                try {
                    method.invoke(bean, event.data());
                } catch (Exception e) {
                    throw new PipelineExecuteException(e);
                }
            });
            stageDefinitionList.add(stageDefinition);
        }
        return stageDefinitionList;
    }

    private Class<?> checkStageParameter(List<Method> stageMethodList, Class<?> clazz) {
        Map<? extends Class<?>, List<Method>> parametersToMethodMap = stageMethodList.stream()
                .collect(Collectors.groupingBy(method -> method.getParameterTypes()[0]));
        if (parametersToMethodMap.size() > 1) {
            String detailErrorMsg = stageMethodList.stream()
                    .map(method -> String.format("%s(%s)", method.getName(), method.getParameterTypes()[0].getName()))
                    .collect(Collectors.joining(","));
            throw new PipelineException(String.format("Parameter type of stages must be the same class：%s , %s", clazz.getName(), detailErrorMsg));
        }
        return parametersToMethodMap.keySet().toArray(new Class[0])[0];
    }

    private List<Method> fetchStageMethod(Class<?> clazz) {
        List<Method> stageMethodList = new ArrayList<>();
        ReflectionUtils.doWithMethods(clazz,
                stageMethodList::add,
                method -> {
                    Stage stageAnnotation = method.getAnnotation(Stage.class);
                    if (stageAnnotation == null) {
                        return false;
                    }
                    int modifiers = method.getModifiers();
                    boolean isPublic = Modifier.isPublic(modifiers);
                    if (!isPublic) {
                        throw new PipelineException(String.format("Method is not public: %s#%s", clazz.getName(), method.getName()));
                    }
                    Parameter[] parameters = method.getParameters();
                    if (parameters.length != 1) {
                        throw new PipelineException(String.format("Only one parameter is required: %s#%s", clazz.getName(), method.getName()));
                    }
                    return true;

                });
        if (stageMethodList.isEmpty()) {
            throw new PipelineException(String.format("Pipeline contains at least one stage: %s", clazz.getName()));
        }
        return stageMethodList;
    }

    private void checkStageName(List<Method> stageMethodList, Class<?> clazz) {
        Map<String, Long> stageNameCountMap = stageMethodList.stream()
                .map(method -> {
                    Stage stageAnnotation = method.getAnnotation(Stage.class);
                    String stageName = stageAnnotation.name();
                    if (StringUtils.hasLength(stageName)) {
                        return stageName;
                    } else {
                        return method.getName();
                    }
                })
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        stageNameCountMap.forEach((stageName, count) -> {
            if (count > 1) {
                throw new PipelineException(String.format("Stage name duplicated: %s#%s", clazz.getName(), stageName));
            }
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
