package com.flab.simplesharingcar.service.reservation;

import com.flab.simplesharingcar.config.QuerydslConfig;
import com.flab.simplesharingcar.constants.CarReservationStatus;
import com.flab.simplesharingcar.domain.*;
import com.flab.simplesharingcar.repository.PaymentRepository;
import com.flab.simplesharingcar.repository.ReservationRepository;
import com.flab.simplesharingcar.repository.SharingCarRepository;
import com.flab.simplesharingcar.repository.UserRepository;
import com.flab.simplesharingcar.service.payment.PaymentService;
import com.flab.simplesharingcar.web.exception.reservation.FailReservationException;
import org.assertj.core.api.AbstractThrowableAssert;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@Import({QuerydslConfig.class})
class ReservationServiceTest {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SharingCarRepository sharingCarRepository;

    @Autowired
    PaymentRepository paymentRepository;

    PaymentService paymentService;

    ReservationService reservationService;

    @BeforeTestClass
    @Sql({"classpath:db/mysql/schema.sql", "classpath:db/mysql/data.sql"})
    void initClass() {
    }

    @BeforeEach
    void init() {
        paymentService = new PaymentService(paymentRepository);
        reservationService = new ReservationService(paymentService, reservationRepository
                , sharingCarRepository, userRepository);
    }

    @Test
    public void 예약이_이미_존재() {
        // given
        long userId = 1L;
        long sharingCarId = 1L;
        long paymentId = 1L;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusOneHour = now.plusHours(1);
        ReservationTime reservationTime = new ReservationTime(now, nowPlusOneHour);
        // when
        reservationService.reserve(sharingCarId, userId, paymentId, reservationTime);
        AbstractThrowableAssert<?, ? extends Throwable> assertThrow = assertThatThrownBy(
            () -> reservationService.reserve(sharingCarId, userId, paymentId, reservationTime)
        );
        // then
        assertThrow.isInstanceOf(FailReservationException.class)
                .message()
                .isEqualTo("이미 예약이 존재 합니다.");
    }

    private User getUser(Long id) {
        return userRepository.findById(id).get();
    }

    private SharingCar getSharingCar(Long id) {
        return sharingCarRepository.findById(id).get();
    }

    @Test
    @Transactional
    public void 예약_성공() {
        // given
        long userId = 1L;
        long sharingCarId = 2L;
        long paymentId = 1L;
        LocalDateTime nowPlusOneHour = LocalDateTime.now().plusHours(1);
        LocalDateTime nowPlusTwoHour = nowPlusOneHour.plusHours(1);
        ReservationTime reservationTime = new ReservationTime(nowPlusOneHour, nowPlusTwoHour);

        // when
        Reservation savedReservation = reservationService.reserve(sharingCarId, userId, paymentId, reservationTime);
        // then
        assertThat(savedReservation.getStatus()).isEqualTo(CarReservationStatus.RESERVED);
    }

    @Test
    void 결제_정보_Null() {
        // given
        long userId = 1L;
        long sharingCarId = 2L;
        long paymentId = 3L;
        LocalDateTime nowPlusOneHour = LocalDateTime.now().plus(1, ChronoUnit.HOURS);
        LocalDateTime nowPlusTwoHour = nowPlusOneHour.plus(1, ChronoUnit.HOURS);
        ReservationTime reservationTime = new ReservationTime(nowPlusOneHour, nowPlusTwoHour);
        // when
        AbstractThrowableAssert<?, ? extends Throwable> assertThrow = assertThatThrownBy(
                () -> reservationService.reserve(sharingCarId, userId, paymentId, reservationTime)
        );
        // then
        assertThrow.isInstanceOf(FailReservationException.class)
                .message().isEqualTo("결제 정보는 필수 입니다.");
    }
    
    @Test
    void 결제_정보_불일치() {
        // given
        long userId = 1L;
        long sharingCarId = 2L;
        long paymentId = 2L;
        LocalDateTime nowPlusOneHour = LocalDateTime.now().plusHours(1);
        LocalDateTime nowPlusTwoHour = nowPlusOneHour.plusHours(1);
        ReservationTime reservationTime = new ReservationTime(nowPlusOneHour, nowPlusTwoHour);
        // when
        AbstractThrowableAssert<?, ? extends Throwable> assertThrow = assertThatThrownBy(
                () -> reservationService.reserve(sharingCarId, userId, paymentId, reservationTime)
        );
        // then
        assertThrow.isInstanceOf(FailReservationException.class)
                .message().isEqualTo("결제 정보가 일치 하지 않습니다.");
    }

    @Test
    void 예약_실패시_결제_취소() {
        // given
        long userId = 1L;
        long sharingCarId = 2L;
        long paymentId = 2L;
        LocalDateTime nowPlusOneHour = LocalDateTime.now().plusHours(1);
        LocalDateTime nowPlusTwoHour = nowPlusOneHour.plusHours(1);
        ReservationTime reservationTime = new ReservationTime(nowPlusOneHour, nowPlusTwoHour);
        // when
        AbstractThrowableAssert<?, ? extends Throwable> assertThrow = assertThatThrownBy(
                () -> reservationService.reserve(sharingCarId, userId, paymentId, reservationTime)
        );
        Payment findPayment = paymentRepository.findById(paymentId).orElse(null);
        // then
        assertThrow.isInstanceOf(FailReservationException.class)
                .message().isEqualTo("결제 정보가 일치 하지 않습니다.");
        assertThat(findPayment).isNull();
    }
}