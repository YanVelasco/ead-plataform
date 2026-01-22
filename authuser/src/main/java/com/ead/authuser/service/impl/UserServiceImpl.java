package com.ead.authuser.service.impl;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.dtos.UserFilterDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.exceptions.AlreadyExistsException;
import com.ead.authuser.exceptions.NotFoundException;
import com.ead.authuser.exceptions.SamePasswordException;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.repository.UserRepository;
import com.ead.authuser.service.ImageService;
import com.ead.authuser.service.UserService;
import com.ead.authuser.specifications.UserSpecifications;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ImageService imageService;

    public UserServiceImpl(UserRepository userRepository, ImageService imageService) {
        this.userRepository = userRepository;
        this.imageService = imageService;
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
        userRepository.deleteById(userId);
    }

    @Override
    @CacheEvict(value = "users", key = "#userId")
    @Transactional
    public UserModel updateById(UUID userId, UserDto userDto) {
        var userModel = new UserModel();
        if (userDto.fullName() != null) {
            userModel.setFullName(userDto.fullName());
        }
        if (userDto.phoneNumber() != null) {
            userModel.setPhoneNumber(userDto.phoneNumber());
        }
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
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

    @Override
    @CacheEvict(value = "users", key = "#userId")
    @Transactional
    public UserModel updateImage(UUID userId, MultipartFile imageFile) {
        var userModel = new UserModel();
        try {
            byte[] compressedImage = imageService.processAndCompressImage(imageFile);
            userModel.setImageUrl(compressedImage);
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            return userRepository.save(userModel);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar a imagem: " + e.getMessage(), e);
        }
    }

    @Override
    public void updatePassword(UserModel userModel, UserDto userDto) {
        if (userDto.password().equals(userModel.getPassword())) {
            throw new SamePasswordException("The new password cannot be the same as the old password");
        }
        userModel.setPassword(userDto.password());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userRepository.save(userModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserModel> findAll(UserFilterDto filter, Pageable pageable) {
        Specification<UserModel> spec = UserSpecifications.withFilters(filter);
        return userRepository.findAll(spec, pageable);
    }

}
