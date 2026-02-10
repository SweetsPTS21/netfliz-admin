package com.netfliz.admin.validator;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.netfliz.admin.constant.enums.MovieImageType;
import com.netfliz.admin.dto.MovieImageDto;

import jakarta.validation.ValidationException;

@Component
public class MovieValidator {

    public void validateMovieImage(List<MovieImageDto> images) {
        images.forEach(image -> {
            if (image.getId() == null) {
                throw new ValidationException(
                        "File ảnh không tồn tại cho loại " + MovieImageType.fromId(image.getType()));
            }
        });

        // các image type không phải gallery sẽ giới hạn 1
        Map<Integer, List<MovieImageDto>> map = images.stream().collect(Collectors.groupingBy(MovieImageDto::getType));
        map.forEach((type, value) -> {
            if (!Objects.equals(type, MovieImageType.GALLERY.getId()) && value.size() > 1) {
                throw new ValidationException("Phim chỉ có thể có một " + MovieImageType.fromId(type));
            }
        });
    }
}
