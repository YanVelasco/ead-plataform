package com.ead.authuser.controller;

import com.ead.authuser.dtos.PageDto;
import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.dtos.UserFilterDto;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.service.ImageService;
import com.ead.authuser.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ImageService imageService;

    public UserController(UserService userService, ImageService imageService) {
        this.userService = userService;
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity<PageDto<UserModel>> getAllUsers(
            @ModelAttribute UserFilterDto filter,
            Pageable pageable) {
        log.info("GET /users - filter: {}, pageable: {}", filter, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(PageDto.from(userService.findAll(filter, pageable)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable UUID userId) {
        log.info("GET /users/{} - fetching user", userId);
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserModel> updateUserById(
            @PathVariable UUID userId,
            @RequestBody @JsonView(UserDto.UserView.UserPut.class) @Validated(UserDto.UserView.UserPut.class) UserDto userDto) {
        log.info("PUT /users/{} - updating profile fields", userId);
        getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateById(userId, userDto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable UUID userId) {
        log.info("DELETE /users/{} - deleting user", userId);
        getUserById(userId);
        userService.deleteById(userId);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<UserModel> updateUserImage(
            @PathVariable UUID userId,
            @RequestParam("file") MultipartFile imageFile) {
        log.info("PUT /users/{}/image - filename: {}, contentType: {}, size: {}",
                userId, imageFile.getOriginalFilename(), imageFile.getContentType(), imageFile.getSize());
        getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateImage(userId, imageFile));
    }

    @GetMapping("/{userId}/image")
    public ResponseEntity<byte[]> getUserImage(@PathVariable UUID userId) {
        log.info("GET /users/{}/image - retrieving image", userId);
        return imageService.getImageAsBytes(userId);
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updateUserPassword(
            @PathVariable UUID userId,
            @RequestBody @JsonView(UserDto.UserView.PasswordPut.class) @Validated(UserDto.UserView.PasswordPut.class) UserDto userDto) {
        log.info("PUT /users/{}/password - updating password", userId);
        var usermodel = userService.findById(userId);
        userService.updatePassword(usermodel, userDto);
        return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully.");
    }

}
