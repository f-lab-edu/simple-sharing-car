package com.flab.simplesharingcar.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.simplesharingcar.config.QuerydslConfig;
import com.flab.simplesharingcar.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.jdbc.Sql;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@Import({QuerydslConfig.class})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeTestClass
    @Sql({"classpath:db/mysql/schema.sql", "classpath:db/mysql/data.sql"})
    public void initClass() {
    }


    @Test
    public void 유저_조회_BY_EMAIL() {
        // given
        String email = "admin@sharing.com";
        // when
        User findUser = userRepository.findByEmail(email);
        // then
        assertThat(findUser).isNotNull();
    }

    @Test
//    @Rollback(value = false)
    public void 유저_저장_테스트() {
        // given
        User givenUser = User.builder()
            .email("gasdkwo@gafsa.com")
            .password("11234")
            .name("김태경")
            .build();
        // when
        userRepository.save(givenUser);
        // then
        String email = givenUser.getEmail();
        assertThat(userRepository.findByEmail(email)).isNotNull();
    }
}
