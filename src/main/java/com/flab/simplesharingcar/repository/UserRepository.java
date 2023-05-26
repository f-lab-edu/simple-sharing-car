package com.flab.simplesharingcar.repository;

import com.flab.simplesharingcar.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRepository {
    Long save(@Param("user") User user);
    User selectByEmail(@Param("email") String email);
}
