package com.ead.authuser.service.impl;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.exceptions.AlreadyExistsException;
import com.ead.authuser.exceptions.NotFoundException;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.repository.UserRepository;
import com.ead.authuser.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Cacheable(value = "users", key = "#userId")
    @Transactional(readOnly = true)
    public UserModel findById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found")
        );
    }

    @Override
    @CacheEvict(value = "users", key = "#userId")
    @Transactional
    public void deleteById(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        userRepository.deleteById(userId);
    }

    @Override
    @CacheEvict(value = "users", key = "#userId")
    @Transactional
    public UserModel updateById(UUID userId, UserModel requestUserModel) {
        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (requestUserModel.getFullName() != null &&
                !requestUserModel.getFullName().equals(userModel.getFullName())) {
            userModel.setFullName(requestUserModel.getFullName());
            userModel.setLastUpdateDate(LocalDateTime.now());
        }

        return userRepository.save(userModel);
    }

    @Override
    public UserModel registerUser(UserDto userDto) {
        if (userRepository.existsByUsernameOrEmail(userDto.username(), userDto.email())) {
            throw new AlreadyExistsException("Username or email already exists");
        }
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.USER);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userRepository.save(userModel);
    }

}