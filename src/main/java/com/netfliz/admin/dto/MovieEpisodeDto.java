package com.netfliz.admin.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieEpisodeDto {
    private Long id;
    @NotBlank(message = "Tên tập phim không được để trống")
    private String name;
    @NotNull(message = "Số thứ tự tập không được để trống")
    private Integer episodeNumber;
    @NotNull(message = "Vị trí tập không để trống")
    private Integer episodeOrder;
    @NotNull(message = "Mô tả không được để trống")
    private String description;
    private Boolean isPublished = Boolean.TRUE;
    @NotNull(message = "Thời lượng tập không để trống")
    @Min(1)
    private Integer runtime;
    private String metadata;
    private List<MovieAssetDto> assets;
    private List<MovieImageDto> posters;
}
