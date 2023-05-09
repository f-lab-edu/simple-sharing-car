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
    @Sql({"classpath:db/mysql/schema.sql"})
    public void initClass() {}

    @Test
//    @Rollback(value = false)
    public void 유저_저장_테스트() {
        // given
        User givenUser = new User("gasdkwo@gafsa.com", "11234", "김태경");
        // when
        Long savedId = userRepository.save(givenUser);
        // then
        assertThat(userRepository.selectById(savedId)).isNotNull();
    }
}