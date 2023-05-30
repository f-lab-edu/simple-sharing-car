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
public class SignInFormDto {

    @NotBlank(message = "이메일은 필수 입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "패스워드는 필수 입니다.")
    private String password;

    public User toDomain() {
        User userDomain = User.builder()
            .email(email)
            .password(password)
            .build();
        return userDomain;
    }

    public static SignInFormDto from(User user) {
        String email = user.getEmail();
        String password = user.getPassword();

        SignInFormDto signInFormDto = SignInFormDto.builder()
            .email(email)
            .password(password)
            .build();
        return signInFormDto;
    }
}
