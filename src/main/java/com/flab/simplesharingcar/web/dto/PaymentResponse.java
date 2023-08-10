package com.flab.simplesharingcar.web.dto;

import com.flab.simplesharingcar.domain.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long paymentId;

    public static PaymentResponse from(Payment payment) {
        Long paymentId = payment.getId();

        PaymentResponse result = PaymentResponse.builder()
            .paymentId(paymentId)
            .build();

        return result;
    }
}
