package com.flab.simplesharingcar.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.flab.simplesharingcar.config.QuerydslConfig;
import com.flab.simplesharingcar.domain.User;
import com.flab.simplesharingcar.repository.UserRepository;
import com.flab.simplesharingcar.web.exception.user.FailLoginException;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.jdbc.Sql;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@Import({QuerydslConfig.class})
class SignInServiceTest {

    @Autowired
    UserRepository userRepository;

    SignInService signInService;


    @BeforeTestClass
    @Sql({"classpath:db/mysql/schema.sql", "classpath:db/mysql/data.sql"})
    void initClass() {
    }

    @BeforeEach
    void init() {
        signInService = new SignInService(userRepository);
    }

    @Test
    public void 로그인_성공() {
        // given
        String email = "admin@sharing.com";
        String password = "1234";
        // when
        User loginUser = signInService.login(email, password);
        // then
        assertThat(loginUser).isNotNull();
    }

    @Test
    public void 존재하지않는_유저() {
        // given
        String email = "1234@sharing.com";
        String password = "1234";
        // when
        AbstractThrowableAssert throwableAssert = assertThatThrownBy(
            () -> signInService.login(email, password));
        // then
        throwableAssert.isInstanceOf(FailLoginException.class);
        throwableAssert.hasMessageContaining("없는 Email 이거나 Password가 잘못 되었습니다.");
    }

    @Test
    public void 틀린_패스워드() {
        // given
        String email = "admin@sharing.com";
        String password = "12345";
        // when
        AbstractThrowableAssert throwableAssert = assertThatThrownBy(
            () -> signInService.login(email, password));
        // then
        throwableAssert.isInstanceOf(FailLoginException.class);
        throwableAssert.hasMessageContaining("없는 Email 이거나 Password가 잘못 되었습니다.");
    }
}