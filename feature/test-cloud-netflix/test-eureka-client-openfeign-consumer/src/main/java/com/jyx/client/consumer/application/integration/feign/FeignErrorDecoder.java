package com.jyx.client.consumer.application.integration.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jyx.infra.exception.MessageCode;
import com.jyx.infra.exception.SystemException;
import com.jyx.infra.result.ErrorResult;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author jiangyaxin
 * @since 2022/5/19 22:12
 */
public class FeignErrorDecoder extends ErrorDecoder.Default {

    private final ObjectMapper objectMapper;

    public FeignErrorDecoder(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String message = Util.toString(response.body().asReader(StandardCharsets.UTF_8));

            ErrorResult errorResult = objectMapper.readValue(message, ErrorResult.class);
            throw SystemException.of(MessageCode.of(50001,"FeignException"),String.format("[%s][%s]-[%s]-[%s]"
                    ,errorResult.getModule()
                    ,errorResult.getCode()
                    ,errorResult.getMessage()
                    ,response.request().url()));
        }catch (IOException ignored) {

        }
        return super.decode(methodKey, response);
    }
}
