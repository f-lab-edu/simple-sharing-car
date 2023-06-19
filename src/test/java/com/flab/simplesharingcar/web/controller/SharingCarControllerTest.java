package com.flab.simplesharingcar.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.simplesharingcar.constants.SessionKey;
import com.flab.simplesharingcar.domain.User;
import com.flab.simplesharingcar.service.sharing.SharingCarService;
import com.flab.simplesharingcar.web.exception.ErrorResponse;
import com.flab.simplesharingcar.web.exception.ErrorStatus;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
@WebMvcTest(SharingCarController.class)
class SharingCarControllerTest {

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @MockBean
    private SharingCarService sharingCarService;

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
    public void 차량_검색_성공() throws Exception {
        // given
        Long sharingZoneId = 2L;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusOneHour = now.plus(1, ChronoUnit.HOURS);

        MockHttpServletRequestBuilder builder = get("/cars")
            .param("sharingZoneId", sharingZoneId.toString())
            .param("resStartTime", now.toString())
            .param("resEndTime", nowPlusOneHour.toString())
            .session(session);
        // when
        ResultActions perform = mockMvc.perform(builder);
        // then
        perform.andExpect(status().isOk())
            .andDo(print());
    }
    
    @Test
    public void 필수_체크() throws Exception {
        // given
        Long sharingZoneId = 2L;
        LocalDateTime now = LocalDateTime.now();
        MockHttpServletRequestBuilder builder = get("/cars")
            .param("sharingZoneId", sharingZoneId.toString())
            .param("resEndTime", now.toString())
            .session(session);
        // when
        ResultActions perform = mockMvc.perform(builder);
        // then
        ErrorResponse expectObject = ErrorResponse.builder()
            .code(ErrorStatus.NOT_VALID_ARGUMENT.toString())
            .message("[resStartTime]은(는) 시작 시간은 필수 입니다.")
            .build();
        String expectJson = objectMapper.writeValueAsString(expectObject);
        perform.andExpect(status().isBadRequest())
            .andExpect(content().string(expectJson))
            .andDo(print());
    }
    
    @Test
    public void 차량_검색시_시작_시간이_종료_시간보다_늦을때() throws Exception {
        // given
        Long sharingZoneId = 2L;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusOneHour = now.plus(-1, ChronoUnit.HOURS);

        MockHttpServletRequestBuilder builder = get("/cars")
            .param("sharingZoneId", sharingZoneId.toString())
            .param("resStartTime", now.toString())
            .param("resEndTime", nowPlusOneHour.toString())
            .session(session);
        // when
        ResultActions perform = mockMvc.perform(builder);
        // then
        ErrorResponse expectObject = ErrorResponse.builder()
            .code(ErrorStatus.NOT_VALID_ARGUMENT.toString())
            .message("[resStartTime]은(는) 시작 시간이 종료 시간보다 늦을 수 없습니다.")
            .build();
        String expectJson = objectMapper.writeValueAsString(expectObject);
        perform.andExpect(status().isBadRequest())
            .andExpect(content().string(expectJson))
            .andDo(print());
    }
}