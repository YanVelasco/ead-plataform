package com.ead.authuser.service.impl;

import com.ead.authuser.dtos.ImageMetadataDto;
import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.exceptions.AlreadyExistsException;
import com.ead.authuser.exceptions.NotFoundException;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.repository.UserRepository;
import com.ead.authuser.service.IImageStorageService;
import com.ead.authuser.service.UserService;
import com.ead.authuser.util.ImageFormatDetector;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private final IImageStorageService imageStorageService;

    public UserServiceImpl(UserRepository userRepository, IImageStorageService imageStorageService) {
        this.userRepository = userRepository;
        this.imageStorageService = imageStorageService;
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
    public UserModel updateById(UUID userId, UserDto userDto) {
        var userModel = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
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
        var userModel = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        try {
            // Compactar e preparar imagem para armazenar no BD
            var imageData = imageStorageService.compressAndPrepareImage(imageFile);

            // Armazenar bytes da imagem diretamente no BD
            userModel.setImageUrl(imageData.imageBytes());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

            return userRepository.save(userModel);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar a imagem: " + e.getMessage(), e);
        }
    }

    @Override
    public String getImageAsBase64(UUID userId) {
        var userModel = getUserModelWithImage(userId);
        return imageStorageService.getImageAsBase64(userModel.getImageUrl());
    }

    @Override
    public ImageMetadataDto getImageWithMetadata(UUID userId) {
        var userModel = getUserModelWithImage(userId);
        return imageStorageService.getImageWithMetadata(userModel.getImageUrl());
    }

    @Override
    public ResponseEntity<byte[]> getImageAsBytes(UUID userId) {
        var userModel = getUserModelWithImage(userId);
        byte[] imageBytes = imageStorageService.getImageAsBytes(userModel.getImageUrl());

        String format = detectImageFormat(imageBytes);
        String mimeType = getMimeType(format);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .contentLength(imageBytes.length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"profile-image." + format + "\"")
                .body(imageBytes);
    }

    /**
     * Helper para validar se usuário existe e possui imagem
     */
    private UserModel getUserModelWithImage(UUID userId) {
        var userModel = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (userModel.getImageUrl() == null || userModel.getImageUrl().length == 0) {
            throw new NotFoundException("Usuário não possui imagem");
        }

        return userModel;
    }

    private String getMimeType(String format) {
        return switch (format.toLowerCase()) {
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            default -> "image/jpeg";
        };
    }


    private String detectImageFormat(byte[] imageBytes) {
        return ImageFormatDetector.detectFormat(imageBytes);
    }

}

