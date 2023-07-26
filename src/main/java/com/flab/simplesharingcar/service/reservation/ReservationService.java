package com.flab.simplesharingcar.service.reservation;

import com.flab.simplesharingcar.constants.CarReservationStatus;
import com.flab.simplesharingcar.domain.Reservation;
import com.flab.simplesharingcar.domain.ReservationTime;
import com.flab.simplesharingcar.domain.SharingCar;
import com.flab.simplesharingcar.domain.User;
import com.flab.simplesharingcar.repository.ReservationRepository;
import com.flab.simplesharingcar.web.exception.reservation.AlreadyReservationException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Transactional
    public Reservation reserve(Reservation reservation) {

        ReservationTime reservationTime = reservation.getReservationTime();
        SharingCar sharingCar = reservation.getSharingCar();
        Long sharingCarId = sharingCar.getId();
        User user = reservation.getUser();

        try {
            validateAlreadyReservation(sharingCarId, reservationTime);
        } catch (AlreadyReservationException e) {
            log.error("error log={}", "AlreadyReservationException 예약이 이미 존재");
            throw e;
        }

        Reservation saveReservation = Reservation.builder()
            .user(user)
            .sharingCar(sharingCar)
            .reservationTime(reservationTime)
            .status(CarReservationStatus.RESERVED)
            .build();
        reservationRepository.save(saveReservation);

        return saveReservation;
    }

    private void validateAlreadyReservation(Long sharingCarId, ReservationTime reservationTime) {
        List<Reservation> reservedCarList = reservationRepository.findByCarIdAndStatusNotAndResTimeBetween(
            sharingCarId, CarReservationStatus.CANCEL_RESERVATION, reservationTime);
        if (reservedCarList.size() > 0) {
            throw new AlreadyReservationException("이미 예약이 존재 합니다.");
        }
    }
}
