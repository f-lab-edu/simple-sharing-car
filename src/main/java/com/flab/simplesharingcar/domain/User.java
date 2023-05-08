package com.flab.simplesharingcar.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Builder
public class User {

    private Long id;

    private String email;

    private String password;

    private String name;

    public static User createUser(String email, String password, String name) {
        return User.builder()
            .email(email)
            .password(password)
            .name(name)
            .build();
    }

}
