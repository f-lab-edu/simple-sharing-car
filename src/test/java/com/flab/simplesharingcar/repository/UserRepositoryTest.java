package com.flab.simplesharingcar.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.simplesharingcar.domain.User;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.jdbc.Sql;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@MybatisTest
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
        User search = User.builder()
            .email("admin@sharing.com")
            .build();
        // when
        User findUser = userRepository.selectUser(search);
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
        Long savedId = givenUser.getId();
        User search = User.builder()
            .id(savedId)
            .build();
        // then
        assertThat(userRepository.selectUser(search)).isNotNull();
    }
}
