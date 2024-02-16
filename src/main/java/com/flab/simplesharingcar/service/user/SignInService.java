package com.flab.simplesharingcar.service.user;

import com.flab.simplesharingcar.domain.User;
import com.flab.simplesharingcar.repository.UserRepository;
import com.flab.simplesharingcar.exception.user.FailLoginException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SignInService {

    private final UserRepository userRepository;

    public User login(String email, String password) {
        User savedUser = userRepository.findByEmail(email);

        try {
            validateUserExists(savedUser);

            String savedPassword = savedUser.getPassword();
            validatePassword(password, savedPassword);
        } catch (FailLoginException exception) {
            log.error("error log={}", "FailLoginException 로그인 실패");
            // ..
            throw exception;
        }

        return savedUser;
    }

    private void validateUserExists(User savedUser) throws FailLoginException {
        if (savedUser == null) {
            throw new FailLoginException("없는 Email 이거나 Password가 잘못 되었습니다.");
        }
    }

    private void validatePassword(String password, String savedPassword) throws FailLoginException {
        boolean checkPassword = BCrypt.checkpw(password, savedPassword);
        if (!checkPassword) {
            throw new FailLoginException("없는 Email 이거나 Password가 잘못 되었습니다.");
        }
    }

}
