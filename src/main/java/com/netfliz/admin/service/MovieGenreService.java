package com.netfliz.admin.service;

import com.netfliz.admin.dto.MovieGenreDto;

import java.util.List;

public interface MovieGenreService {
    /**
     * Get all movie genres
     */
    List<MovieGenreDto> getAllMovieGenres();
}
