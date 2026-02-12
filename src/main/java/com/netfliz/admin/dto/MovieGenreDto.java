package com.netfliz.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieGenreDto {
    private Integer id;
    private String name;
    private String title;
    private String description;
    private String slug;
}
