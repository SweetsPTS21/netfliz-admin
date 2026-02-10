package com.netfliz.admin.mapper;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.netfliz.admin.constant.enums.MovieImageType;
import com.netfliz.admin.constant.enums.MovieObjectType;
import com.netfliz.admin.dto.MovieImageDto;
import com.netfliz.admin.entity.MovieImageEntity;

@Component
public class MovieImageMapper {

    public MovieImageDto mapFromEntity(MovieImageEntity from) {
        return MovieImageDto.builder()
                .id(from.getId())
                .fileId(from.getFileId())
                .name(from.getName())
                .format(from.getFormat())
                .type(from.getImageType().getId())
                .url(from.getImageUrl())
                .build();
    }

    public MovieImageEntity mapToEntity(MovieImageDto from, Long objectId, MovieObjectType objectType) {
        return MovieImageEntity.builder()
                .name(from.getName())
                .format(from.getFormat())
                .objectId(objectId)
                .objectType(objectType)
                .fileId(Objects.isNull(from.getId()) ? 0L : from.getId())
                .imageType(MovieImageType.fromId(from.getType()))
                .imageUrl(from.getUrl())
                .build();
    }

    public List<MovieImageDto> mapFromEntities(List<MovieImageEntity> from) {
        return from.stream().map(this::mapFromEntity).toList();
    }

    public List<MovieImageEntity> mapToEntities(List<MovieImageDto> from, Long objectId, MovieObjectType objectType) {
        return from.stream().map(movieImage -> mapToEntity(movieImage, objectId, objectType)).toList();
    }
}