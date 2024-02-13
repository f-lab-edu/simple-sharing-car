package com.flab.simplesharingcar.web.exception.reservation;

import com.flab.simplesharingcar.exception.reservation.FailReservationException;
import com.flab.simplesharingcar.web.exception.ErrorResponse;
import com.flab.simplesharingcar.web.exception.ErrorStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ReservationExceptionHandler {

    @ExceptionHandler(FailReservationException.class)
    public ResponseEntity<ErrorResponse> failReservationException(FailReservationException exception) {
        String errorMessage = exception.getMessage();

        ErrorResponse responseBody = ErrorResponse.builder()
                .code(ErrorStatus.FAIL_RESERVE.toString())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

}
