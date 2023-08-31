package com.flab.simplesharingcar.service.payment;

import com.flab.simplesharingcar.domain.Payment;
import com.flab.simplesharingcar.domain.ReservationTime;
import com.flab.simplesharingcar.domain.SharingCar;
import com.flab.simplesharingcar.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment makePayment(SharingCar sharingCar, ReservationTime time) {
        Integer price = sharingCar.calculatePriceByTime(time);
        Payment payment = new Payment(price);
        paymentRepository.save(payment);

        return payment;
    }

    public Payment findById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).get();
        return payment;
    }
}
