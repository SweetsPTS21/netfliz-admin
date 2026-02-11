package com.netfliz.admin.controller.graphql;

import com.netfliz.admin.dto.MovieDto;
import com.netfliz.admin.dto.request.MovieFilterRequest;
import com.netfliz.admin.dto.response.PageResponse;
import com.netfliz.admin.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@AllArgsConstructor
public class MovieGraphql {
    private final MovieService movieService;

    @QueryMapping
    public PageResponse<MovieDto> getMoviesByFilter(@Argument(name = "request") MovieFilterRequest request) {
        return movieService.getMoviesByFilter(request);
    }

    @QueryMapping
    public MovieDto getMovieById(@Argument(name = "id") Long id) {
        return movieService.getMovieById(id);
    }

    @MutationMapping
    public MovieDto createMovie(@Argument(name = "movie") MovieDto movie) {
        return movieService.createMovie(movie);
    }

    @MutationMapping
    public Integer createListMovie(@Argument(name = "movies") List<MovieDto> movies) {
        return movieService.bulkMovie(movies);
    }

    @MutationMapping
    public MovieDto updateMovie(@Argument(name = "id") Long id,
            @Argument(name = "movie") MovieDto movie) {
        return movieService.updateMovie(id, movie);
    }

    @MutationMapping
    public String deleteMovie(@Argument Long id) {
        movieService.deleteMovie(id);
        return "Xóa phim thành công";
    }
}
