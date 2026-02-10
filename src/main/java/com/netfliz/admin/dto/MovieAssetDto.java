package com.netfliz.admin.dto;

import com.netfliz.admin.entity.MovieAssetEntity;
import com.netfliz.admin.util.JsonUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieAssetDto {
    private Long id;
    @NotNull(message = "File ID không được để trống")
    private Long fileId;
    @NotNull(message = "Loại asset không được để trống")
    private Integer assetType;
    @NotBlank(message = "Format không được để trống")
    private String format;
    @NotBlank(message = "Tên không được để trống")
    private String name;
    @NotBlank(message = "URL không được để trống")
    private String url;
    private String drm;
    private String renditions;

    public static MovieAssetDto buildFromEntity(MovieAssetEntity from) {
        return MovieAssetDto.builder()
                .id(from.getId())
                .fileId(from.getFileId())
                .assetType(from.getAssetType().getId())
                .format(from.getFormat())
                .name(from.getName())
                .url(from.getUrl())
                .renditions(JsonUtils.serialize(from.getRendition()))
                .drm(JsonUtils.serialize(from.getDrm()))
                .build();
    }
}
