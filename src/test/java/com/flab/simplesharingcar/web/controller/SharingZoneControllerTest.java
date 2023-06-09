package com.flab.simplesharingcar.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.simplesharingcar.constants.SessionKey;
import com.flab.simplesharingcar.domain.User;
import com.flab.simplesharingcar.service.sharing.SharingZoneService;
import com.flab.simplesharingcar.web.exception.ErrorResponse;
import com.flab.simplesharingcar.web.exception.ErrorStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SharingZoneController.class)
class SharingZoneControllerTest {

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @MockBean
    private SharingZoneService sharingZoneService;

    private ObjectMapper objectMapper;

    private MockHttpSession session;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .build();
        this.objectMapper = new ObjectMapper();
        this.session = new MockHttpSession();
        this.session.setAttribute(SessionKey.LOGIN_USER, User.builder().build());
    }

    @Test
    public void 지도_화면_HTML() throws Exception {
        // given
        MockHttpServletRequestBuilder builder = get("/zone/map").session(session);
        // when
        ResultActions perform = mockMvc.perform(builder);
        // then
        perform.andExpect(status().isOk())
            .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    public void 주변_주차장_찾기_성공() throws Exception {
        // given
        String latitude = "126.0";
        String longitude = "37.11";
        String distance = "1.0";

        MockHttpServletRequestBuilder builder = get("/zone")
            .param("latitude", latitude)
            .param("longitude", longitude)
            .param("distance", distance)
            .session(session);
        // when
        ResultActions perform = mockMvc.perform(builder);
        // then
        perform.andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    public void 주변_주차장_찾기_필수_Valid() throws Exception {
        // given
        String latitude = "37.11";
        String longitude = "126.0";

        MockHttpServletRequestBuilder builder = get("/zone")
            .param("latitude", latitude)
            .param("longitude", longitude)
            .session(session);
        // when
        ResultActions perform = mockMvc.perform(builder);
        // then
        ErrorResponse expectObject = ErrorResponse.builder()
            .code(ErrorStatus.NOT_VALID_ARGUMENT.toString())
            .message("[distance]은(는) 거리는 필수 입니다.")
            .build();
        String expectJson = objectMapper.writeValueAsString(expectObject);
        perform.andExpect(status().isBadRequest())
            .andExpect(content().string(expectJson))
            .andDo(print());
    }

    @Test
    public void 위도_경도_범위_Valid() throws Exception {
        // given
        String latitude = "91.0";
        String longitude = "126.0";
        String distance = "1.0";

        MockHttpServletRequestBuilder builder = get("/zone")
            .param("latitude", latitude)
            .param("longitude", longitude)
            .param("distance", distance)
            .session(session);
        // when
        ResultActions perform = mockMvc.perform(builder);
        // then
        ErrorResponse expectObject = ErrorResponse.builder()
            .code(ErrorStatus.NOT_VALID_ARGUMENT.toString())
            .message("[latitude]은(는) 위도 값이 범위에 벗어 났습니다.")
            .build();
        String expectJson = objectMapper.writeValueAsString(expectObject);
        perform.andExpect(status().isBadRequest())
            .andExpect(content().string(expectJson))
            .andDo(print());
    }
}