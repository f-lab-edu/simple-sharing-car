package com.flab.simplesharingcar.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.flab.simplesharingcar.config.SecurityConfig;
import com.flab.simplesharingcar.service.UserService;
import java.nio.charset.Charset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@Import(SecurityConfig.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();
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
        MockHttpServletRequestBuilder builder = post("/users")
            .param("email", "aasf@naver.com")
            .param("password", "1234")
            .param("name", "kim");
        // when
        ResultActions perform = mockMvc.perform(builder);
        // then
        perform.andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/users/create"));
    }

    @Test
    public void 회원가입_필수_Valid() throws Exception {
        // given
        MockHttpServletRequestBuilder builder = post("/users")
            .param("email", "aasf@naver.com")
            .param("password", "1234");
        // when
        MvcResult mvcResult = mockMvc.perform(builder).andReturn();
        // then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8"))).contains("필수 입니다.");
    }
    
    @Test
    public void 회원가입_이메일_Valid() throws Exception {
        // given
        MockHttpServletRequestBuilder builder = post("/users")
            .param("email", "test")
            .param("password", "1234")
            .param("name", "kim");
        // when
        MvcResult mvcResult = mockMvc.perform(builder).andReturn();
        // then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8"))).contains("이메일 형식이 아닙니다.");
    }
}