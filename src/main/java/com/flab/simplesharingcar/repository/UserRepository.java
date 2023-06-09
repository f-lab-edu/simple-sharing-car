package com.flab.simplesharingcar.repository;

import com.flab.simplesharingcar.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);
    User findByEmail(String email);
}
