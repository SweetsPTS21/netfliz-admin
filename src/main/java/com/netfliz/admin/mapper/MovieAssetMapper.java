package com.netfliz.admin.mapper;

import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import com.netfliz.admin.constant.enums.MovieAssetType;
import com.netfliz.admin.constant.enums.MovieObjectType;
import com.netfliz.admin.dto.MovieAssetDto;
import com.netfliz.admin.entity.MovieAssetEntity;
import com.netfliz.admin.util.JsonUtils;

@Component
public class MovieAssetMapper {
    public MovieAssetDto mapFromEntity(MovieAssetEntity from) {
        return MovieAssetDto.builder()
                .id(from.getId())
                .fileId(from.getFileId())
                .name(from.getName())
                .url(from.getUrl())
                .assetType(from.getAssetType().getId())
                .format(from.getFormat())
                .drm(JsonUtils.serialize(from.getDrm()))
                .renditions(JsonUtils.serialize(from.getRendition()))
                .build();
    }

    public MovieAssetEntity mapToEntity(Long objectId, MovieObjectType objectType, MovieAssetDto from) {
        return MovieAssetEntity.builder()
                .name(from.getName())
                .format(from.getFormat())
                .objectId(objectId)
                .objectType(objectType)
                .url(from.getUrl())
                .assetType(MovieAssetType.fromId(from.getAssetType()))
                .fileId(Objects.isNull(from.getFileId()) ? 0L : from.getFileId())
                .drm(JsonUtils.parse(from.getDrm()))
                .rendition(JsonUtils.parse(from.getRenditions()))
                .build();
    }

    public List<MovieAssetEntity> mapToEntities(Long objectId, MovieObjectType objectType, List<MovieAssetDto> from) {
        return from.stream().map(movieAsset -> mapToEntity(objectId, objectType, movieAsset)).toList();
    }

    public List<MovieAssetDto> mapFromEntities(List<MovieAssetEntity> from) {
        return from.stream().map(this::mapFromEntity).toList();
    }
}
