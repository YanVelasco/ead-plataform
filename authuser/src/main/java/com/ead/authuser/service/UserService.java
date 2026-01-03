package com.ead.authuser.service;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.dtos.ImageMetadataDto;
import com.ead.authuser.model.UserModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserModel> findAll();

    UserModel findById(UUID userId);

    void deleteById(UUID userId);

    UserModel updateById(UUID userId, UserDto userModel);

    UserModel registerUser(UserDto userDto);

    UserModel updateImage(UUID userId, MultipartFile imageFile);

    String getImageAsBase64(UUID userId);

    ImageMetadataDto getImageWithMetadata(UUID userId);

    ResponseEntity<byte[]> getImageAsBytes(UUID userId);
}
