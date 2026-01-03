package com.ead.authuser.service.impl;

import com.ead.authuser.dtos.ImageDataDto;
import com.ead.authuser.dtos.ImageMetadataDto;
import com.ead.authuser.service.IImageStorageService;
import com.ead.authuser.util.ImageFormatDetector;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * 📦 Serviço de Armazenamento de Imagens
 *
 * Responsabilidades:
 * ✅ Validar arquivo de imagem
 * ✅ Redimensionar para 400x400
 * ✅ Compactar em JPEG com qualidade otimizada
 * ✅ Armazenar bytes no banco de dados
 * ✅ Recuperar imagem em diversos formatos
 */
@Service
public class ImageStorageServiceImpl implements IImageStorageService {

    // ============================================================================
    // CONSTANTES
    // ============================================================================
    private static final int MAX_WIDTH = 400;
    private static final int MAX_HEIGHT = 400;
    private static final float COMPRESSION_QUALITY = 0.65f;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    // ============================================================================
    // MÉTODOS PÚBLICOS (Implementação da Interface)
    // ============================================================================

    /**
     * 1️⃣  UPLOAD: Compacta e prepara imagem para armazenar no BD
     *
     * Fluxo:
     * - Valida arquivo
     * - Lê imagem original
     * - Redimensiona para 400x400
     * - Compacta em JPEG (0.65 qualidade)
     * - Retorna bytes + metadados
     */
    @Override
    public ImageDataDto compressAndPrepareImage(MultipartFile imageFile) throws IOException {
        validateFile(imageFile);

        BufferedImage originalImage = ImageIO.read(imageFile.getInputStream());
        if (originalImage == null) {
            throw new IllegalArgumentException("Arquivo não é uma imagem válida");
        }

        BufferedImage resizedImage = Scalr.resize(originalImage, Scalr.Mode.FIT_TO_WIDTH, MAX_WIDTH, MAX_HEIGHT);
        String format = getImageFormat(imageFile.getContentType());
        byte[] compressedBytes = compressImage(resizedImage, format);

        return new ImageDataDto(compressedBytes, format, compressedBytes.length);
    }

    /**
     * 2️⃣  DOWNLOAD: Recupera imagem como Base64
     *
     * Formato: "data:image/jpg;base64,/9j/4AAQSkZJRg..."
     * Uso: Direto em <img src="..."> do HTML
     */
    @Override
    public String getImageAsBase64(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            throw new IllegalArgumentException("Imagem não fornecida");
        }
        String format = detectImageFormat(imageBytes);
        return "data:image/" + format + ";base64," + Base64.getEncoder().encodeToString(imageBytes);
    }

    /**
     * 3️⃣  DOWNLOAD: Recupera imagem com metadados
     *
     * Retorna: ImageMetadataDto {
     *   - imageData: Base64 completo
     *   - format: jpg/png/gif
     *   - size: tamanho em bytes
     * }
     */
    @Override
    public ImageMetadataDto getImageWithMetadata(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            throw new IllegalArgumentException("Imagem não fornecida");
        }
        String format = detectImageFormat(imageBytes);
        String base64 = "data:image/" + format + ";base64," + Base64.getEncoder().encodeToString(imageBytes);
        return new ImageMetadataDto(base64, format, imageBytes.length);
    }

    /**
     * 4️⃣  DOWNLOAD: Recupera imagem como bytes puros
     *
     * Uso: Retornar como ResponseEntity<byte[]> com Content-Type
     */
    @Override
    public byte[] getImageAsBytes(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            throw new IllegalArgumentException("Imagem não fornecida");
        }
        return imageBytes;
    }

    // ============================================================================
    // MÉTODOS PRIVADOS (Helpers)
    // ============================================================================

    /**
     * Valida arquivo antes de processar
     */
    private void validateFile(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Arquivo não fornecido");
        }
        if (imageFile.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Arquivo muito grande. Máximo: 5MB");
        }
        String contentType = imageFile.getContentType();
        if (!isAllowedType(contentType)) {
            throw new IllegalArgumentException("Formato não suportado. Use JPG, PNG ou GIF");
        }
    }

    /**
     * Verifica se é tipo de imagem permitido
     */
    private boolean isAllowedType(String contentType) {
        return contentType != null && (
            contentType.equals("image/jpeg") ||
            contentType.equals("image/png") ||
            contentType.equals("image/gif") ||
            contentType.equals("image/jpg")
        );
    }

    /**
     * Extrai formato da imagem do Content-Type
     */
    private String getImageFormat(String contentType) {
        if (contentType == null) return "jpg";
        return contentType.contains("jpeg") || contentType.contains("jpg") ? "jpg" :
               contentType.contains("png") ? "png" :
               contentType.contains("gif") ? "gif" : "jpg";
    }

    /**
     * Comprime imagem em bytes
     *
     * Para JPEG: usa compressão com qualidade 0.65
     * Para PNG/GIF: usa compressão padrão do formato
     */
    private byte[] compressImage(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if ("jpg".equalsIgnoreCase(format)) {
            compressAsJpeg(image, baos);
        } else {
            ImageIO.write(image, format, baos);
        }

        return baos.toByteArray();
    }

    /**
     * Comprime especificamente como JPEG com qualidade controlada
     */
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

    /**
     * Detecta formato da imagem usando a classe utilitária ImageFormatDetector
     */
    private String detectImageFormat(byte[] imageBytes) {
        return ImageFormatDetector.detectFormat(imageBytes);
    }
}

