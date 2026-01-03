package com.ead.authuser.service;

import com.ead.authuser.dtos.ImageDataDto;
import com.ead.authuser.dtos.ImageMetadataDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Interface para serviço de armazenamento de imagens
 * Define contrato para compressão e recuperação de imagens no banco de dados
 */
public interface IImageStorageService {

    ImageDataDto compressAndPrepareImage(MultipartFile imageFile) throws IOException;

    /**
     * Recupera imagem como Base64
     * @param imageBytes Bytes da imagem
     * @return String com formato "data:image/jpg;base64,..."
     */
    String getImageAsBase64(byte[] imageBytes);

    /**
     * Recupera imagem com metadados
     * @param imageBytes Bytes da imagem
     * @return ImageMetadataDto com dados completos
     */
    ImageMetadataDto getImageWithMetadata(byte[] imageBytes);

    /**
     * Recupera imagem como bytes puros
     * @param imageBytes Bytes da imagem
     * @return byte[] da imagem
     */
    byte[] getImageAsBytes(byte[] imageBytes);
}

