package com.flab.simplesharingcar.repository;

import com.flab.simplesharingcar.config.QuerydslConfig;
import com.flab.simplesharingcar.constants.CarReservationStatus;
import com.flab.simplesharingcar.domain.*;
import com.flab.simplesharingcar.dto.MyReservationSearchResult;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Import({QuerydslConfig.class})
class ReservationQueryRepositoryTest {

    @Autowired
    JPAQueryFactory jpaQueryFactory;
    ReservationQueryRepository reservationQueryRepository;
    @Autowired ReservationRepository reservationRepository;
    @Autowired UserRepository userRepository;
    @Autowired PaymentRepository paymentRepository;
    @Autowired SharingCarRepository sharingCarRepository;

    @BeforeEach
    void setUp() {
        reservationQueryRepository = new ReservationQueryRepository(jpaQueryFactory);
    }

    @Test
    @Transactional
    void 나의_예약_검색() {
        // given
        User user = userRepository.findById(1L).get();
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 1000; i++) {
            Payment payment = new Payment(1000);
            paymentRepository.save(payment);
            LocalDateTime start = now.plusHours(-1000 + i);
            LocalDateTime end = now.plusHours(-1000 + i + 1);
            ReservationTime reservationTime = new ReservationTime(start, end);
            SharingCar sharingCar = sharingCarRepository.findById(1L).get();

            Reservation reservation = Reservation.builder()
                    .payment(payment)
                    .user(user)
                    .status(CarReservationStatus.RESERVED)
                    .reservationTime(reservationTime)
                    .sharingCar(sharingCar)
                    .build();
            reservationRepository.save(reservation);
        }
        // when
        PageRequest pageRequest = PageRequest.of(0, 500);
        Slice<MyReservationSearchResult> myReservationOnePage = reservationQueryRepository.findMyReservation(user.getId(), pageRequest);
        pageRequest = PageRequest.of(1, 500);
        Slice<MyReservationSearchResult> myReservationTwoPage = reservationQueryRepository.findMyReservation(user.getId(), pageRequest);

        // then
        int pageOneSize = myReservationOnePage.getContent().size();
        int pageTwoSize = myReservationTwoPage.getContent().size();

        assertThat(pageOneSize).isEqualTo(pageTwoSize);
    }

}