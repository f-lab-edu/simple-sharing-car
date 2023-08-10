package com.flab.simplesharingcar.web.controller;

import com.flab.simplesharingcar.constants.SessionKey;
import com.flab.simplesharingcar.domain.Payment;
import com.flab.simplesharingcar.domain.Reservation;
import com.flab.simplesharingcar.domain.ReservationTime;
import com.flab.simplesharingcar.domain.SharingCar;
import com.flab.simplesharingcar.domain.User;
import com.flab.simplesharingcar.service.payment.PaymentService;
import com.flab.simplesharingcar.service.reservation.ReservationService;
import com.flab.simplesharingcar.service.sharing.SharingCarService;
import com.flab.simplesharingcar.web.dto.ReservationRequest;
import com.flab.simplesharingcar.web.dto.ReservationResponse;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    private final SharingCarService sharingCarService;

    private final PaymentService paymentService;

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> reserve(@Valid @RequestBody ReservationRequest request,
            HttpServletRequest servletRequest) {
        Long paymentId = request.getPaymentId();
        Payment payment = paymentService.findById(paymentId);

        Long sharingCarId = request.getSharingCarId();
        SharingCar sharingCar = sharingCarService.findById(sharingCarId);

        LocalDateTime resStartTime = request.getResStartTime();
        LocalDateTime resEndTime = request.getResEndTime();
        ReservationTime reservationTime = new ReservationTime(resStartTime, resEndTime);

        HttpSession session = servletRequest.getSession();
        User loginUser = (User) session.getAttribute(SessionKey.LOGIN_USER);

        Reservation reservation = Reservation.builder()
            .payment(payment)
            .user(loginUser)
            .sharingCar(sharingCar)
            .reservationTime(reservationTime)
            .build();

        Reservation reserve = reservationService.reserve(reservation);
        ReservationResponse response = ReservationResponse.from(reserve);
        return ResponseEntity.ok(response);
    }

}
