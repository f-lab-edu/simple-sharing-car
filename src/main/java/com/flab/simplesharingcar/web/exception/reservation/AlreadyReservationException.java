package com.flab.simplesharingcar.web.exception.reservation;

public class AlreadyReservationException extends RuntimeException {

    public AlreadyReservationException(String message) {
        super(message);
    }

}
