package com.netfliz.admin.validator;

import jakarta.validation.ValidationException;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
public class FileValidator {
    private static final long MAX_IMAGE_FILE_SIZE = 5 * 1024 * 1024; // 5MB in bytes
    private static final List<String> FILE_FORMAT_SUPPORT = List.of("jpeg", "jpg", "png");
    private final Tika tika = new Tika();

    public void validateImage(MultipartFile file) {
        if (Objects.isNull(file) || file.isEmpty()) {
            throw new ValidationException("File không được để trống!");
        }

        // Check file size
        if (file.getSize() > MAX_IMAGE_FILE_SIZE) {
            throw new ValidationException("Kích thước file không được vượt quá 5MB");
        }

        String fileType;
        try {
            fileType = tika.detect(file.getInputStream());
        } catch (IOException e) {
            throw new ValidationException("Lỗi khi đọc file file: " + e.getMessage());
        }

        if (!fileType.startsWith("image/")) {
            throw new ValidationException("File không phải ảnh.");
        }

        // Chỉ support file jpeg/jpg/png
        String ext = fileType.split("/")[1];
        if (!FILE_FORMAT_SUPPORT.contains(ext)) {
            throw new ValidationException("Chỉ hỗ trợ định dạng jpeg/jpg/png");
        }
    }
}
