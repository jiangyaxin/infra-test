package com.jyx.infra.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jyx.infra.exception.JsonException;

import java.util.Map;

/**
 * @author jiangyaxin
 * @since 2023/12/7 16:38
 */
public class JsonUtil {

    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, Object> toMap(String jsonStr) {
        try {
            return objectMapper.reader().readValue(jsonStr, Map.class);
        } catch (Exception e) {
            throw new JsonException(String.format("Convert map error: %s", jsonStr), e);
        }
    }
}
