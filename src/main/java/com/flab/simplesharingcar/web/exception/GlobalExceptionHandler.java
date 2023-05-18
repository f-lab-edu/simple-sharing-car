package com.flab.simplesharingcar.web.exception;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorResponse> validException(MethodArgumentNotValidException exception) {
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
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("[");
        messageBuilder.append(error.getField());
        messageBuilder.append("]은(는) ");
        messageBuilder.append(error.getDefaultMessage());
        return messageBuilder.toString();
    }
}
