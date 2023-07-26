package com.flab.simplesharingcar.service.payment;

import com.flab.simplesharingcar.config.QuerydslConfig;
import com.flab.simplesharingcar.domain.Payment;
import com.flab.simplesharingcar.repository.PaymentRepository;
import org.assertj.core.api.Assertions;
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
class PaymentServiceTest {

    @Autowired
    PaymentRepository paymentRepository;

    PaymentService paymentService;

    @BeforeTestClass
    @Sql({"classpath:db/mysql/schema.sql"})
    void initClass() {
    }

    @BeforeEach
    void init() {
        paymentService = new PaymentService(paymentRepository);
    }


    @Test
    public void 결제_저장() {
        // given
        Integer price = 100000;
        // when
        Payment savedPayment = paymentService.makePayment(price);
        // then
        Long savedId = savedPayment.getId();
        Payment findPayment = paymentRepository.findById(savedId).get();
        Assertions.assertThat(savedPayment).isEqualTo(findPayment);
    }

}