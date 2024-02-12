package com.flab.simplesharingcar.service.reservation;

import com.flab.simplesharingcar.constants.CarReservationStatus;
import com.flab.simplesharingcar.domain.*;
import com.flab.simplesharingcar.repository.ReservationRepository;
import com.flab.simplesharingcar.repository.SharingCarRepository;
import com.flab.simplesharingcar.repository.UserRepository;
import com.flab.simplesharingcar.service.payment.PaymentService;
import com.flab.simplesharingcar.exception.reservation.FailReservationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import com.flab.simplesharingcar.web.exception.NotLoginException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReservationService {

    private final PaymentService paymentService;
    private final ReservationRepository reservationRepository;
    private final SharingCarRepository sharingCarRepository;
    private final UserRepository userRepository;

    @Transactional
    public Reservation reserve(Long sharingCarId, Long userId, Long paymentId, ReservationTime reservationTime) {


        Reservation saveReservation = null;

        try {

            saveReservation = validateAndGetSaveReservation(sharingCarId, userId, paymentId, reservationTime);
            reservationRepository.save(saveReservation);

        } catch (FailReservationException fre) {
            paymentService.cancel(paymentId);
            throw fre;
        } catch (Exception e) {
            paymentService.cancel(paymentId);
            throw new FailReservationException(e.getMessage());
        }

        return saveReservation;
    }

    private Reservation validateAndGetSaveReservation(Long sharingCarId, Long userId, Long paymentId, ReservationTime reservationTime) {

        SharingCar sharingCar = sharingCarRepository.findById(sharingCarId)
                .orElseThrow(() -> new FailReservationException("대상 공유 차 정보는 필수 입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new FailReservationException("예약자 정보는 필수 입니다."));
        Payment payment = getPayment(paymentId);

        validateAlreadyReservation(sharingCarId, reservationTime);

        Integer price = sharingCar.calculatePriceByTime(reservationTime);

        if (!Objects.equals(price, payment.getPrice())) {
            throw new FailReservationException("결제 정보가 일치 하지 않습니다.");
        }

        return Reservation.builder()
                .user(user)
                .sharingCar(sharingCar)
                .reservationTime(reservationTime)
                .payment(payment)
                .status(CarReservationStatus.RESERVED)
                .build();
    }

    private Payment getPayment(Long paymentId) {
        Payment payment = paymentService.findById(paymentId);

        if (payment == null) {
            throw new FailReservationException("결제 정보는 필수 입니다.");
        }
        return payment;
    }

    private void validateAlreadyReservation(Long sharingCarId, ReservationTime reservationTime) {
        List<Reservation> reservedCarList = reservationRepository.findByCarIdAndStatusNotAndResTimeBetween(
            sharingCarId, CarReservationStatus.CANCEL_RESERVATION, reservationTime);
        if (!reservedCarList.isEmpty()) {
            throw new FailReservationException("이미 예약이 존재 합니다.");
        }
    }

    @Transactional
    public Reservation cancel(Long reservationId, Long userId) {

        Reservation reservation = validateAndGetReservation(reservationId, userId);

        ReservationTime reservationTime = reservation.getReservationTime();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = reservationTime.getStartTime();

        if (now.isBefore(startTime)) {
            Payment payment = reservation.getPayment();
            Long paymentId = payment.getId();

            paymentService.cancel(paymentId);
        }

        reservation.cancel();

        return reservation;
    }

    private Reservation validateAndGetReservation(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findByIdAndStatus(reservationId, CarReservationStatus.RESERVED)
                .orElseThrow(() -> new NoSuchElementException("예약을 찾을 수 없습니다."));

        User user = reservation.getUser();
        Long findUserId = user.getId();

        if (!Objects.equals(userId, findUserId)) {
            throw new NotLoginException("회원 정보가 일치 하지 않습니다.");
        }

        return reservation;
    }


}
