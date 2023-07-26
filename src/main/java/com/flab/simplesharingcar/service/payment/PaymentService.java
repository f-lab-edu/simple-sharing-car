package com.flab.simplesharingcar.service.payment;

import com.flab.simplesharingcar.domain.Payment;
import com.flab.simplesharingcar.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment makePayment(Integer price) {

        Payment payment = new Payment(price);
        paymentRepository.save(payment);

        return payment;
    }
}
