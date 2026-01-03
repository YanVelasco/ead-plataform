package com.ead.authuser.dtos;

/**
 * Record para metadados da imagem (Base64 + formato + tamanho)
 * Records são imutáveis e geram automaticamente constructores, getters, equals, hashCode e toString
 */
public record ImageMetadataDto(
        String imageData,  // Base64 completo: "data:image/jpg;base64,..."
        String format,     // jpg, png, gif
        long size          // Tamanho em bytes
) {
}

