package com.flab.simplesharingcar.service.payment;

import com.flab.simplesharingcar.config.QuerydslConfig;
import com.flab.simplesharingcar.domain.Payment;
import com.flab.simplesharingcar.domain.ReservationTime;
import com.flab.simplesharingcar.domain.SharingCar;
import com.flab.simplesharingcar.repository.PaymentRepository;
import com.flab.simplesharingcar.repository.SharingCarRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    @Autowired
    SharingCarRepository sharingCarRepository;

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

        SharingCar sharingCar = sharingCarRepository.findById(1L).get();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime plusOneHour = now.plus(1, ChronoUnit.HOURS);
        ReservationTime reservationTime = new ReservationTime(now, plusOneHour);
        // when
        Payment savedPayment = paymentService.makePayment(sharingCar, reservationTime);
        // then
        Long savedId = savedPayment.getId();
        Payment findPayment = paymentRepository.findById(savedId).get();
        Assertions.assertThat(savedPayment).isEqualTo(findPayment);
    }

}