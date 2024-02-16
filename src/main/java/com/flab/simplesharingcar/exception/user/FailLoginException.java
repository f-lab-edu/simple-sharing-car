package com.flab.simplesharingcar.exception.user;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FailLoginException extends RuntimeException {

    public FailLoginException(String message) {
        super(message);
    }

}
