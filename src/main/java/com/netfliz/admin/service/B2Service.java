package com.netfliz.admin.service;

import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;

public interface B2Service {
    void upload(MultipartFile file, String filePath);

    String upload(byte[] fileBytes, String filePath, String contentType);

    String generatePresignedUrl(String key, Duration validFor);

    void checkSignature(String key, String ts, String signature);
}
