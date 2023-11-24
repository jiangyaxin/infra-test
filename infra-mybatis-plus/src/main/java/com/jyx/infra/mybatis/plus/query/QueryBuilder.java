package com.jyx.infra.mybatis.plus.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.CaseFormat;
import com.jyx.infra.constant.CommonConstant;
import com.jyx.infra.constant.StringConstant;
import com.jyx.infra.datetime.DateTimeConstant;
import com.jyx.infra.datetime.DateTimeUtil;
import com.jyx.infra.exception.BusinessException;
import com.jyx.infra.exception.WebMessageCodes;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * @author jiangyaxin
 * @since 2023/10/30 14:17
 */
public class QueryBuilder {

    public static <T> QueryWrapper<T> build(Object request, Class<T> entityClazz) {
        QueryWrapper<T> query = new QueryWrapper<>();
        if (request == null) {
            return query;
        }

        ReflectionUtils.doWithFields(
                request.getClass(),
                requestField -> buildForField(query, requestField, request, entityClazz),
                QueryBuilder::filterField
        );

        if (request instanceof OrderRequest) {
            buildForOrderBy((OrderRequest) request, entityClazz, query);
        }

        return query;
    }

    private static <T> void buildForOrderBy(OrderRequest request, Class<T> entityClazz, QueryWrapper<T> query) {
        String[] orderBy = request.getOrderBy();
        if (orderBy != null) {
            for (String orderByStr : orderBy) {
                if (orderByStr.startsWith(OrderRequest.Constants.ASC)) {
                    String orderByFieldName = orderByStr.substring(1);
                    Field orderByField = ReflectionUtils.findField(entityClazz, orderByFieldName);
                    if (orderByField == null) {
                        throw BusinessException.of(WebMessageCodes.WRONG_PARAMETER_CODE,
                                String.format("orderBy error,%s cannot find field:%s",
                                        request.getClass().getName(), orderByFieldName));
                    }
                    query.orderByAsc(orderByFieldName);
                } else if (orderByStr.startsWith(OrderRequest.Constants.DESC)) {
                    String orderByFieldName = orderByStr.substring(1);
                    Field orderByField = ReflectionUtils.findField(entityClazz, orderByFieldName);
                    if (orderByField == null) {
                        throw BusinessException.of(WebMessageCodes.WRONG_PARAMETER_CODE,
                                String.format("orderBy error,%s cannot find field:%s",
                                        request.getClass().getName(), orderByFieldName));
                    }
                    query.orderByDesc(orderByFieldName);
                } else {
                    String orderByFieldName = orderByStr;
                    Field orderByField = ReflectionUtils.findField(entityClazz, orderByFieldName);
                    if (orderByField == null) {
                        throw BusinessException.of(WebMessageCodes.WRONG_PARAMETER_CODE,
                                String.format("orderBy error,%s cannot find field:%s",
                                        request.getClass().getName(), orderByFieldName));
                    }
                    query.orderByAsc(orderByFieldName);
                }
            }
        }
    }

    private static <T> void buildForField(QueryWrapper<T> query, Field requestField,
                                          Object request, Class<T> entityClazz) throws IllegalAccessException {

        requestField.setAccessible(true);

        String entityFieldName;
        QueryType queryTypeAnnotation = requestField.getAnnotation(QueryType.class);
        if (queryTypeAnnotation != null && StringUtils.hasLength(queryTypeAnnotation.entityFieldName())) {
            entityFieldName = queryTypeAnnotation.entityFieldName();
        } else {
            entityFieldName = requestField.getName();
        }
        Field entityField = ReflectionUtils.findField(entityClazz, entityFieldName);
        if (entityField == null) {
            throw BusinessException.of(WebMessageCodes.WRONG_PARAMETER_CODE,
                    String.format("Cannot find %s field in %s", entityFieldName, entityClazz.getName()));
        }

        String dbColumnName = resolveDbColumnName(entityField);
        Object requestFieldValue = requestField.get(request);

        String errorMsg = String.format("%s of %s", requestField.getName(), request.getClass().getName());

        parseQueryType(query, queryTypeAnnotation, dbColumnName, requestFieldValue, errorMsg);
    }

    private static boolean filterField(Field requestField) {
        QueryIgnore queryIgnore = requestField.getAnnotation(QueryIgnore.class);
        return queryIgnore == null && Modifier.isPrivate(requestField.getModifiers());
    }

    private static <T> void parseQueryType(QueryWrapper<T> query, QueryType queryTypeAnnotation,
                                           String dbColumnName, Object requestFieldValue,
                                           String errorMsg) {
        QueryTypes queryType = queryTypeAnnotation == null ? QueryTypes.EQ : queryTypeAnnotation.value();
        switch (queryType) {
            case EQ:
                query.eq(requestFieldValue != null, dbColumnName, requestFieldValue);
                break;
            case NE:
                query.ne(requestFieldValue != null, dbColumnName, requestFieldValue);
                break;
            case GT:
                query.gt(requestFieldValue != null, dbColumnName, requestFieldValue);
                break;
            case LT:
                query.lt(requestFieldValue != null, dbColumnName, requestFieldValue);
                break;
            case GE:
                query.ge(requestFieldValue != null, dbColumnName, requestFieldValue);
                break;
            case LE:
                query.le(requestFieldValue != null, dbColumnName, requestFieldValue);
                break;
            case IN:
                if (requestFieldValue == null) {
                    break;
                }
                if (!(requestFieldValue instanceof String)) {
                    throw BusinessException.of(WebMessageCodes.WRONG_PARAMETER_CODE, String.format("%s is not String.", errorMsg));
                }
                String[] ins = ((String) requestFieldValue).split(StringConstant.COMMA);
                query.in(dbColumnName, Arrays.asList(ins));
                break;
            case BETWEEN:
                if (requestFieldValue == null) {
                    break;
                }
                if (!(requestFieldValue instanceof String)) {
                    throw BusinessException.of(WebMessageCodes.WRONG_PARAMETER_CODE, String.format("%s is not String.", errorMsg));
                }
                parseBetween(query, queryTypeAnnotation, dbColumnName, (String) requestFieldValue, errorMsg);
                break;
            case LIKE:
                query.like(requestFieldValue != null, dbColumnName, requestFieldValue);
                break;
            case LEFT_LIKE:
                query.likeLeft(requestFieldValue != null, dbColumnName, requestFieldValue);
                break;
            case RIGHT_LIKE:
                query.likeRight(requestFieldValue != null, dbColumnName, requestFieldValue);
                break;
            case NOT_LIKE:
                query.notLike(requestFieldValue != null, dbColumnName, requestFieldValue);
                break;
            default:
                throw BusinessException.of(WebMessageCodes.WRONG_PARAMETER_CODE, "Not support QueryTypes:" + queryType.name());
        }
    }

    private static <T> void parseBetween(QueryWrapper<T> query, QueryType queryTypeAnnotation,
                                         String dbColumnName, String requestFieldValue,
                                         String errorMsg) {
        BetweenType betweenType = queryTypeAnnotation.betweenType();
        String[] between = requestFieldValue.split(StringConstant.COMMA);

        String from = between[0];
        String to = between.length == 1 ? "" : between[1];
        boolean fromExist = StringUtils.hasLength(from);
        boolean toExist = StringUtils.hasLength(to);

        switch (betweenType) {
            case SINGLE_DATE:
                if (!DateTimeUtil.checkDate(from, DateTimeConstant.DateTimeFormatters.DEFAULT_DATE_FORMATTER)) {
                    throw BusinessException.of(WebMessageCodes.WRONG_PARAMETER_CODE,
                            String.format("%s Error date,Only support date format:%s",
                                    errorMsg, DateTimeConstant.Patterns.DEFAULT_DATE_PATTERN));
                }
                if (fromExist) {
                    query.ge(dbColumnName, from + " 00:00:00");
                    query.le(dbColumnName, from + " 23:59:59");
                }
                break;
            case MULTI_DATE:
                if (fromExist) {
                    boolean fromIsDateTime = DateTimeUtil.checkDate(from, DateTimeConstant.DateTimeFormatters.DEFAULT_DATETIME_FORMATTER);
                    boolean fromIsDate = DateTimeUtil.checkDate(from, DateTimeConstant.DateTimeFormatters.DEFAULT_DATE_FORMATTER);
                    if (!fromIsDateTime && !fromIsDate) {
                        throw BusinessException.of(WebMessageCodes.WRONG_PARAMETER_CODE,
                                String.format("%s Error date:%s,Only support date format:%s 、%s",
                                        errorMsg, requestFieldValue, DateTimeConstant.Patterns.DEFAULT_DATETIME_PATTERN, DateTimeConstant.Patterns.DEFAULT_DATE_PATTERN));
                    }
                    if (fromIsDateTime) {
                        query.ge(dbColumnName, from);
                    }
                    if (fromIsDate) {
                        query.ge(dbColumnName, from + " 00:00:00");
                    }
                }
                if (toExist) {
                    boolean toIsDate = DateTimeUtil.checkDate(to, DateTimeConstant.DateTimeFormatters.DEFAULT_DATE_FORMATTER);
                    boolean toIsDateTime = DateTimeUtil.checkDate(to, DateTimeConstant.DateTimeFormatters.DEFAULT_DATETIME_FORMATTER);
                    if (!toIsDateTime && !toIsDate) {
                        throw BusinessException.of(WebMessageCodes.WRONG_PARAMETER_CODE, String.format("%s Error date:%s,Only support date format:%s 、%s",
                                errorMsg, requestFieldValue, DateTimeConstant.Patterns.DEFAULT_DATETIME_PATTERN, DateTimeConstant.Patterns.DEFAULT_DATE_PATTERN));
                    }
                    if (toIsDateTime) {
                        query.le(dbColumnName, to);
                    }
                    if (toIsDate) {
                        query.le(dbColumnName, to + " 23:59:59");
                    }
                }
                break;
            case DEFAULT:
            default:
                if (fromExist) {
                    query.ge(dbColumnName, from);
                }
                if (toExist) {
                    query.le(dbColumnName, to);
                }
                break;
        }
    }


    private static String resolveDbColumnName(Field entityField) {
        String dbColumnName;
        TableField tableField = entityField.getAnnotation(TableField.class);
        if (tableField == null) {
            TableId tableId = entityField.getAnnotation(TableId.class);
            if (tableId == null) {
                dbColumnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityField.getName());
            } else {
                dbColumnName = tableId.value();
            }
        } else {
            dbColumnName = tableField.value();
        }
        return dbColumnName;
    }


}
