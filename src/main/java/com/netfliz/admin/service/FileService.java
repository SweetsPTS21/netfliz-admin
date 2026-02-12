package com.netfliz.admin.service;

import com.netfliz.admin.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<FileDto> uploadMoviePoster(MultipartFile file);

    FileDto uploadMovieGallery(MultipartFile file);

    FileDto uploadMovieAsset(MultipartFile file);
}
