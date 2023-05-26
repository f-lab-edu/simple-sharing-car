package com.flab.simplesharingcar.web.exception.user;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FailLoginException extends RuntimeException {

    public FailLoginException(String message) {
        super(message);
    }

}
