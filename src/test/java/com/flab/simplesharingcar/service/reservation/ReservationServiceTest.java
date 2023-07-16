package com.flab.simplesharingcar.service.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.flab.simplesharingcar.config.QuerydslConfig;
import com.flab.simplesharingcar.constants.CarReservationStatus;
import com.flab.simplesharingcar.domain.Reservation;
import com.flab.simplesharingcar.domain.ReservationTime;
import com.flab.simplesharingcar.domain.SharingCar;
import com.flab.simplesharingcar.domain.User;
import com.flab.simplesharingcar.repository.ReservationRepository;
import com.flab.simplesharingcar.repository.SharingCarRepository;
import com.flab.simplesharingcar.repository.UserRepository;
import com.flab.simplesharingcar.web.exception.reservation.AlreadyReservationException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
class ReservationServiceTest {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SharingCarRepository sharingCarRepository;

    ReservationService reservationService;

    @BeforeTestClass
    @Sql({"classpath:db/mysql/schema.sql", "classpath:db/mysql/data.sql"})
    void initClass() {
    }

    @BeforeEach
    void init() {
        reservationService = new ReservationService(reservationRepository);
    }

    @Test
    public void 예약이_이미_존재() {
        // given
        User user = getUser(1L);
        SharingCar sharingCar = getSharingCar(1L);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusOneHour = now.plus(1, ChronoUnit.HOURS);
        ReservationTime reservationTime = new ReservationTime(now, nowPlusOneHour);
        Reservation saveReservation = Reservation.builder()
            .user(user)
            .sharingCar(sharingCar)
            .reservationTime(reservationTime)
            .build();
        // when
        AbstractThrowableAssert assertThrow = assertThatThrownBy(
            () -> reservationService.reserve(saveReservation)
        );
        // then
        assertThrow.isInstanceOf(AlreadyReservationException.class);
    }

    private User getUser(Long id) {
        return userRepository.findById(id).get();
    }

    private SharingCar getSharingCar(Long id) {
        return sharingCarRepository.findById(id).get();
    }

    @Test
    public void 예약_성공() {
        // given
        User user = getUser(1L);
        SharingCar sharingCar = getSharingCar(2L);
        LocalDateTime nowPlusOneHour = LocalDateTime.now().plus(1, ChronoUnit.HOURS);
        LocalDateTime nowPlusTwoHour = nowPlusOneHour.plus(1, ChronoUnit.HOURS);
        ReservationTime reservationTime = new ReservationTime(nowPlusOneHour, nowPlusTwoHour);
        Reservation saveReservation = Reservation.builder()
            .user(user)
            .sharingCar(sharingCar)
            .reservationTime(reservationTime)
            .build();
        // when
        Reservation savedReservation = reservationService.reserve(saveReservation);
        // then
        assertThat(savedReservation.getStatus()).isEqualTo(CarReservationStatus.RESERVED);
    }
}