package com.flab.simplesharingcar.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.simplesharingcar.constants.SessionKey;
import com.flab.simplesharingcar.domain.*;
import com.flab.simplesharingcar.service.reservation.MyReservationService;
import com.flab.simplesharingcar.service.reservation.ReservationService;
import com.flab.simplesharingcar.web.dto.ReservationRequest;
import com.flab.simplesharingcar.web.exception.ErrorResponse;
import com.flab.simplesharingcar.web.exception.ErrorStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private MyReservationService myReservationService;

    HttpServletRequest mockedRequest;

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
        mockedRequest = mock(HttpServletRequest.class);
    }

    @Test
    public void 예약_필수_체크() throws Exception {
        // given
        ReservationRequest request = ReservationRequest.builder()
            .paymentId(1L)
            .sharingCarId(2L)
            .resEndTime(LocalDateTime.now())
            .build();
        String requestJson = objectMapper.writeValueAsString(request);
        MockHttpServletRequestBuilder builder = post("/reservation")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .session(session);
        // when
        ResultActions perform = mockMvc.perform(builder);
        // then
        ErrorResponse expectObject = ErrorResponse.builder()
            .code(ErrorStatus.NOT_VALID_ARGUMENT.toString())
            .message("[resStartTime]은(는) 예약 시작 시간은 필수 입니다.")
            .build();
        String expectJson = objectMapper.writeValueAsString(expectObject);
        perform.andExpect(status().isBadRequest())
            .andExpect(content().string(expectJson))
            .andDo(print());
    }

    @Test
    public void 예약시작시간_보다_종료시간이_늦을때() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.now();
        ReservationRequest request = ReservationRequest.builder()
            .paymentId(1L)
            .sharingCarId(2L)
            .resStartTime(now.plusHours(1))
            .resEndTime(now)
            .build();
        String requestJson = objectMapper.writeValueAsString(request);
        MockHttpServletRequestBuilder builder = post("/reservation")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
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

    @Test
    public void 예약_성공() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.now();
        when(reservationService.reserve(any(), any(), any(), any()))
            .thenReturn(
                Reservation.builder()
                    .user(User.builder().build())
                    .payment(new Payment(100))
                    .sharingCar(SharingCar.builder().build())
                    .reservationTime(new ReservationTime(now, now.plusHours(1)))
                    .build()
            );
        ReservationRequest request = ReservationRequest.builder()
            .paymentId(1L)
            .sharingCarId(2L)
            .resStartTime(now)
            .resEndTime(now.plusHours(1))
            .build();
        String requestJson = objectMapper.writeValueAsString(request);
        MockHttpServletRequestBuilder builder = post("/reservation")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .session(session);
        // when
        ResultActions perform = mockMvc.perform(builder);
        // then
        perform.andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    void 결제_정보_필수_valid() throws Exception {
        // given
        ReservationRequest request = ReservationRequest.builder()
                .sharingCarId(2L)
                .resStartTime(LocalDateTime.now())
                .resEndTime(LocalDateTime.now())
                .build();
        String requestJson = objectMapper.writeValueAsString(request);
        MockHttpServletRequestBuilder builder = post("/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .session(session);
        // when
        ResultActions perform = mockMvc.perform(builder);
        // then
        ErrorResponse expectObject = ErrorResponse.builder()
                .code(ErrorStatus.NOT_VALID_ARGUMENT.toString())
                .message("[paymentId]은(는) 결제 정보는 필수 입니다.")
                .build();
        String expectJson = objectMapper.writeValueAsString(expectObject);
        perform.andExpect(status().isBadRequest())
                .andExpect(content().string(expectJson))
                .andDo(print());
    }

    @Test
    void 나의_예약_조회() throws Exception {
        // given
        MockHttpServletRequestBuilder builder = get("/reservation/my")
                .param("page", "0")
                .param("size", "20")
                .session(session);
        // when
        ResultActions perform = mockMvc.perform(builder);
        // then
        perform.andExpect(status().isOk())
                .andDo(print());
    }

}