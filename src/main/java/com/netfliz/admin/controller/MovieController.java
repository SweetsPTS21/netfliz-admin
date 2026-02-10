package com.netfliz.admin.controller;

import com.netfliz.admin.dto.MovieDto;
import com.netfliz.admin.dto.request.MovieFilterRequest;
import com.netfliz.admin.dto.response.PageResponse;
import com.netfliz.admin.service.MovieService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
@AllArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @PostMapping("/filter")
    public PageResponse<MovieDto> getMoviesByFilter(MovieFilterRequest request) {
        return movieService.getMoviesByFilter(request);
    }

    @GetMapping("/{id}")
    public MovieDto getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    @PostMapping("")
    public MovieDto createMovie(@RequestBody @Valid MovieDto request) {
        return movieService.createMovie(request);
    }

    @PutMapping("/{id}")
    public MovieDto updateMovie(@PathVariable Long id, @RequestBody @Valid MovieDto request) {
        return movieService.updateMovie(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }

    @PostMapping("/bulk")
    public Integer bulkMovie(@RequestBody List<@Valid MovieDto> movies) {
        return movieService.bulkMovie(movies);
    }
}
