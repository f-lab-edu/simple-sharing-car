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
    @JoinColumn(name = "payments_history_id")
    private PaymentsHistory paymentsHistory;

    @Embedded
    private ReservationTime reservationTime;

    @Enumerated(EnumType.STRING)
    private CarReservationStatus status;

    @Builder
    public Reservation(User user, SharingCar sharingCar,
        ReservationTime reservationTime, CarReservationStatus status) {
        this.user = user;
        this.sharingCar = sharingCar;
        this.reservationTime = reservationTime;
        this.status = status;
    }
}
