package com.flab.simplesharingcar.domain;

import com.flab.simplesharingcar.constants.CarReservationStatus;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sharing_car_id")
    private SharingCar sharingCar;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Embedded
    private ReservationTime reservationTime;

    @Enumerated(EnumType.STRING)
    private CarReservationStatus status;

    @Builder
    public Reservation(User user, SharingCar sharingCar,
        ReservationTime reservationTime, CarReservationStatus status,
        Payment payment) {
        this.user = user;
        this.sharingCar = sharingCar;
        this.reservationTime = reservationTime;
        this.status = status;
        setPayment(payment);
    }

    private void setPayment(Payment payment) {
        if (payment == null) {
            throw new IllegalStateException("결제 정보가 없습니다.");
        }
        this.payment = payment;
    }

    public void cancel() {
        if (!Objects.equals(this.status, CarReservationStatus.RESERVED)) {
            throw new IllegalStateException("예약 상태가 아닙니다.");
        }
        this.status = CarReservationStatus.CANCEL_RESERVATION;
    }
}
