package com.jyx.infra.web.exception.handler;

import com.jyx.infra.collection.Maps;
import com.jyx.infra.exception.AppException;
import com.jyx.infra.exception.BusinessException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jyx.infra.exception.WebMessageCodes.WRONG_PARAMETER_CODE;

/**
 * @author JYX
 * @since 2021/11/5 17:44
 */
public class ValidationExceptionHandlerSupport {

    private static final String VALIDATION_EXCEPTION_MESSAGE_FIELD_KEY = "field";

    private static final String VALIDATION_EXCEPTION_MESSAGE_MESSAGE_KEY = "message";

    private static final String CONSTRAINT_VIOLATION_EXCEPTION_FIELD_SPLIT = ",";

    private static final String CONSTRAINT_VIOLATION_EXCEPTION_FIELD_METHOD_SPLIT = "\\.";

    private static final String CONSTRAINT_VIOLATION_EXCEPTION_MESSAGE_SPLIT = ":";

    private static final String EXCEPTION_CAN_NOT_HANDLE = "该异常不能解析";

    public static BusinessException transferValidationException(Exception cause){
        List<Map<String, String>> exceptionList;
        if(cause instanceof BindException){
            BindException bindException = (BindException) cause;
            exceptionList  = handleErrors(bindException.getBindingResult().getAllErrors());
        }else if (cause instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) cause;
            exceptionList  = handleErrors(methodArgumentNotValidException.getBindingResult().getAllErrors());
        }else if (cause instanceof ConstraintViolationException){
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) cause;
            exceptionList = handleConstraintViolationException(constraintViolationException);
        }else {
            throw AppException.of(EXCEPTION_CAN_NOT_HANDLE);
        }
        String errorCode = exceptionList.stream()
                .map(map -> map.get(VALIDATION_EXCEPTION_MESSAGE_MESSAGE_KEY))
                .collect(Collectors.joining(","));
        return BusinessException.of(WRONG_PARAMETER_CODE, errorCode,cause);
    }

    private static List<Map<String,String>> handleErrors(List<ObjectError> errors){
        return errors.stream()
                .map(error ->{
                    if (error instanceof FieldError) {
                        FieldError fieldError = (FieldError) error;
                        return Maps.<String,String>builder()
                                .put(VALIDATION_EXCEPTION_MESSAGE_FIELD_KEY,fieldError.getField())
                                .put(VALIDATION_EXCEPTION_MESSAGE_MESSAGE_KEY,fieldError.getDefaultMessage())
                                .build();
                    } else {
                        return Maps.<String,String>builder()
                                .put(VALIDATION_EXCEPTION_MESSAGE_FIELD_KEY,error.getObjectName())
                                .put(VALIDATION_EXCEPTION_MESSAGE_MESSAGE_KEY,error.getDefaultMessage())
                                .build();
                    }
                })
                .collect(Collectors.toList());
    }

    private static List<Map<String,String>> handleConstraintViolationException(ConstraintViolationException cause){
        return Stream.of(cause.getMessage().split(CONSTRAINT_VIOLATION_EXCEPTION_FIELD_SPLIT))
                .map( msg -> {
                    String[] fieldAndMsg = msg.split(CONSTRAINT_VIOLATION_EXCEPTION_MESSAGE_SPLIT);
                    String fieldValue = fieldAndMsg[0].contains(CONSTRAINT_VIOLATION_EXCEPTION_FIELD_METHOD_SPLIT) ? fieldAndMsg[0].split(CONSTRAINT_VIOLATION_EXCEPTION_FIELD_METHOD_SPLIT)[1] : fieldAndMsg[0];
                    return Maps.<String,String>builder()
                            .put(VALIDATION_EXCEPTION_MESSAGE_FIELD_KEY,fieldValue)
                            .put(VALIDATION_EXCEPTION_MESSAGE_MESSAGE_KEY,fieldAndMsg[1])
                            .build();
                })
                .collect(Collectors.toList());
    }
}
