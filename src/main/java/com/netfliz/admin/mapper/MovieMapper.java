package com.netfliz.admin.mapper;

import com.netfliz.admin.dto.MovieDto;
import com.netfliz.admin.entity.MovieEntity;
import com.netfliz.admin.util.JsonUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieMapper {

    public MovieEntity mapToEntity(MovieDto from) {
        return MovieEntity.builder()
                .title(from.getTitle())
                .plot(from.getPlot())
                .rated(from.getRated())
                .genre(JsonUtils.parse(from.getGenre()))
                .released(from.getReleased())
                .year(from.getYear())
                .runtime(from.getRuntime())
                .director(from.getDirector())
                .writer(from.getWriter())
                .actors(from.getActors())
                .languages(JsonUtils.parse(from.getLanguages()))
                .countries(JsonUtils.parse(from.getCountries()))
                .awards(from.getAwards())
                .metaScore(from.getMetaScore())
                .imdbRating(from.getImdbRating())
                .genre(JsonUtils.parse(from.getGenre()))
                .type(from.getType())
                .updatedAt(new Date())
                .build();

    }

    public MovieDto buildFromEntity(MovieEntity from) {
        return MovieDto.builder()
                .id(from.getId())
                .title(from.getTitle())
                .year(from.getYear())
                .rated(from.getRated())
                .released(from.getReleased())
                .runtime(from.getRuntime())
                .genre(JsonUtils.parseList(from.getGenre(), String.class))
                .categories(JsonUtils.parseList(from.getCategories(), String.class))
                .director(from.getDirector())
                .actors(from.getActors())
                .writer(from.getWriter())
                .plot(from.getPlot())
                .languages(JsonUtils.parseList(from.getLanguages(), String.class))
                .countries(JsonUtils.parseList(from.getCountries(), String.class))
                .awards(from.getAwards())
                .metaScore(from.getMetaScore())
                .imdbRating(from.getImdbRating())
                .type(from.getType())
                .build();
    }

    public List<MovieDto> mapToDtoList(List<MovieEntity> movieEntities) {
        return movieEntities.stream().map(this::buildFromEntity).collect(Collectors.toList());
    }
}
