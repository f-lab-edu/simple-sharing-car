package com.flab.simplesharingcar.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.flab.simplesharingcar.domain.User;
import com.flab.simplesharingcar.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.jdbc.Sql;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@MybatisTest
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    UserService userService;

    @BeforeTestClass
    @Sql({"classpath:db/mysql/schema.sql", "classpath:db/mysql/data.sql"})
    void initClass() {
    }

    @BeforeEach
    void init() {
        userService = new UserService(userRepository);
    }

    @Test
    public void 회원가입_성공() {
        // given
        User joinUser = User.builder()
            .email("tk1111@naver.com")
            .password("1234")
            .name("김태경")
            .build();
        // when
        Long joinUserId = userService.join(joinUser);
        joinUser.setId(joinUserId);
        // then
        Assertions.assertThat(joinUser).isEqualTo(userService.findById(joinUserId));
    }

    @Test
    public void 중복_이메일_회원가입() {
        // given
        User joinUser1 = User.builder()
            .email("a1234@naver.com")
            .password("1234")
            .name("user1")
            .build();
        User joinUser2 = User.builder()
            .email("a1234@naver.com")
            .password("5678")
            .name("user2")
            .build();
        // when
        userService.join(joinUser1);
        // then
        assertThatThrownBy(() -> userService.join(joinUser1)).isInstanceOf(IllegalStateException.class);
    }
}