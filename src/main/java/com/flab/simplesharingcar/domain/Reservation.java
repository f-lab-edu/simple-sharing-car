package com.flab.simplesharingcar.domain;

import com.flab.simplesharingcar.constants.CarReservationStatus;

import javax.persistence.*;

import com.flab.simplesharingcar.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Reservation extends BaseEntity {

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
