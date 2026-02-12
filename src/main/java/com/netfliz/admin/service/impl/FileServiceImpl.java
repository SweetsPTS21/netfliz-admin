package com.netfliz.admin.service.impl;

import com.netfliz.admin.constant.UploadKey;
import com.netfliz.admin.dto.FileDto;
import com.netfliz.admin.entity.FileEntity;
import com.netfliz.admin.mapper.FileMapper;
import com.netfliz.admin.repository.FileRepository;
import com.netfliz.admin.service.B2Service;
import com.netfliz.admin.service.FileService;
import com.netfliz.admin.service.ImageResizerService;
import com.netfliz.admin.validator.FileValidator;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileValidator fileValidator;
    private final FileMapper fileMapper;
    private final FileRepository fileRepository;
    private final B2Service b2Service;
    private final ImageResizerService imageResizerService;
    private final Tika tika = new Tika();
    private final Executor uploadExecutor;

    @Override
    public List<FileDto> uploadMoviePoster(MultipartFile file) {
        fileValidator.validateImage(file);
        List<CompletableFuture<FileEntity>> uploadTasks = new ArrayList<>();

        // Upload resized images in parallel
        for (int width : UploadKey.TARGET_WIDTHS) {
            final int currentWidth = width;
            CompletableFuture<FileEntity> uploadTask = CompletableFuture.supplyAsync(() ->
                            uploadResizedImage(file, currentWidth),
                    uploadExecutor);
            uploadTasks.add(uploadTask);
        }

        // Wait for all uploads to complete
        List<FileEntity> fileEntityList = uploadTasks.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        // Save all files to database
        return fileMapper.mapToModels(fileRepository.saveAll(fileEntityList));
    }

    @Override
    public FileDto uploadMovieGallery(MultipartFile file) {
        fileValidator.validateImage(file);
        int width = 1024;

        return fileMapper.mapToDto(uploadResizedImage(file, width));
    }

    @Override
    public FileDto uploadMovieAsset(MultipartFile file) {
        return null;
    }

    /**
     * Resize image and upload to B2
     */
    public FileEntity uploadResizedImage(MultipartFile file, Integer width) {
        try {
            String fileType = tika.detect(file.getInputStream());
            String ext = fileType.split("/")[1]; // jpeg, png
            String outputFormat = ext.equals("jpeg") ? "jpg" : ext;
            String uuid = UUID.randomUUID().toString();

            byte[] resized = imageResizerService.resize(file.getBytes(), width, outputFormat);
            String filename = String.format("%s-%dw.%s", uuid, width, outputFormat);
            String path = String.format("%s/%s", UploadKey.IMAGES_PATH, filename);

            // upload and ger url
            log.info("Uploading resized image: {}", path);
            String url = b2Service.upload(resized, path, fileType);

            return fileMapper.buildFileEntity(file, filename, url, String.valueOf(width), "ADMIN");
        } catch (Exception e) {
            log.error("Lỗi khi upload file {} ", e.getMessage());
            throw new ValidationException("Lỗi khi upload file: " + e.getMessage());
        }
    }
}
