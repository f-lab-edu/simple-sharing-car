package com.flab.simplesharingcar.web.dto;

import com.flab.simplesharingcar.domain.User;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {

    @NotBlank(message = "이메일은 필수 입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "패스워드는 필수 입니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입니다.")
    private String name;

    public User toDomain() {
        User userDomain = User.builder()
            .email(email)
            .password(password)
            .name(name)
            .build();
        return userDomain;
    }

    public static SignUpDto from(User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        String name = user.getName();

        SignUpDto signUpDto = SignUpDto.builder()
            .email(email)
            .password(password)
            .name(name)
            .build();
        return signUpDto;
    }
}
