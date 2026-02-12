package com.netfliz.admin.controller;

import com.netfliz.admin.dto.FileDto;
import com.netfliz.admin.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@AllArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/upload-poster")
    public ResponseEntity<List<FileDto>> uploadMoviePoster(MultipartFile file) {
        return ResponseEntity.ok(fileService.uploadMoviePoster(file));
    }

    @PostMapping("/upload-gallery")
    public ResponseEntity<FileDto> uploadMovieGallery(MultipartFile file) {
        return ResponseEntity.ok(fileService.uploadMovieGallery(file));
    }

    @PostMapping("/upload-asset")
    public ResponseEntity<FileDto> uploadMovieAsset(MultipartFile file) {
        return ResponseEntity.ok(fileService.uploadMovieAsset(file));
    }
}
