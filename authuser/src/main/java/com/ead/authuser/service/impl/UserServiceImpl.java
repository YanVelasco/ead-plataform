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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
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

@Log4j2
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
        log.info("Finding all users (no filters)");
        return userRepository.findAll();
    }

    @Override
    @Cacheable(value = "users", key = "#userId")
    @Transactional(readOnly = true)
    public UserModel findById(UUID userId) {
        log.info("Finding user by id: {}", userId);
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found")
        );
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "users", key = "#userId"),
            @CacheEvict(value = "users-page", allEntries = true)
    })
    @Transactional
    public void deleteById(UUID userId) {
        log.info("Deleting user by id: {}", userId);
        userRepository.deleteById(userId);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "users", key = "#userId"),
            @CacheEvict(value = "users-page", allEntries = true)
    })
    @Transactional
    public UserModel updateById(UUID userId, UserDto userDto) {
        log.info("Updating user by id: {}", userId);
        var userModel = new UserModel();
        if (userDto.fullName() != null) {
            log.debug("Updating fullName for user {}", userId);
            userModel.setFullName(userDto.fullName());
        }
        if (userDto.phoneNumber() != null) {
            log.debug("Updating phoneNumber for user {}", userId);
            userModel.setPhoneNumber(userDto.phoneNumber());
        }
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        var savedUser = userRepository.save(userModel);
        log.info("User updated successfully: {}", savedUser.getUserId());
        return savedUser;
    }

    @Override
    @CacheEvict(value = "users-page", allEntries = true)
    public UserModel registerUser(UserDto userDto) {
        String normalizedUsername = normalizeCredential(userDto.username());
        String normalizedEmail = normalizeCredential(userDto.email());
        log.info("Registering new user - username: {}, email: {}", normalizedUsername, normalizedEmail);
        if (userRepository.existsByUsernameOrEmail(normalizedUsername, normalizedEmail)) {
            log.warn("Username or email already exists {}, {}", normalizedUsername, normalizedEmail);
            throw new AlreadyExistsException("Username or email already exists");
        }
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUsername(normalizedUsername);
        userModel.setEmail(normalizedEmail);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.USER);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        var savedUser = userRepository.save(userModel);
        log.info("User registered successfully - id: {}", savedUser.getUserId());
        return savedUser;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "users", key = "#userId"),
            @CacheEvict(value = "users-page", allEntries = true)
    })
    @Transactional
    public UserModel updateImage(UUID userId, MultipartFile imageFile) {
        log.info("Updating user image - userId: {}, filename: {}, contentType: {}, size: {}",
                userId, imageFile.getOriginalFilename(), imageFile.getContentType(), imageFile.getSize());
        var userModel = new UserModel();
        try {
            byte[] compressedImage = imageService.processAndCompressImage(imageFile);
            userModel.setImageUrl(compressedImage);
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            var savedUser = userRepository.save(userModel);
            log.info("User image updated successfully - userId: {}", savedUser.getUserId());
            return savedUser;
        } catch (IOException e) {
            log.error("Error processing image for userId {}: {}", userId, e.getMessage());
            throw new RuntimeException("Erro ao processar a imagem: " + e.getMessage(), e);
        }
    }

    @Override
    @CacheEvict(value = "users-page", allEntries = true)
    public void updatePassword(UserModel userModel, UserDto userDto) {
        log.info("Updating password for userId: {}", userModel.getUserId());
        if (userDto.password().equals(userModel.getPassword())) {
            log.warn("New password matches old password for userId: {}", userModel.getUserId());
            throw new SamePasswordException("The new password cannot be the same as the old password");
        }
        userModel.setPassword(userDto.password());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userRepository.save(userModel);
        log.info("Password updated successfully for userId: {}", userModel.getUserId());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            value = "users-page",
            key = "{#filter.username, #filter.email, #filter.fullName, #filter.userStatus, " +
                    "#filter.userType, #filter.courseId, #pageable.pageNumber, #pageable.pageSize, #pageable.sort}",
            condition = "#filter == null || (#filter.username == null && #filter.email == null && " +
                    "#filter.fullName == null && #filter.userStatus == null && #filter.userType == null && " +
                    "#filter.courseId == null)"
    )
    public Page<UserModel> findAll(UserFilterDto filter, Pageable pageable) {
        log.info("Finding users with filters: {}, pageable: {}", filter, pageable);
        Specification<UserModel> spec = UserSpecifications.withFilters(filter);
        return userRepository.findAll(spec, pageable);
    }

    private static String normalizeCredential(String value) {
        return value == null ? null : value.trim().toLowerCase();
    }

}
