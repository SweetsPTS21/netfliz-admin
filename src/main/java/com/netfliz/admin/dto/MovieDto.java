package com.netfliz.admin.dto;

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
public class MovieDto {
    private Long id;
    @NotNull(message = "Tên phim không được để trống")
    private String title;
    @NotNull(message = "Năm sản xuất không được để trống")
    private Integer year;
    @NotNull(message = "Đánh giá không được để trống")
    private String rated;
    private String released;
    private String runtime;
    @NotNull(message = "Loại phim không được để trống")
    private List<String> genre;
    private List<String> categories;
    private String director;
    private String writer;
    private String actors;
    @NotBlank(message = "Plot không được để trống")
    private String plot;
    @NotNull(message = "Ngôn ngữ không được để trống")
    private List<String> languages;
    @NotNull(message = "Quốc gia không được để trống")
    private List<String> countries;
    private String awards;
    @NotNull(message = "Meta score không được để trống")
    private Long metaScore;
    private String imdbRating;
    @NotBlank(message = "Loại phim không được để trống")
    private String type;
    private List<MovieImageDto> images;
    private List<MovieAssetDto> assets;
}
