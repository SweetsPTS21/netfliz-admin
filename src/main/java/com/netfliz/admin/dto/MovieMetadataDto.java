package com.netfliz.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieMetadataDto {
    private List<Metadata> genres;
    private List<Metadata> countries;
    private List<Metadata> languages;
    private List<Metadata> rated;
    private List<Metadata> types;
    private List<Metadata> years;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Metadata {
        private String value;
        private String description;
        private String slug;
    }
}
