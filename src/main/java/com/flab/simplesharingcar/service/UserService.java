package com.flab.simplesharingcar.service;

import com.flab.simplesharingcar.domain.User;
import com.flab.simplesharingcar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long join(User user) {
        validateDuplicateEmail(user.getEmail());
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateEmail(String email) {
        User findByEmail = findByEmail(email);
        if (findByEmail != null) {
            throw new IllegalStateException("이미 존재 하는 Email 입니다.");
        }
    }

    private User findByEmail(String email) {
        User findUser = User.builder()
            .email(email)
            .build();
        return userRepository.selectUser(findUser);
    }

    public User findById(Long id) {
        User findUser = User.builder()
            .id(id)
            .build();
        return userRepository.selectUser(findUser);
    }
}
