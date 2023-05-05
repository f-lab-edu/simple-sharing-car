package com.flab.simplesharingcar.domain;

import lombok.Getter;

@Getter
public class PaymentsHistory {

    private Long id;

    private Long reservationId;

    private Integer price;
}
