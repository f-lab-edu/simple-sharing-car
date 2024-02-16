package com.flab.simplesharingcar.web.controller;

import com.flab.simplesharingcar.domain.Payment;
import com.flab.simplesharingcar.service.payment.PaymentService;
import com.flab.simplesharingcar.web.dto.PaymentRequest;
import com.flab.simplesharingcar.web.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> payment(@Valid @RequestBody PaymentRequest request) {

        int price = request.getPrice();

        Payment payment = paymentService.makePayment(price);
        PaymentResponse response = PaymentResponse.from(payment);

        return ResponseEntity.ok(response);
    }
}
