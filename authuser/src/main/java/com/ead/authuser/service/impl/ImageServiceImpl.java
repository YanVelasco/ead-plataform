package com.ead.authuser.service.impl;

import com.ead.authuser.exceptions.InvalidImageException;
import com.ead.authuser.exceptions.NotFoundException;
import com.ead.authuser.repository.UserRepository;
import com.ead.authuser.service.ImageService;
import com.ead.authuser.util.ImageFormatDetector;
import org.imgscalr.Scalr;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private static final int MAX_WIDTH = 400;
    private static final int MAX_HEIGHT = 400;
    private static final float COMPRESSION_QUALITY = 0.65f;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    private final UserRepository userRepository;

    public ImageServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public byte[] processAndCompressImage(MultipartFile imageFile) throws IOException {
        validateFile(imageFile);

        BufferedImage originalImage = ImageIO.read(imageFile.getInputStream());
        if (originalImage == null) {
            throw new InvalidImageException("Arquivo não é uma imagem válida");
        }

        BufferedImage resizedImage = Scalr.resize(originalImage, Scalr.Mode.FIT_TO_WIDTH, MAX_WIDTH, MAX_HEIGHT);
        String format = getImageFormat(imageFile.getContentType());
        return compressImage(resizedImage, format);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> getImageAsBytes(UUID userId) {
        var userModel = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (userModel.getImageUrl() == null || userModel.getImageUrl().length == 0) {
            throw new NotFoundException("Usuário não possui imagem");
        }

        String format = detectImageFormat(userModel.getImageUrl());
        String mimeType = getMimeType(format);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, mimeType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"user_" + userId + "." + format + "\"")
                .body(userModel.getImageUrl());
    }

    private void validateFile(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new InvalidImageException("Arquivo não fornecido");
        }
        if (imageFile.getSize() > MAX_FILE_SIZE) {
            throw new InvalidImageException("Arquivo muito grande. Máximo: 5MB");
        }
        String contentType = imageFile.getContentType();
        if (!isAllowedType(contentType)) {
            throw new InvalidImageException("Formato não suportado. Use JPG, PNG ou GIF");
        }
    }

    private boolean isAllowedType(String contentType) {
        return contentType != null && (
                contentType.equals("image/jpeg") ||
                        contentType.equals("image/png") ||
                        contentType.equals("image/gif") ||
                        contentType.equals("image/jpg")
        );
    }

    private String getImageFormat(String contentType) {
        if (contentType == null) return "jpg";
        return contentType.contains("jpeg") || contentType.contains("jpg") ? "jpg" :
                contentType.contains("png") ? "png" :
                        contentType.contains("gif") ? "gif" : "jpg";
    }

    private byte[] compressImage(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if ("jpg".equalsIgnoreCase(format)) {
            compressAsJpeg(image, baos);
        } else {
            ImageIO.write(image, format, baos);
        }

        return baos.toByteArray();
    }

    private void compressAsJpeg(BufferedImage image, ByteArrayOutputStream baos) throws IOException {
        var writers = ImageIO.getImageWritersByFormatName("jpg");

        if (!writers.hasNext()) {
            ImageIO.write(image, "jpg", baos);
            return;
        }

        ImageWriter writer = writers.next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(COMPRESSION_QUALITY);

        try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
            writer.setOutput(ios);
            writer.write(null, new javax.imageio.IIOImage(image, null, null), param);
        } finally {
            writer.dispose();
        }
    }

    private String detectImageFormat(byte[] imageBytes) {
        return ImageFormatDetector.detectFormat(imageBytes);
    }

    private String getMimeType(String format) {
        return switch (format.toLowerCase()) {
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            default -> "image/jpeg";
        };
    }

}

