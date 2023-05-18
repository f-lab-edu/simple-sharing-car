package com.flab.simplesharingcar.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.simplesharingcar.config.SecurityConfig;
import com.flab.simplesharingcar.domain.User;
import com.flab.simplesharingcar.service.UserService;
import com.flab.simplesharingcar.web.exception.ErrorResponse;
import com.flab.simplesharingcar.web.exception.ErrorStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@Import(SecurityConfig.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void 회원가입_FORM() throws Exception {
        // given
        MockHttpServletRequestBuilder builder = get("/users/create");
        // when
        ResultActions perform = mockMvc.perform(builder);
        // then
        perform.andExpect(status().isOk())
            .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    public void 회원가입_필수_성공() throws Exception {
        // given
        User requestUser = User.builder()
            .email("aasf@naver.com")
            .password("1234")
            .name("kim")
            .build();
        String requestUserJson = objectMapper.writeValueAsString(requestUser);
        MockHttpServletRequestBuilder builder = post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestUserJson);
        // when
        ResultActions perform = mockMvc.perform(builder);
        // then
        perform.andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    public void 회원가입_필수_Valid() throws Exception {
        // given
        User requestUser = User.builder()
            .email("aasf@naver.com")
            .password("1234")
            .build();
        String requestUserJson = objectMapper.writeValueAsString(requestUser);
        MockHttpServletRequestBuilder builder = post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestUserJson);

        // when
        ResultActions perform = mockMvc.perform(builder);

        // then
        ErrorResponse expectObject = ErrorResponse.builder()
            .code(ErrorStatus.NOT_VALID_ARGUMENT.toString())
            .message("[name]은(는) 이름은 필수 입니다.")
            .build();
        String expectJson = objectMapper.writeValueAsString(expectObject);
        perform.andExpect(status().isBadRequest())
            .andExpect(content().string(expectJson))
            .andDo(print());
    }
    
    @Test
    public void 회원가입_이메일_Valid() throws Exception {
        // given
        User requestUser = User.builder()
            .email("invalidEmail")
            .password("1234")
            .name("testUser")
            .build();
        String requestUserJson = objectMapper.writeValueAsString(requestUser);
        MockHttpServletRequestBuilder builder = post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestUserJson);

        // when
        ResultActions perform = mockMvc.perform(builder);

        // then
        ErrorResponse expectObject = ErrorResponse.builder()
            .code(ErrorStatus.NOT_VALID_ARGUMENT.toString())
            .message("[email]은(는) 이메일 형식이 아닙니다.")
            .build();
        String expectJson = objectMapper.writeValueAsString(expectObject);
        perform.andExpect(status().isBadRequest())
            .andExpect(content().string(expectJson))
            .andDo(print());
    }

}