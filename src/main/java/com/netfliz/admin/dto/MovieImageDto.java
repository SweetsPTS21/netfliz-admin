package com.netfliz.admin.dto;

import com.netfliz.admin.entity.MovieImageEntity;
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
public class MovieImageDto {
    private Long id;
    @NotNull(message = "File ID không được để trống")
    private Long fileId;
    @NotBlank(message = "Tên không được để trống")
    private String name;
    @NotBlank(message = "Format không được để trống")
    private String format;
    @NotNull(message = "Type không được để trống")
    private Integer type;
    @NotBlank(message = "URL không được để trống")
    private String url;

    public static MovieImageDto buildFromEntity(MovieImageEntity from) {
        return MovieImageDto.builder()
                .id(from.getId())
                .fileId(from.getFileId())
                .name(from.getName())
                .format(from.getFormat())
                .type(from.getImageType().getId())
                .url(from.getImageUrl())
                .build();
    }
}
