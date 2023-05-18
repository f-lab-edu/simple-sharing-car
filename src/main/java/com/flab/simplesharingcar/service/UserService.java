package com.flab.simplesharingcar.service;

import com.flab.simplesharingcar.domain.User;
import com.flab.simplesharingcar.repository.UserRepository;
import com.flab.simplesharingcar.web.exception.user.DuplicateEmailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;


    public User join(User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        String name = user.getName();
        String encodedPassword = hashPassword(password);

        try {
            validateDuplicateEmail(email);
        } catch (DuplicateEmailException exception) {
            log.error("error log={}", exception.getMessage());
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
        User findByEmail = findByEmail(email);
        if (findByEmail != null) {
            throw new DuplicateEmailException("이미 존재 하는 Email 입니다.");
        }
    }

    private String hashPassword(String password) {
        String encodedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        return encodedPassword;
    }

    private User findByEmail(String email) {
        User findUser = User.builder()
            .email(email)
            .build();
        User selectedUser = userRepository.selectUser(findUser);
        return selectedUser;
    }

    public User findById(Long id) {
        User findUser = User.builder()
            .id(id)
            .build();
        User selectedUser = userRepository.selectUser(findUser);
        return selectedUser;
    }

}
