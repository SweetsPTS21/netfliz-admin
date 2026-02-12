package com.netfliz.admin.mapper;

import com.netfliz.admin.dto.FileDto;
import com.netfliz.admin.entity.FileEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class FileMapper {
    public FileEntity buildFileEntity(MultipartFile file,
                                      String fileName,
                                      String downloadUri,
                                      String category,
                                      String username) {
        return FileEntity.builder()
                .fileName(fileName)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .fileDownloadUri(downloadUri)
                .fileExtension(Objects
                        .requireNonNull(file.getOriginalFilename())
                        .substring(file.getOriginalFilename().lastIndexOf(".") + 1))
                .fileCategory(category)
                .fileOwner(username)
                .fileUploader(username)
                .build();
    }

    public FileDto mapToDto(FileEntity entity) {
        return FileDto.builder()
                .id(entity.getId())
                .name(entity.getFileName())
                .ext(entity.getFileExtension())
                .category(entity.getFileCategory())
                .url(entity.getFileDownloadUri())
                .build();
    }

    public List<FileDto> mapToModels(List<FileEntity> entityList) {
        return entityList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}
