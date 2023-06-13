package com.flab.simplesharingcar.domain;

import com.flab.simplesharingcar.constants.CarReservationStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SharingCar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sharing_zone_id")
    private SharingZone sharingZone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "standard_car_id")
    private StandardCar standardCar;

    @OneToMany(mappedBy = "sharingCar")
    private List<Reservation> reservations = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private CarReservationStatus status;

    @Builder
    public SharingCar(Long id, SharingZone sharingZone, StandardCar standardCar,
        List<Reservation> reservations, CarReservationStatus status) {
        this.id = id;
        this.sharingZone = sharingZone;
        this.standardCar = standardCar;
        this.reservations = reservations;
        this.status = status;
    }

    public static SharingCar newInstanceByTime(SharingCar from, LocalDateTime time) {
        Long id = from.getId();
        SharingZone sharingZone = from.getSharingZone();
        StandardCar standardCar = from.getStandardCar();
        List<Reservation> reservations = from.getReservations();
        CarReservationStatus status = from.getReservationStatus(time);

        SharingCar result = SharingCar.builder()
            .id(id)
            .sharingZone(sharingZone)
            .standardCar(standardCar)
            .reservations(reservations)
            .status(status)
            .build();
        return result;
    }

    private CarReservationStatus getReservationStatus(LocalDateTime searchTime) {
        if (reservations.size() == 0) {
            return status;
        }

        CarReservationStatus result = reservations.stream()
            .filter((reservation) -> {
                LocalDateTime resStartTime = reservation.getResStartTime();
                LocalDateTime resEndTime = reservation.getResEndTime();

                return searchTime.compareTo(resStartTime) > -1
                    && searchTime.isBefore(resEndTime);
            })
            .map(Reservation::getStatus)
            .findFirst()
            .orElse(status);
        return result;
    }
}
