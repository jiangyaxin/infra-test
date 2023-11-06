package com.jyx.infra.reflection;

import com.jyx.infra.exception.ReflectionException;
import com.jyx.infra.log.Logs;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Archforce
 * @since 2023/11/6 9:28
 */
@Slf4j
public class Constructors {

    public static <T> Constructor<T> findMostArgConstructor(Class<T> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();

        Class<?>[] mostParameters = null;
        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameters = constructor.getParameterTypes();
            int mostParameterLength = mostParameters == null ? Integer.MIN_VALUE : mostParameters.length;
            if (parameters.length > mostParameterLength) {
                mostParameters = parameters;
            }
        }

        try {
            Constructor<T> mostArgConstructor = clazz.getConstructor(mostParameters);
            return mostArgConstructor;
        } catch (NoSuchMethodException e) {
            throw ReflectionException.of(e);
        }
    }

    public static <T> CheckResult newInstancePreCheck(Constructor<T> constructor, Object[] objects) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        String className = constructor.getName();
        if (parameterTypes.length != objects.length) {
            String errorMsg = String.format("New Instance pre check error,the number of parameters is incorrect: %s constructor except %s ,actual %s",
                    className, parameterTypes.length, objects.length);
            return CheckResult.fail(errorMsg);
        }

        for (int index = 0; index < parameterTypes.length; index++) {
            if (parameterTypes[index] != objects[index].getClass()) {
                String errorMsg = String.format("New Instance pre check error, the %s parameter type of %s not matched, %s",
                        index + 1, className, buildConstructorParamErrorMsg(constructor, objects));
                return CheckResult.fail(errorMsg);
            }
        }

        return CheckResult.success("");
    }

    public static <T> T newInstance(Constructor<T> constructor, Object[] objects) {
        try {
            T instance = constructor.newInstance(objects);
            return instance;
        } catch (Exception e) {
            String errorMsg = "New instance error," + buildConstructorParamErrorMsg(constructor, objects);

            Logs.error(log, errorMsg, e);
            throw ReflectionException.of(errorMsg, e);
        }
    }

    private static <T> String buildConstructorParamErrorMsg(Constructor<T> constructor, Object[] objects) {
        List<String> constructorParamClassStrList = Arrays.stream(constructor.getParameterTypes())
                .map(Class::getSimpleName)
                .collect(Collectors.toList());
        List<String> valClassStrList = Arrays.stream(objects)
                .map(object -> object == null ? "null" : object.getClass().getSimpleName())
                .collect(Collectors.toList());
        List<String> valStrList = Arrays.stream(objects)
                .map(object -> object == null ? "null" : object.toString())
                .collect(Collectors.toList());
        String className = constructor.getName();

        String errorMsg = String.format("class: %s \n" +
                        "data: %s \n" +
                        "constructor param type: %s \n" +
                        "data param type       : %s ",
                className, valStrList, constructorParamClassStrList, valClassStrList);
        return errorMsg;
    }


}
