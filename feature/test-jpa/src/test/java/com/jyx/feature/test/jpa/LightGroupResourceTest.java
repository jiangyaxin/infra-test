package com.jyx.feature.test.jpa;

import com.google.common.collect.Lists;
import com.jyx.feature.test.jpa.application.dto.ChannelDto;
import com.jyx.feature.test.jpa.application.dto.LightGroupDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.jyx.feature.test.jpa.domain.entity.value.Direction.EAST_SOUTH;
import static com.jyx.feature.test.jpa.domain.entity.value.Direction.WEST_SOUTH;
import static com.jyx.feature.test.jpa.domain.entity.value.FlowDirection.PEDESTRIAN_ONCE_CROSSING;
import static com.jyx.feature.test.jpa.domain.entity.value.FlowDirection.TURN_ROUND_LEFT_STRAIGHT_RIGHT;
import static com.jyx.feature.test.jpa.domain.entity.value.LightGroupType.PEDESTRIAN;
import static com.jyx.feature.test.jpa.domain.entity.value.LightGroupType.VEHICLE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author asa
 * @since 2021/11/7 10:58
 */
@Slf4j
@SpringBootTest
public class LightGroupResourceTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void queryByChannelNumberList() throws Exception{
        String url = "/api/v1/lightGroup/channelNumber/3,4";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        ResultActions actions = MockMvcSupport.prepareRequest(mockMvc, requestBuilder);
        String response = actions.andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        log.info(response);
    }

    @Test
    public void create() throws Exception{
        String url = "/api/v1/lightGroup";
        ChannelDto channel1 = ChannelDto.builder().number(3).build();
        ChannelDto channel2 = ChannelDto.builder().number(4).build();
        LightGroupDto lightGroup = LightGroupDto.builder()
                .number(2)
                .type(PEDESTRIAN)
                .direction(EAST_SOUTH)
                .flowDirection(PEDESTRIAN_ONCE_CROSSING)
                .channelList(Lists.newArrayList(
                        channel1,channel2
                ))
                .build();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url);
        ResultActions actions = MockMvcSupport.prepareRequest(mockMvc, requestBuilder,Lists.newArrayList(lightGroup));
        String response = actions.andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        log.info(response);
    }

    @Test
    public void modify() throws Exception{
        String url = "/api/v1/lightGroup";
        String content = "{\"id\":7341907357220864," +
                "\"version\":0,\"number\":2,\"type\":3,\"direction\":7," +
                "\"flowDirection\":17," +
                "\"channelList\":[" +
                        "{\"id\":7341907357220865," +
                        "\"version\":0," +
                        "\"number\":4}," +

                        "{\"id\":7341907357220866," +
                        "\"version\":0," +
                        "\"number\":3}]}";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(url);
        ResultActions actions = MockMvcSupport.prepareRequest(mockMvc, requestBuilder,content);
        String response = actions.andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        log.info(response);
    }

    @Test
    public void testBusinessException() throws Exception{
        String url = "/api/v1/exception/business";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url);
        ResultActions actions = MockMvcSupport.prepareRequest(mockMvc, requestBuilder);
        String response = actions.andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        log.info(response);

         actions = MockMvcSupport.prepareRequest(mockMvc, requestBuilder,"");
         response = actions.andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        log.info(response);

        actions = MockMvcSupport.prepareRequest(mockMvc, requestBuilder,"1");
        response = actions.andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        log.info(response);
    }
}
