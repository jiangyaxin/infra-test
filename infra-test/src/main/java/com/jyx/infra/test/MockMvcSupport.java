package com.jyx.infra.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author jiangyaxin
 * @since 2021/11/7 11:13
 */
public class MockMvcSupport {

    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer().withDefaultPrettyPrinter();

    public static ResultActions prepareRequest(final MockMvc mockMvc, final MockHttpServletRequestBuilder saveRequestBuilder) throws Exception {
        return prepareRequest(mockMvc, saveRequestBuilder, null);
    }

    public static ResultActions prepareRequest(final MockMvc mockMvc, final MockHttpServletRequestBuilder saveRequestBuilder, final Object object) throws Exception {
        return prepareRequest(mockMvc, saveRequestBuilder, OBJECT_WRITER.writeValueAsString(object));
    }

    public static ResultActions prepareRequest(final MockMvc mockMvc, final MockHttpServletRequestBuilder saveRequestBuilder, final String content) throws Exception {
        saveRequestBuilder.contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name());
        if (Objects.nonNull(content)) {
            saveRequestBuilder.content(content);
        }
        ResultActions actions = mockMvc.perform(saveRequestBuilder);
        actions.andReturn().getResponse().setCharacterEncoding(StandardCharsets.UTF_8.name());
        return actions;
    }
}
