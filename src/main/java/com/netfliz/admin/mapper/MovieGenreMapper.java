package com.netfliz.admin.mapper;

import com.netfliz.admin.dto.MovieGenreDto;
import com.netfliz.admin.entity.MovieGenreEntity;

import java.util.List;

public class MovieGenreMapper {
    public static MovieGenreDto mapToDto(MovieGenreEntity from) {
        return MovieGenreDto.builder()
                .id(from.getId())
                .name(from.getName())
                .title(from.getTitle())
                .description(from.getDescription())
                .slug(from.getSlug())
                .build();
    }

    public static MovieGenreEntity mapToEntity(MovieGenreDto from) {
        return MovieGenreEntity.builder()
                .name(from.getName())
                .title(from.getTitle())
                .description(from.getDescription())
                .slug(from.getSlug())
                .build();
    }

    public List<MovieGenreDto> mapToListDto(List<MovieGenreEntity> entityList) {
        return entityList.stream().map(MovieGenreMapper::mapToDto).toList();
    }
}
