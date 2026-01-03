package com.ead.authuser.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface ImageService {

    byte[] processAndCompressImage(MultipartFile imageFile) throws IOException;

    ResponseEntity<byte[]> getImageAsBytes(UUID userId);

}

