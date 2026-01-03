package com.ead.authuser.controller;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserModel>> getUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable UUID userId) {
        userService.deleteById(userId);
        return  ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateUserImage(@PathVariable UUID userId, @RequestParam("file") MultipartFile imageFile) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateImage(userId, imageFile));
    }

    @GetMapping("/{userId}/image")
    public ResponseEntity<Object> getUserImage(@PathVariable UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getImageAsBase64(userId));
    }

    @GetMapping("/{userId}/image/metadata")
    public ResponseEntity<Object> getUserImageWithMetadata(@PathVariable UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getImageWithMetadata(userId));
    }

    @GetMapping("/{userId}/image/view")
    public ResponseEntity<byte[]> viewUserImage(@PathVariable UUID userId) {
        return userService.getImageAsBytes(userId);
    }

}
