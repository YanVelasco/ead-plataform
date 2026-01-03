package com.ead.authuser.dtos;

/**
 * Record para dados da imagem a ser armazenada no banco de dados
 * Records são imutáveis e geram automaticamente constructores, getters, equals, hashCode e toString
 */
public record ImageDataDto(
        byte[] imageBytes,  // Bytes da imagem compactada
        String format,      // jpg, png, gif
        long size           // Tamanho em bytes
) {
}

