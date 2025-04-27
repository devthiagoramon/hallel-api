package br.api.hallel.moduloAPI.utils;

import br.api.hallel.moduloAPI.exceptions.main.InvalidFileTypeException;

import java.util.List;

public class ImageTypeValidator {


    private static final List<String> ALLOWED_TYPES = List.of(
            "image/jpeg",  // JPEG
            "image/png",   // PNG
            "image/gif",   // GIF
            "image/bmp",   // BMP
            "image/webp",  // WEBP
            "image/tiff",  // TIFF
            "image/svg+xml" // SVG
                                                             );

    public static boolean isTypeValid(String imageType)  {
        return ALLOWED_TYPES.contains(imageType);
    }
}
