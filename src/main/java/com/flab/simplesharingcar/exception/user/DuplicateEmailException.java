package com.flab.simplesharingcar.exception.user;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String message) {
        super(message);
    }
}
