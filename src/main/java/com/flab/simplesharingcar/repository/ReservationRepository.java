package com.flab.simplesharingcar.repository;

import com.flab.simplesharingcar.constants.CarReservationStatus;
import com.flab.simplesharingcar.domain.Reservation;
import com.flab.simplesharingcar.domain.ReservationTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("select res from Reservation res where res.sharingCar.id = :sharingCarId and res.status <> :status"
        + " and (res.reservationTime.startTime between :#{#reservationTime.startTime} and :#{#reservationTime.endTime} "
        + " or res.reservationTime.endTime between :#{#reservationTime.startTime} and :#{#reservationTime.endTime})"
    )
    List<Reservation> findByCarIdAndStatusNotAndResTimeBetween(
        @Param("sharingCarId") Long sharingCarId,
        @Param("status") CarReservationStatus status,
        @Param("reservationTime") ReservationTime reservationTime);

    Optional<Reservation> findByIdAndStatus(Long id, CarReservationStatus status);
}
