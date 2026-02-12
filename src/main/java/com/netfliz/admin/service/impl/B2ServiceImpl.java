package com.netfliz.admin.service.impl;

import com.netfliz.admin.constant.properties.B2Properties;
import com.netfliz.admin.service.B2Service;
import com.netfliz.admin.util.HashUtil;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;

@Service
@AllArgsConstructor
public class B2ServiceImpl implements B2Service {
    private static final long ALLOWED_SKEW_SECONDS = 300;
    private final S3Client s3;
    private final S3Presigner presigner;
    private final B2Properties b2Properties;

    @Override
    public String upload(byte[] fileBytes, String filePath, String contentType) {
        PutObjectRequest putReq = PutObjectRequest.builder()
                .bucket(b2Properties.getBucketName())
                .key(filePath)
                .contentLength((long) fileBytes.length)
                .contentType(contentType)
                .build();

        try {
            s3.putObject(putReq, RequestBody.fromBytes(fileBytes));
            return generatePresignedUrl(filePath, Duration.ofHours(1));
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    @Override
    public void upload(MultipartFile file, String filePath) {
        PutObjectRequest putReq = PutObjectRequest.builder()
                .bucket(b2Properties.getBucketName())
                .key(filePath)
                .contentLength(file.getSize())
                .contentType(file.getContentType())
                .build();

        try {
            s3.putObject(putReq, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String generatePresignedUrl(String key, Duration validFor) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(b2Properties.getBucketName())
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(validFor)
                .build();

        return presigner.presignGetObject(presignRequest).url().toString();
    }

    @Override
    public void checkSignature(String key, String ts, String signature) {
        long timestampSec;
        try {
            timestampSec = Long.parseLong(ts);
        } catch (NumberFormatException ex) {
            throw new ValidationException("invalid timestamp");
        }
        long now = System.currentTimeMillis() / 1000L;
        if (Math.abs(now - timestampSec) > ALLOWED_SKEW_SECONDS) {
            throw new ValidationException("timestamp skew too large");
        }

        // 2) compute expected HMAC
        String message = HashUtil.buildMessage(key, ts);
        String expected = HashUtil.computeHmacHex(b2Properties.getWorkerSharedSecret(), message);

        // 3) constant-time compare
        if (!HashUtil.constantTimeEquals(expected, signature)) {
            throw new ValidationException("invalid signature");
        }
    }
}
