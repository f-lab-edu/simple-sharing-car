package com.flab.simplesharingcar.constants;

import lombok.Getter;

@Getter
public enum ReservationStatus {
    WAITING("대기"),
    RESERVED("예약 중"),
    DRIVING("운행 중"),
    DELAY_RETURN("반납 지연");

    String name;

    ReservationStatus(String name) {
        this.name = name;
    }
}
