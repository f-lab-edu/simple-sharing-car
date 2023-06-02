package com.flab.simplesharingcar.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.simplesharingcar.domain.User;
import com.flab.simplesharingcar.service.user.SignInService;
import com.flab.simplesharingcar.service.user.SignUpService;
import com.flab.simplesharingcar.web.dto.SignInFormDto;
import com.flab.simplesharingcar.web.dto.SignUpFormDto;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @MockBean
    private SignUpService signUpService;

    @MockBean
    private SignInService signInService;

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
        when(signUpService.join(any())).thenReturn(User.builder().build());
        SignUpFormDto requestUser = SignUpFormDto.builder()
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
        SignUpFormDto requestUser = SignUpFormDto.builder()
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
        SignUpFormDto requestUser = SignUpFormDto.builder()
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

    @Test
    public void 로그인_FORM() throws Exception {
        // given
        MockHttpServletRequestBuilder builder = get("/users/login");
        // when
        ResultActions perform = mockMvc.perform(builder);
        // then
        perform.andExpect(status().isOk())
            .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    public void 로그인_성공() throws Exception {
        // given
        when(signInService.login(any(), any())).thenReturn(User.builder().build());
        SignInFormDto requestUser = SignInFormDto.builder()
            .email("test1@navver.com")
            .password("1234")
            .build();
        String requestUserJson = objectMapper.writeValueAsString(requestUser);
        MockHttpServletRequestBuilder builder = post("/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestUserJson);

        // when
        ResultActions perform = mockMvc.perform(builder);
        // then
        perform.andExpect(status().isOk())
            .andDo(print());
    }
    
    @Test
    public void 로그인_필수_Valid() throws Exception {
        // given
        SignInFormDto requestUser = SignInFormDto.builder()
            .email("test1@navver.com")
            .build();
        String requestUserJson = objectMapper.writeValueAsString(requestUser);
        MockHttpServletRequestBuilder builder = post("/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestUserJson);
        // when
        ResultActions perform = mockMvc.perform(builder);
        // then
        ErrorResponse expectObject = ErrorResponse.builder()
            .code(ErrorStatus.NOT_VALID_ARGUMENT.toString())
            .message("[password]은(는) 패스워드는 필수 입니다.")
            .build();
        String expectJson = objectMapper.writeValueAsString(expectObject);
        perform.andExpect(status().isBadRequest())
            .andExpect(content().string(expectJson))
            .andDo(print());
    }

    @Test
    public void 로그인_없이_HOME_VIEW() throws Exception {
        // given
        MockHttpServletRequestBuilder builder = get("/home");
        // when
        ResultActions perform = mockMvc.perform(builder);
        // then
        perform.andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/users/login"));
    }

    @Test
    public void 로그인_하고_HOME_VIEW() throws Exception {
        // given
        MockHttpSession session = loginAndGetSession();
        MockHttpServletRequestBuilder builder = get("/home").session(session);

        // when
        ResultActions perform = mockMvc.perform(builder);
        // then
        perform.andExpect(status().isOk())
            .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    private MockHttpSession loginAndGetSession() throws Exception {
        User loginUser = User.builder()
            .email("test1@navver.com")
            .password("1234")
            .build();
        when(signInService.login(any(), any())).thenReturn(loginUser);
        SignInFormDto requestUser = SignInFormDto.from(loginUser);

        String requestUserJson = objectMapper.writeValueAsString(requestUser);
        MockHttpServletRequestBuilder loginBuilder = post("/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestUserJson);

        MvcResult mvcResult = mockMvc.perform(loginBuilder).andReturn();
        MockHttpSession session = (MockHttpSession) mvcResult.getRequest().getSession();

        return session;
    }

    @Test
    public void 로그인_없이_로그아웃() throws Exception {
        MockHttpServletRequestBuilder builder = get("/users/logout");
        // when
        ResultActions perform = mockMvc.perform(builder);
        // then
        ErrorResponse expectObject = ErrorResponse.builder()
            .code(ErrorStatus.NOT_LOGIN.toString())
            .message("로그인이 필요 합니다.")
            .build();
        String expectJson = objectMapper.writeValueAsString(expectObject);
        perform.andExpect(status().isUnauthorized())
            .andExpect(content().string(expectJson))
            .andDo(print());
    }

    @Test
    public void 로그인_하고_로그아웃() throws Exception {
        MockHttpSession session = loginAndGetSession();
        MockHttpServletRequestBuilder builder = get("/users/logout").session(session);
        // when
        ResultActions perform = mockMvc.perform(builder);
        // then
        perform.andExpect(status().isOk())
            .andDo(print());
    }

}