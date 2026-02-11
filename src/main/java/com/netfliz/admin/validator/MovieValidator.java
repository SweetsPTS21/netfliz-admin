package com.netfliz.admin.validator;

import com.netfliz.admin.constant.enums.MovieImageType;
import com.netfliz.admin.constant.enums.MovieType;
import com.netfliz.admin.dto.MovieImageDto;
import com.netfliz.admin.entity.MovieEntity;
import com.netfliz.admin.exception.NotFoundException;
import com.netfliz.admin.repository.MovieRepository;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MovieValidator {
    private final MovieRepository movieRepository;

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

    public void validateSeriesMovie(Long movieId) {
        MovieEntity movie = movieRepository.findById(movieId).orElseThrow(
                () -> new NotFoundException("Phim không tồn tại")
        );

        if (!MovieType.SERIES.getValue().equalsIgnoreCase(movie.getType())) {
            throw new ValidationException("Phim không phải là phim bộ");
        }
    }
}
