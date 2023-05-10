package com.flab.simplesharingcar.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class User {

    private Long id;

    private String email;

    private String password;

    private String name;

}
