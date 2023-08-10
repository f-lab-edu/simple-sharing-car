package com.flab.simplesharingcar.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.simplesharingcar.constants.SessionKey;
import com.flab.simplesharingcar.domain.Payment;
import com.flab.simplesharingcar.domain.Reservation;
import com.flab.simplesharingcar.domain.ReservationTime;
import com.flab.simplesharingcar.domain.SharingCar;
import com.flab.simplesharingcar.domain.User;
import com.flab.simplesharingcar.service.payment.PaymentService;
import com.flab.simplesharingcar.service.reservation.ReservationService;
import com.flab.simplesharingcar.service.sharing.SharingCarService;
import com.flab.simplesharingcar.web.dto.ReservationRequest;
import com.flab.simplesharingcar.web.exception.ErrorResponse;
import com.flab.simplesharingcar.web.exception.ErrorStatus;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.servlet.http.HttpServletRequest;
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

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private SharingCarService sharingCarService;

    @MockBean
    private PaymentService paymentService;

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
            .resStartTime(now.plus(1, ChronoUnit.HOURS))
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
        when(reservationService.reserve(any()))
            .thenReturn(
                Reservation.builder()
                    .user(User.builder().build())
                    .payment(new Payment(100))
                    .sharingCar(SharingCar.builder().build())
                    .reservationTime(new ReservationTime(now, now.plus(1, ChronoUnit.HOURS)))
                    .build()
            );
        ReservationRequest request = ReservationRequest.builder()
            .paymentId(1L)
            .sharingCarId(2L)
            .resStartTime(now)
            .resEndTime(now.plus(1, ChronoUnit.HOURS))
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

}