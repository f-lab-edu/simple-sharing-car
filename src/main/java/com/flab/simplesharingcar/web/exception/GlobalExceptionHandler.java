package com.flab.simplesharingcar.web.exception;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    private ResponseEntity<ErrorResponse> validException(BindException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> errors = bindingResult.getFieldErrors();
        String errorMessage = makeMessageByFieldErrors(errors);

        ErrorResponse responseBody = ErrorResponse.builder()
            .code(ErrorStatus.NOT_VALID_ARGUMENT.toString())
            .message(errorMessage)
            .build();

        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    private String makeMessageByFieldErrors(List<FieldError> errors) {
        String message = errors.stream()
            .map(this::makeMessageByFieldError)
            .collect(Collectors.joining("\n"));
        return message;
    }

    private String makeMessageByFieldError(FieldError error) {
        String fieldName = error.getField();
        String fieldMessage = error.getDefaultMessage();
        String errorMessage = String.format("[%1$s]은(는) %2$s", fieldName, fieldMessage);

        return errorMessage;
    }

    @ExceptionHandler(NotLoginException.class)
    private ResponseEntity<ErrorResponse> notLoginException(NotLoginException exception) {
        String errorMessage = exception.getMessage();

        ErrorResponse responseBody = ErrorResponse.builder()
            .code(ErrorStatus.NOT_LOGIN.toString())
            .message(errorMessage)
            .build();

        return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoSuchElementException.class)
    private ResponseEntity<ErrorResponse> noSuchElementException(RuntimeException exception) {
        String errorMessage = exception.getMessage();

        ErrorResponse responseBody = ErrorResponse.builder()
                .code(ErrorStatus.NO_SUCH_ELEMENT.toString())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }
}
