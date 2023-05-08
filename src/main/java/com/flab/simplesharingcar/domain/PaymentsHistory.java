package com.flab.simplesharingcar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class PaymentsHistory {

    private Long id;

    private Long reservationId;

    private Integer price;
}
