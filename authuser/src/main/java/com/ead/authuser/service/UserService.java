package com.ead.authuser.service;

import com.ead.authuser.model.UserModel;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserModel> findAll();

    UserModel findById(UUID userId);

    void deleteById(UUID userId);

    UserModel updateById(UUID userId, UserModel userModel);
}
