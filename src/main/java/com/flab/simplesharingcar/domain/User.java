package com.flab.simplesharingcar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class User {

    private Long id;

    private String email;

    private String password;

    private String name;

}
