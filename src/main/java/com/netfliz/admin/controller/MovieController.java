package com.netfliz.admin.controller;

import com.netfliz.admin.dto.MovieDto;
import com.netfliz.admin.dto.request.MovieFilterRequest;
import com.netfliz.admin.dto.response.PageResponse;
import com.netfliz.admin.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/movies")
@AllArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/filter")
    public PageResponse<MovieDto> getMoviesByFilter(MovieFilterRequest request) {
        return movieService.getMoviesByFilter(request);
    }

    @GetMapping("/{id}")
    public MovieDto getMovieById(Long id) {
        return movieService.getMovieById(id);
    }

    @PostMapping()
    public MovieDto createMovie(@RequestBody MovieDto request) {
        return movieService.createMovie(request);
    }

    @PutMapping("/{id}")
    public MovieDto updateMovie(@PathVariable Long id, @RequestBody MovieDto request) {
        return movieService.updateMovie(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }
}
