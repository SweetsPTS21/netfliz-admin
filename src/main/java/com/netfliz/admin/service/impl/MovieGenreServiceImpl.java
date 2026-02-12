package com.netfliz.admin.service.impl;

import com.netfliz.admin.dto.MovieGenreDto;
import com.netfliz.admin.mapper.MovieGenreMapper;
import com.netfliz.admin.repository.MovieGenreRepository;
import com.netfliz.admin.service.MovieGenreService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MovieGenreServiceImpl implements MovieGenreService {
    private final MovieGenreRepository movieGenreRepository;

    @Override
    public List<MovieGenreDto> getAllMovieGenres() {
        return movieGenreRepository.findAll()
                .stream()
                .map(MovieGenreMapper::mapToDto)
                .toList();
    }
}
