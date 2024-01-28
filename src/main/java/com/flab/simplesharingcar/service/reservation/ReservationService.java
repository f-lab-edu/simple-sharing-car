package com.flab.simplesharingcar.service.reservation;

import com.flab.simplesharingcar.constants.CarReservationStatus;
import com.flab.simplesharingcar.domain.*;
import com.flab.simplesharingcar.repository.PaymentRepository;
import com.flab.simplesharingcar.repository.ReservationRepository;
import com.flab.simplesharingcar.repository.SharingCarRepository;
import com.flab.simplesharingcar.repository.UserRepository;
import com.flab.simplesharingcar.web.exception.reservation.FailReservationException;
import java.util.List;
import java.util.Objects;

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
    private final SharingCarRepository sharingCarRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    @Transactional
    public Reservation reserve(Long sharingCarId, Long userId, Long paymentId, ReservationTime reservationTime) {


        Reservation saveReservation = null;

        try {

            saveReservation = validateAndGetSaveReservation(sharingCarId, userId, paymentId, reservationTime);
            reservationRepository.save(saveReservation);

        } catch (FailReservationException fre) {
            log.error("error log={}", "FailReservationException : 예약 실패");
            throw fre;
        } catch (Exception e) {
            throw new FailReservationException(e.getMessage());
        }

        return saveReservation;
    }

    private Reservation validateAndGetSaveReservation(Long sharingCarId, Long userId, Long paymentId, ReservationTime reservationTime) {

        SharingCar sharingCar = sharingCarRepository.findById(sharingCarId)
                .orElseThrow(() -> new FailReservationException("대상 공유 차 정보는 필수 입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new FailReservationException("예약자 정보는 필수 입니다."));
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new FailReservationException("결제 정보는 필수 입니다."));

        validateAlreadyReservation(sharingCarId, reservationTime);

        Integer price = sharingCar.calculatePriceByTime(reservationTime);

        if (!Objects.equals(price, payment.getPrice())) {
            throw new FailReservationException("결제 정보가 일치 하지 않습니다.");
        }

        Reservation saveReservation = Reservation.builder()
                .user(user)
                .sharingCar(sharingCar)
                .reservationTime(reservationTime)
                .payment(payment)
                .status(CarReservationStatus.RESERVED)
                .build();

        return saveReservation;
    }

    private void validateAlreadyReservation(Long sharingCarId, ReservationTime reservationTime) {
        List<Reservation> reservedCarList = reservationRepository.findByCarIdAndStatusNotAndResTimeBetween(
            sharingCarId, CarReservationStatus.CANCEL_RESERVATION, reservationTime);
        if (!reservedCarList.isEmpty()) {
            throw new FailReservationException("이미 예약이 존재 합니다.");
        }
    }

}
