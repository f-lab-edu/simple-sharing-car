package com.flab.simplesharingcar.web.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.simplesharingcar.constants.SessionKey;
import com.flab.simplesharingcar.domain.Payment;
import com.flab.simplesharingcar.domain.User;
import com.flab.simplesharingcar.service.payment.PaymentService;
import com.flab.simplesharingcar.service.sharing.SharingCarService;
import com.flab.simplesharingcar.web.dto.PaymentRequest;
import com.flab.simplesharingcar.web.exception.ErrorResponse;
import com.flab.simplesharingcar.web.exception.ErrorStatus;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
@WebMvcTest(PaymentController.class)
class PaymentControllerTest {


    @Autowired
    WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private SharingCarService sharingCarService;

    @MockBean
    private PaymentService paymentService;

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
    public void 결제_필수_valid() throws Exception {
        // given
        PaymentRequest request = PaymentRequest.builder()
            .sharingCarId(2L)
            .resEndTime(LocalDateTime.now())
            .build();
        String requestJson = objectMapper.writeValueAsString(request);
        MockHttpServletRequestBuilder builder = post("/payment")
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
    public void 결제_성공() throws Exception {
        // given
        when(paymentService.makePayment(any(), any())).thenReturn(new Payment(1000));
        LocalDateTime now = LocalDateTime.now();
        PaymentRequest request = PaymentRequest.builder()
            .sharingCarId(2L)
            .resStartTime(now)
            .resEndTime(now.plus(1, ChronoUnit.HOURS))
            .build();
        String requestJson = objectMapper.writeValueAsString(request);
        MockHttpServletRequestBuilder builder = post("/payment")
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