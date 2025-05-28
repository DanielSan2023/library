package com.example.library.service.service;

import com.example.library.entity.UserInfo;

import java.util.Optional;

public interface UserInfoService {
    UserInfo saveUser(UserInfo user);

    Optional<UserInfo> findUserByEmail(String email);

    // TODO   UserInfo addBookCopiesToUser

    //TODO  boolean deleteUserById(String id);
}
