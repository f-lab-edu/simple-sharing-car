package com.flab.simplesharingcar.web.exception.user;

import com.flab.simplesharingcar.web.exception.ErrorResponse;
import com.flab.simplesharingcar.web.exception.ErrorStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(DuplicateEmailException.class)
    private ResponseEntity<ErrorResponse> duplicateEmailException(DuplicateEmailException exception) {
        String errorMessage = exception.getMessage();

        ErrorResponse responseBody = ErrorResponse.builder()
            .code(ErrorStatus.DUPLICATE_EMAIL.toString())
            .message(errorMessage)
            .build();

        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }
}
