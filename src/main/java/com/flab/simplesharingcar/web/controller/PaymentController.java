package com.flab.simplesharingcar.web.controller;

import com.flab.simplesharingcar.domain.Payment;
import com.flab.simplesharingcar.domain.ReservationTime;
import com.flab.simplesharingcar.domain.SharingCar;
import com.flab.simplesharingcar.service.payment.PaymentService;
import com.flab.simplesharingcar.service.sharing.SharingCarService;
import com.flab.simplesharingcar.web.dto.PaymentRequest;
import com.flab.simplesharingcar.web.dto.PaymentResponse;
import java.time.LocalDateTime;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    private final SharingCarService sharingCarService;

    @PostMapping
    public ResponseEntity<PaymentResponse> reserve(@Valid @RequestBody PaymentRequest request) {
        Long sharingCarId = request.getSharingCarId();
        SharingCar sharingCar = sharingCarService.findById(sharingCarId);

        LocalDateTime resStartTime = request.getResStartTime();
        LocalDateTime resEndTime = request.getResEndTime();
        ReservationTime reservationTime = new ReservationTime(resStartTime, resEndTime);

        Payment payment = paymentService.makePayment(sharingCar, reservationTime);
        PaymentResponse response = PaymentResponse.from(payment);

        return ResponseEntity.ok(response);
    }
}
