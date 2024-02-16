package com.flab.simplesharingcar.service.user;

import com.flab.simplesharingcar.domain.User;
import com.flab.simplesharingcar.repository.UserRepository;
import com.flab.simplesharingcar.exception.user.DuplicateEmailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SignUpService {

    private final UserRepository userRepository;

    @Transactional
    public User join(User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        String name = user.getName();
        String encodedPassword = hashPassword(password);

        try {
            validateDuplicateEmail(email);
        } catch (DuplicateEmailException exception) {
            log.error("error log={}", "DuplicateEmailException 회원가입 중복 Email 발생");
            // ..
            throw exception;
        }

        User saveUser = User.builder()
            .email(email)
            .password(encodedPassword)
            .name(name)
            .build();
        userRepository.save(saveUser);

        return saveUser;
    }

    private void validateDuplicateEmail(String email) throws DuplicateEmailException {
        User findByEmail = userRepository.findByEmail(email);
        if (findByEmail != null) {
            throw new DuplicateEmailException("이미 존재 하는 Email 입니다.");
        }
    }

    private String hashPassword(String password) {
        String encodedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        return encodedPassword;
    }

}
