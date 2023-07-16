package com.flab.simplesharingcar.constants;

import lombok.Getter;

@Getter
public enum CarReservationStatus {
    WAITING("대기"),
    RESERVED("예약 중"),
    CANCEL_RESERVATION("예약 취소"),
    DRIVING("운행 중"),
    DELAY_RETURN("반납 지연");

    String name;

    CarReservationStatus(String name) {
        this.name = name;
    }
}
