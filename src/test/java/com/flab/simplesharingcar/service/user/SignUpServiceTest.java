package com.flab.simplesharingcar.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.flab.simplesharingcar.config.QuerydslConfig;
import com.flab.simplesharingcar.domain.User;
import com.flab.simplesharingcar.repository.UserRepository;
import com.flab.simplesharingcar.exception.user.DuplicateEmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
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
class SignUpServiceTest {

    @Autowired
    UserRepository userRepository;

    SignUpService signUpService;


    @BeforeTestClass
    @Sql({"classpath:db/mysql/schema.sql", "classpath:db/mysql/data.sql"})
    void initClass() {
    }

    @BeforeEach
    void init() {
        signUpService = new SignUpService(userRepository);
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
        User resultUser = signUpService.join(joinUser);
        // then
        assertThat(joinUser.getEmail()).isEqualTo(resultUser.getEmail());
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
        signUpService.join(joinUser1);
        // then
        assertThatThrownBy(() -> signUpService.join(joinUser2)).isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    public void 비밀번호_BCrypt_인코딩() {
        // given
        String password = "1234";

        User joinUser = User.builder()
            .email("a1234@naver.com")
            .password(password)
            .name("user1")
            .build();
        // when
        User resultUser = signUpService.join(joinUser);
        // then
        String resultUserEmail = resultUser.getEmail();
        User findUser = userRepository.findByEmail(resultUserEmail);
        assertThat(BCrypt.checkpw(password, findUser.getPassword())).isTrue();
    }
}