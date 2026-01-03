package com.ead.authuser.util;

/**
 * 🔍 Detector de Formato de Imagem por Magic Bytes
 *
 * Detecta o formato da imagem analisando seus primeiros bytes (assinatura do arquivo)
 *
 * Magic Bytes:
 * ├─ JPEG: FF D8 FF
 * ├─ PNG:  89 50 4E 47
 * └─ GIF:  47 49 46 38
 */
public class ImageFormatDetector {

    private ImageFormatDetector() {
        // Utility class - não instanciável
    }

    /**
     * Detecta formato da imagem pelos magic bytes
     *
     * @param imageBytes bytes da imagem
     * @return formato detectado: "jpg", "png", "gif" ou "jpg" (padrão)
     */
    public static String detectFormat(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length < 3) {
            return "jpg";
        }

        // JPEG: FF D8 FF
        if (isJpeg(imageBytes)) {
            return "jpg";
        }

        // PNG: 89 50 4E 47
        if (isPng(imageBytes)) {
            return "png";
        }

        // GIF: 47 49 46 38
        if (isGif(imageBytes)) {
            return "gif";
        }

        return "jpg";
    }

    /**
     * Verifica se é JPEG (FF D8 FF)
     */
    private static boolean isJpeg(byte[] imageBytes) {
        return imageBytes.length >= 3 &&
                imageBytes[0] == (byte) 0xFF &&
                imageBytes[1] == (byte) 0xD8 &&
                imageBytes[2] == (byte) 0xFF;
    }

    /**
     * Verifica se é PNG (89 50 4E 47)
     */
    private static boolean isPng(byte[] imageBytes) {
        return imageBytes.length >= 4 &&
                imageBytes[0] == (byte) 0x89 &&
                imageBytes[1] == (byte) 0x50 &&
                imageBytes[2] == (byte) 0x4E &&
                imageBytes[3] == (byte) 0x47;
    }

    /**
     * Verifica se é GIF (47 49 46 38)
     */
    private static boolean isGif(byte[] imageBytes) {
        return imageBytes.length >= 4 &&
                imageBytes[0] == (byte) 0x47 &&
                imageBytes[1] == (byte) 0x49 &&
                imageBytes[2] == (byte) 0x46 &&
                imageBytes[3] == (byte) 0x38;
    }
}

