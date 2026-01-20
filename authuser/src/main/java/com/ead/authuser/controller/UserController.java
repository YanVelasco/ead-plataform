package com.ead.authuser.controller;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.service.ImageService;
import com.ead.authuser.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<List<UserModel>> getUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserModel> updateUserById(
            @PathVariable UUID userId,
            @RequestBody @JsonView(UserDto.UserView.UserPut.class) @Validated(UserDto.UserView.UserPut.class) UserDto userDto) {
        getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateById(userId, userDto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable UUID userId) {
        getUserById(userId);
        userService.deleteById(userId);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<UserModel> updateUserImage(
            @PathVariable UUID userId,
            @RequestParam("file") MultipartFile imageFile) {
        getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateImage(userId, imageFile));
    }

    @GetMapping("/{userId}/image")
    public ResponseEntity<byte[]> getUserImage(@PathVariable UUID userId) {
        return imageService.getImageAsBytes(userId);
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updateUserPassword(
            @PathVariable UUID userId,
            @RequestBody @JsonView(UserDto.UserView.PasswordPut.class) @Validated(UserDto.UserView.PasswordPut.class) UserDto userDto) {
        var usermodel = userService.findById(userId);
        userService.updatePassword(usermodel, userDto);
        return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully.");
    }

}
