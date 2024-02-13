package com.flab.simplesharingcar.domain;

import com.flab.simplesharingcar.constants.CarReservationStatus;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @Test
    void 예약은_결제_정보_필수() {
        // given
        User user = new User();
        SharingCar sharingCar = new SharingCar();
        ReservationTime reservationTime = new ReservationTime();
        CarReservationStatus status = CarReservationStatus.WAITING;
        // when
        AbstractThrowableAssert<?, ? extends Throwable> assertThrow = assertThatThrownBy(
                () -> Reservation.builder()
                        .user(user)
                        .sharingCar(sharingCar)
                        .reservationTime(reservationTime)
                        .status(status)
                        .build()
        );
        // then
        assertThrow.isInstanceOf(IllegalStateException.class)
                .message()
                .isEqualTo("결제 정보가 없습니다.");
    }

    @Test
    void 예약_상태가_아니면_취소_불가능() {
        // given
        User user = new User();
        SharingCar sharingCar = new SharingCar();
        ReservationTime reservationTime = new ReservationTime();
        CarReservationStatus status = CarReservationStatus.WAITING;
        Payment payment = new Payment();
        Reservation reservation = Reservation.builder()
                .user(user)
                .sharingCar(sharingCar)
                .reservationTime(reservationTime)
                .status(status)
                .payment(payment)
                .build();
        // when
        AbstractThrowableAssert<?, ? extends Throwable> assertThrow = assertThatThrownBy(reservation::cancel);
        // then
        assertThrow.isInstanceOf(IllegalStateException.class)
                .message()
                .isEqualTo("예약 상태가 아닙니다.");
    }
}